package com.flcat.stock_market.service;

import com.flcat.stock_market.dto.OverseasPriceDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SlackService {

    private final String webhookUrl;

    public SlackService(@Value("${webhook.slack.url}") String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public void sendStockPriceToSlack(List<OverseasPriceDto> stockList) {
        StringBuilder message = new StringBuilder();
        for (OverseasPriceDto stock : stockList) {
            message.append("Ticker: ").append(stock.getRsym()).append("\n");
            message.append("Price: ").append(stock.getLast()).append("\n");
            message.append("Volume: ").append(stock.getTvol()).append("\n");
            message.append("Change: ").append(stock.getDiff()).append(" (").append(stock.getRate()).append(")\n");
            message.append("\n");
        }

        sendMessageToSlack("Overseas Stock Price", message.toString());
    }

    public void sendMessage(String title, HashMap<String, String> data) {
        StringBuilder message = new StringBuilder();
        message.append(title).append("\n");
        for (Map.Entry<String, String> entry : data.entrySet()) {
            message.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        sendMessageToSlack(title, message.toString());
    }

    private void sendMessageToSlack(String title, String message) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payload = new HashMap<>();
        payload.put("text", title + "\n" + message);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);

        restTemplate.postForObject(webhookUrl, entity, String.class);
    }
}