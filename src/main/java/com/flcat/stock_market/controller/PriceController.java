package com.flcat.stock_market.controller;

import com.flcat.stock_market.dto.OverseasPriceDto;
import com.flcat.stock_market.service.PriceService;
import com.flcat.stock_market.service.SlackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class PriceController {

    private static final int PAGE_SIZE = 10;

    private final PriceService priceService;
    private final SlackService slackService;

    @GetMapping("/api/price/{ticker}")
    public String getMarketPrice(Model model,
                                 @PathVariable String ticker,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "") String search) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        try {
            this.startedInfoLog("getMarketPrice");
            Pageable pageable = PageRequest.of(page, PAGE_SIZE);
            Page<OverseasPriceDto> stockPage = priceService.getMarketPriceFromPython(ticker, search, pageable);
            int totalPages = stockPage.getTotalPages();

            List<OverseasPriceDto> stockList = stockPage.getContent();
            slackService.sendStockPriceToSlack(stockList);

            model.addAttribute("stockList", stockList);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("search", search);
            model.addAttribute("ticker", ticker);
        } catch (Exception e) {
            log.error("Error getting market price: {}", e.getMessage(), e);
        } finally {
            this.finishedInfoLog("getMarketPrice", startTime);
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