//package com.flcat.stock_market.service.impl;
//
//import com.flcat.stock_market.data.PrintOpsData;
//import org.glassfish.tyrus.client.ClientManager;
//
//import javax.websocket.*;
//import java.net.URI;
//
//@ClientEndpoint
//public class WebsocketClientEndpoint {
//    Session userSession = null;
//    private MessageHandler messageHandler;
//    // aes256 key, iv
//    static String Key = null;	// 32byte
//    static String iv = null;	// 16byte
//
//
//    public WebsocketClientEndpoint(URI endpointURI) {
//        try {
//            ClientManager clientManager = ClientManager.createClient();
//            this.userSession = clientManager.connectToServer(this, endpointURI);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @OnOpen
//    public void onOpen(Session userSession) {
//        System.out.println("==== Connected Websocket!!");
//        this.userSession = userSession;
//    }
//
//    @OnClose
//    public void onClose(Session userSession, CloseReason reason) {
//        System.out.println("==== Closing Websocket");
//        this.userSession = null;
//    }
//
//    @OnMessage
//    public void onMessage(String message) {
//        if (this.messageHandler != null) {
//            PrintOpsData printOpsData = new PrintOpsData();
//            printOpsData.printMessage(message);
//        }
//    }
//
//    public void addMessageHandler(MessageHandler messageHandler) {
//        this.messageHandler = messageHandler;
//    }
//
//
//    public void sendMessage(String message) {
//        this.userSession.getAsyncRemote().sendText(message);
//    }
//
//    public static interface MessageHandler {
//        public void handleMessage(String message);
//    }
//
//}
