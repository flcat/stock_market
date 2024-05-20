package com.flcat.stock_market.dto;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TradeSignal {
    private LocalDate date;
    private String action;
    private BigDecimal price;

    public TradeSignal(LocalDate date, String action, BigDecimal price) {
        this.date = date;
        this.action = action;
        this.price = price;
    }
}
