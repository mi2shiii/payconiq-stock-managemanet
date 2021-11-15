package com.payconiq.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payconiq.backend.domain.Stock;
import com.payconiq.backend.dto.StockDtoMapper;
import com.payconiq.backend.dto.StockRequestDto;
import com.payconiq.backend.dto.StockResponseDto;
import com.payconiq.backend.exception.EntityIsLockedException;
import com.payconiq.backend.exception.EntityNotFoundException;
import com.payconiq.backend.service.StockService;
import com.payconiq.backend.util.DateProcessor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockController.class)
public class StockControllerTest {

    private final String baseUri = "/api/stocks/";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private StockService stockService;

    @Test
    void findStock_ForSavedStock_Returned() throws Exception {
        given(stockService.findById(anyLong())).willReturn(
                Stock.builder()
                        .id(1L)
                        .name("Apple")
                        .currentPrice(10.0)
                        .build()
        );

        mockMvc.perform(get(baseUri + "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("Apple"))
                .andExpect(jsonPath("currentPrice").value(10.0));
    }

    @Test
    void findStock_ForMissingStock_status404() throws Exception {
        given(stockService.findById(anyLong())).willThrow(EntityNotFoundException.class);

        mockMvc.perform(get(baseUri + "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAll_returnStocks() throws Exception {
        given(stockService.findAll()).willReturn(List.of(
                Stock.builder()
                        .id(1L)
                        .name("Apple")
                        .currentPrice(10.0)
                        .build(),
                Stock.builder()
                        .id(2L)
                        .name("Amazon")
                        .currentPrice(9.0)
                        .build()
        ));

        mockMvc.perform(get(baseUri))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Apple"))
                .andExpect(jsonPath("$[0].currentPrice").value(10.0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Amazon"))
                .andExpect(jsonPath("$[1].currentPrice").value(9.0));
    }

    @Test
    public void createStock_ReturnsStock() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        StockRequestDto input = StockRequestDto.builder()
                .name("Apple")
                .currentPrice(10.0)
                .build();
        StockResponseDto expected = StockResponseDto.builder()
                .id(1L)
                .name("Apple")
                .currentPrice(10.0)
                .createdAt(now)
                .lastUpdate(now)
                .build();

        Mockito.when(stockService.create(StockDtoMapper.map(input))).thenReturn(StockDtoMapper.map(expected));

        String inputJsonString = objectMapper.writeValueAsString(input);
        mockMvc
                .perform(
                        post(baseUri).content(inputJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.currentPrice").value(10.0))
                .andExpect(jsonPath("$.createdAt").value(DateProcessor.toString(now)))
                .andExpect(jsonPath("$.lastUpdate").value(DateProcessor.toString(now)));
    }

    @Test
    public void createStock_withMissingName_status400() throws Exception {
        StockRequestDto input = StockRequestDto.builder()
                .currentPrice(10.0)
                .build();
        mockMvc.perform(
                        post(baseUri)
                                .content(objectMapper.writeValueAsString(input))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createStock_withMissingPrice_status400() throws Exception {
        StockRequestDto input = StockRequestDto.builder()
                .name("Apple")
                .build();
        mockMvc.perform(
                        post(baseUri)
                                .content(objectMapper.writeValueAsString(input))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createStock_withNegativePrice_status400() throws Exception {
        StockRequestDto input = StockRequestDto.builder()
                .name("Apple")
                .currentPrice(-10.0)
                .build();
        mockMvc.perform(
                        post(baseUri)
                                .content(objectMapper.writeValueAsString(input))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateStock_stockIsNotLocked_returnsStock() throws Exception {
        LocalDateTime now = LocalDateTime.now();

        StockRequestDto input = StockRequestDto.builder()
                .name("Apple")
                .currentPrice(10.0)
                .build();
        StockResponseDto expected = StockResponseDto.builder()
                .id(1L)
                .name("Apple")
                .currentPrice(10.0)
                .createdAt(now)
                .lastUpdate(now)
                .build();
        Mockito.when(stockService.update(1L, StockDtoMapper.map(input))).thenReturn(StockDtoMapper.map(expected));

        this.mockMvc
                .perform(put(baseUri + "{id}", 1)
                        .content(objectMapper.writeValueAsString(input))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.currentPrice").value(10.0))
                .andExpect(jsonPath("$.createdAt").value(DateProcessor.toString(now)))
                .andExpect(jsonPath("$.lastUpdate").value(DateProcessor.toString(now)));
    }

    @Test
    public void updateStock_stockIsLocked_status400() throws Exception {
        StockRequestDto input = StockRequestDto.builder()
                .name("Apple")
                .currentPrice(10.0)
                .build();

        Mockito.when(stockService.update(1L, StockDtoMapper.map(input))).thenThrow(EntityIsLockedException.class);

        this.mockMvc
                .perform(put(baseUri + "{id}", 1)
                        .content(objectMapper.writeValueAsString(input))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateStock_stockIsMissing_status404() throws Exception {
        StockRequestDto input = StockRequestDto.builder()
                .name("Apple")
                .currentPrice(10.0)
                .build();

        Mockito.when(stockService.update(1L, StockDtoMapper.map(input))).thenThrow(EntityNotFoundException.class);

        this.mockMvc
                .perform(put(baseUri + "{id}", 1)
                        .content(objectMapper.writeValueAsString(input))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteStock_stockIsNotLocked_deletesStock() throws Exception {

        Mockito.when(stockService.delete(1L)).thenReturn(Boolean.TRUE);

        mockMvc.perform(delete(baseUri + "{id}", 1))
                .andExpect(status().isAccepted());
    }

    @Test
    public void deleteStock_stockIsLocked_status400() throws Exception {
        Mockito.when(stockService.delete(1L)).thenThrow(EntityIsLockedException.class);

        this.mockMvc
                .perform(delete(baseUri + "{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteStock_stockIsMissing_status404() throws Exception {
        Mockito.when(stockService.delete(1L)).thenThrow(EntityNotFoundException.class);

        this.mockMvc
                .perform(delete(baseUri + "{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteStock_unknownHappens_status500() throws Exception {
        Mockito.when(stockService.delete(1L)).thenThrow(RuntimeException.class);

        this.mockMvc
                .perform(delete(baseUri + "{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isInternalServerError());
    }
}
