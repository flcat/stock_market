package com.flcat.stock_market.controller;

import com.flcat.stock_market.service.LiveStocksStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class LiveStocksStatusController {

    private final LiveStocksStatusService liveStocksStatusService;


}
