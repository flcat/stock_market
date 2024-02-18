package com.flcat.stock_market.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class StocksEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String itemName;
    private String itemCode;
    private String stocksPrice;
    private double rate;


    public StocksEntity(String itemName, String itemCode, String stocksPrice, double rate) {
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.stocksPrice = stocksPrice;
        this.rate = rate;
    }
}
