package com.payconiq.backend.controller;

import com.payconiq.backend.domain.Stock;
import com.payconiq.backend.dto.StockDtoMapper;
import com.payconiq.backend.dto.StockRequestDto;
import com.payconiq.backend.dto.StockResponseDto;
import com.payconiq.backend.exception.EntityNotFoundException;
import com.payconiq.backend.service.StockService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/stocks")
@Log
public class StockController {

    private final StockService stockService;

    @GetMapping("/")
    public List<StockResponseDto> findAll() {
        List<Stock> stockList = stockService.findAll();
        return stockList.stream().map(StockDtoMapper::map).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockResponseDto> findStockById(@PathVariable(value = "id") Long stockId)
            throws EntityNotFoundException {
        Stock foundStock = stockService.findById(stockId);

        log.info("The Stock entity with id={" + foundStock.getId() + "} was found successfully.");
        return ResponseEntity.ok().body(StockDtoMapper.map(foundStock));
    }

    @PostMapping("/")
    public ResponseEntity<StockResponseDto> createStock(@Valid @RequestBody StockRequestDto stockRequestDto) {
        Stock createdStock = stockService.create(StockDtoMapper.map(stockRequestDto));

        log.info("The Stock entity with id={" + createdStock.getId() + "} was created successfully.");
        return ResponseEntity.ok().body(StockDtoMapper.map(createdStock));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockResponseDto> updateStock(@PathVariable(value = "id") Long stockId,
                                                        @Valid @RequestBody StockRequestDto stock) throws EntityNotFoundException {
        Stock updatedStock = stockService.update(stockId, StockDtoMapper.map(stock));

        log.info("The Stock entity with id={" + stockId + "} was updated successfully.");
        return ResponseEntity.ok().body(StockDtoMapper.map(updatedStock));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteStock(@PathVariable(value = "id") Long stockId)
            throws EntityNotFoundException {
        stockService.delete(stockId);
        log.info("The Stock entity with id={" + stockId + "} was deleted successfully.");
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
