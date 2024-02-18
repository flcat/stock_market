package com.flcat.stock_market.config;

import com.flcat.stock_market.dto.StocksDto;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class WebSockethandler extends TextWebSocketHandler {

    private static Set<WebSocketSession> CLIENTS = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info(session.toString());

        if (CLIENTS.contains(session)) {
            System.out.println("이미 연결된 세션입니다. : " + session);
        } else {
            CLIENTS.add(session);
            System.out.println("새로운 세션입니다. :" + session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        CLIENTS.remove(session);
        System.out.println("세션을 닫습니다. :" + session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) throws Exception {
        log.info("입력된 메세지 입니다. :" + message);

        String payload = message.getPayload();

        try {
            final WebSocketClientEndpoint clientEndpoint = new WebSocketClientEndpoint();

            Session session = clientEndpoint.connect(new URI("https://openapi.koreainvestment.com:9443"));
            clientEndpoint.sendMessage(payload);

            clientEndpoint.addMessageHandler(new WebSocketClientEndpoint.MessageHandler() {
                @Override
                public void handleMessage(String message) throws ParseException, IOException {
                    String[] parts = message.split("\\^");
                    String[] codepart = parts[0].split("\\|");

                    if (parts.length >= 4) {

                        String stocksCode = codepart[3];
                        String timeStamp = parts[1];
                        String lastPrice = parts[2];
                        String rate = parts[5];
                        String name = "0";


                        StocksDto stocksDto = new StocksDto();
                        stocksDto.setStocksCode(stocksCode);
                        stocksDto.setLastPrice(lastPrice);

                        String data = "종목명: " + name + "\n종목 코드: " + stocksCode + "\n현재가: " + lastPrice + "\n등락률: " + rate;
                        sendWebSocketMessage(data);
                    } else {
                        sendWebSocketMessage(message);
                    }
                }
            });

            Thread.sleep(5000);

        } catch (InterruptedException e) {
            sendWebSocketMessage("Invalid Message Format : " + e.getMessage());
        } catch (URISyntaxException e) {
            sendWebSocketMessage("URISyntaxException :" + e.getMessage());
        }
    }

    public void sendWebSocketMessage(String message) throws IOException {
        for (WebSocketSession client : CLIENTS) {
            TextMessage textMessage = new TextMessage(message);
            System.out.println("Sending Message to WebSocket Client: \n" + message);
            client.sendMessage(textMessage);
        }
    }
}
