package com.flcat.stock_market.controller;

import com.flcat.stock_market.dto.TradeSignal;
import com.flcat.stock_market.service.impl.QuantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/quant")
@RequiredArgsConstructor
public class QuantController {

    private final QuantService quantService;

    @GetMapping("/trade-signals")
    public ResponseEntity<List<TradeSignal>> getTradeSignals(@RequestParam("symbol") String symbol) {
        List<TradeSignal> tradeSignals = quantService.calculateTradeSignals(symbol);
        return new ResponseEntity<>(tradeSignals, HttpStatus.OK);
    }
}
