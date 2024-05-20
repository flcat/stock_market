package com.flcat.stock_market.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private BigDecimal volume;
    private BigDecimal movingAverage;

}
