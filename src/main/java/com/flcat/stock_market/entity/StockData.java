package com.flcat.stock_market.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table
@Getter
@Setter
public class StockData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String symbol;
    private LocalDate date;
    private double openPrice;
    private double highPrice;
    private double lowPrice;
    private double closePrice;
    private double volume;
    private double movingAverage;

}
