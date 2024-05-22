package com.flcat.stock_market.controller;

import com.flcat.stock_market.dto.PageResponse;
import com.flcat.stock_market.service.PriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Controller
@RequestMapping("/api/v1/price")
public class PriceController {
    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping("/nasdaq")
    public String nasdaqView() {
        return "nasdaq";
    }

    @GetMapping("/nasdaq/data")
    @ResponseBody
    public CompletableFuture<ResponseEntity<PageResponse<Map<String, Object>>>> getNasdaqStockPrice(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        long startTime = System.currentTimeMillis();
        startedInfoLog("getNasdaqStockPrice");

        return priceService.getMarketPriceFromPython(page, size)
                .thenApply(resultList -> {
                    int totalElements = priceService.getTotalElements();
                    int totalPages = (int) Math.ceil((double) totalElements / size);

                    PageResponse<Map<String, Object>> response = new PageResponse<>();
                    response.setContent(resultList);
                    response.setPage(page);
                    response.setSize(size);
                    response.setTotalElements(totalElements);
                    response.setTotalPages(totalPages);

                    finishedInfoLog("getNasdaqStockPrice", startTime);
                    return ResponseEntity.ok(response);
                });
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