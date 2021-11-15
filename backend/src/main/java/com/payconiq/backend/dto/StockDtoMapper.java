package com.payconiq.backend.dto;

import com.payconiq.backend.domain.Stock;
import org.springframework.beans.BeanUtils;

public class StockDtoMapper {

    public static StockResponseDto map(Stock stock) {
        StockResponseDto stockDto = new StockResponseDto();
        BeanUtils.copyProperties(stock, stockDto);
        return stockDto;
    }

    public static Stock map(StockRequestDto stockRequestDto) {
        Stock stock = new Stock();
        BeanUtils.copyProperties(stockRequestDto, stock);
        return stock;
    }

    public static Stock map(StockResponseDto stockResponseDto) {
        Stock stock = new Stock();
        BeanUtils.copyProperties(stockResponseDto, stock);
        return stock;
    }
}
