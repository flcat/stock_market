package com.flcat.stock_market.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StocksDto {

    private String stocksCode;
    private String lastPrice;
    private String rate;
}
