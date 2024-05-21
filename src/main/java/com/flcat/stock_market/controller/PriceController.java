package com.flcat.stock_market.controller;

import com.flcat.stock_market.dto.StockDto;
import com.flcat.stock_market.service.PriceService;
import com.flcat.stock_market.service.SlackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;
    private final SlackService slackService;

    @GetMapping("/api/price")
    public String getNasdaqStockPrice(Model model,
                                      @RequestParam(required = false) String ticker,
                                      @PageableDefault(size = 10) Pageable pageable,
                                      @RequestParam(required = false) String search) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        try {
            this.startedInfoLog("getNasdaqStockPrice");
            Page<StockDto> stockPricePage = priceService.getMarketPriceFromPython(ticker,search, pageable);
            slackService.sendStockPriceToSlack(stockPricePage.getContent());
            model.addAttribute("stockPricePage", stockPricePage);
            model.addAttribute("search", search);
        } catch (Exception e) {
            log.error("Error getting Nasdaq stock price: {}", e.getMessage(), e);
        } finally {
            this.finishedInfoLog("getNasdaqStockPrice", startTime);
        }
        return "stock-price";
    }

    private void startedInfoLog(String methodName) {
        if (log.isInfoEnabled()) {
            log.info("{} started.", methodName);
        }
    }

    private void finishedInfoLog(String methodName, long startTime) {
        if (log.isInfoEnabled()) {
            log.info("{} finished. elapsed time: {} ms", methodName, (System.currentTimeMillis() - startTime));
        }
    }
}