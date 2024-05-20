package com.flcat.stock_market.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// MarketPrice.java
@Entity
@Table(name = "prices")
@Getter
@Setter
public class MarketPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String ticker;
    private BigDecimal price;
    private LocalDateTime updatedAt;

    // constructors, getters, setters, etc.
}