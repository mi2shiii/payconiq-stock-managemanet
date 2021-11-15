package com.payconiq.backend.dto;

import com.payconiq.backend.base.BaseDto;
import lombok.*;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockRequestDto extends BaseDto {

    @NotBlank(message = "Name is required!")
    private String name;

    @NotNull(message = "Price is required!")
    @DecimalMin(value = "0.0", message = "Price can not be negative!")
    private Double currentPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockRequestDto stockDto = (StockRequestDto) o;
        return Objects.equals(name, stockDto.name)
                && Objects.equals(currentPrice, stockDto.currentPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, currentPrice);
    }
}
