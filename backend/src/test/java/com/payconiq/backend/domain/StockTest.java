package com.payconiq.backend.domain;

import com.payconiq.backend.dto.StockRequestDto;
import com.payconiq.backend.dto.StockResponseDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.then;

public class StockTest {

    @Test
    public void stock_equalsHashcodeVerify() {
        LocalDateTime now = LocalDateTime.now();

        Stock stock1 = Stock.builder()
                .id(1L)
                .name("Apple")
                .currentPrice(10.0)
                .createdAt(now)
                .lastUpdate(now)
                .build();
        Stock stock2 = Stock.builder()
                .id(1L)
                .name("Apple")
                .currentPrice(10.0)
                .createdAt(now)
                .lastUpdate(now)
                .build();

        then(stock1).isEqualTo(stock2);
        then(stock1.hashCode()).isEqualTo(stock2.hashCode());
    }

    @Test
    public void stockRequestDto_equalsHashcodeVerify() {
        StockRequestDto stockRequestDto1 = StockRequestDto.builder()
                .name("Apple")
                .currentPrice(10.0)
                .build();
        StockRequestDto stockRequestDto2 = StockRequestDto.builder()
                .name("Apple")
                .currentPrice(10.0)
                .build();

        then(stockRequestDto1).isEqualTo(stockRequestDto2);
        then(stockRequestDto1.hashCode()).isEqualTo(stockRequestDto2.hashCode());
    }

    @Test
    public void stockResponseDto_equalsHashcodeVerify() {
        LocalDateTime now = LocalDateTime.now();

        StockResponseDto stockResponseDto1 = StockResponseDto.builder()
                .id(1L)
                .name("Apple")
                .currentPrice(10.0)
                .createdAt(now)
                .lastUpdate(now)
                .build();
        StockResponseDto stockResponseDto2 = StockResponseDto.builder()
                .id(1L)
                .name("Apple")
                .currentPrice(10.0)
                .createdAt(now)
                .lastUpdate(now)
                .build();

        then(stockResponseDto1).isEqualTo(stockResponseDto2);
        then(stockResponseDto1.hashCode()).isEqualTo(stockResponseDto2.hashCode());
    }
}
