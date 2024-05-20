package com.flcat.stock_market.dto;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TradeSignal {
    private LocalDate date;
    private String action;
    private double price;

    public TradeSignal(LocalDate date, String action, double price) {
        this.date = date;
        this.action = action;
        this.price = price;
    }
}
