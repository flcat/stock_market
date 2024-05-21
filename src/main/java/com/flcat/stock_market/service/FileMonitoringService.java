package com.flcat.stock_market.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileMonitoringService {
    private final StockPriceDataService stockPriceDataService;

    @Value("${file.monitoring.directory}")
    private String monitoringDirectory;

    private WatchService watchService;
    private Map<String, LocalDateTime> uploadedFiles = new HashMap<>();

    @PostConstruct
    public void startMonitoring() {
        try {
            Path directory = Paths.get(monitoringDirectory);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
                log.info("Monitoring directory created: {}", monitoringDirectory);
            }
            watchService = FileSystems.getDefault().newWatchService();
            directory.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            log.info("File monitoring started for directory: {}", monitoringDirectory);
        } catch (IOException e) {
            log.error("Failed to start file monitoring for directory: {}", monitoringDirectory, e);
        }
    }
//    @Scheduled(fixedDelay = 1800000) // 30분마다 모니터링 수행
    @Scheduled(fixedDelay = 5000) // 5초마다 모니터링 수행
    public void monitor() throws InterruptedException {
        WatchKey key = watchService.take();
        for (WatchEvent<?> event : key.pollEvents()) {
            if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                String fileName = event.context().toString();
                LocalDateTime currentTime = LocalDateTime.now();

                if (!uploadedFiles.containsKey(fileName) ||
                        currentTime.isAfter(uploadedFiles.get(fileName).plusMinutes(30))) {
                    stockPriceDataService.saveStockPriceDataFromJsonFile(fileName);
                    uploadedFiles.put(fileName, currentTime);
                }
            }
        }
        key.reset();
    }
}