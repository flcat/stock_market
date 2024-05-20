package com.flcat.stock_market.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flcat.stock_market.config.KisConfig;
import com.flcat.stock_market.dto.OrderData;
import com.flcat.stock_market.exception.FailedAuthenticationException;
import com.flcat.stock_market.exception.FailedResponseException;
import com.flcat.stock_market.service.SlackService;
import com.flcat.stock_market.util.AuthenticationManager;
import com.flcat.stock_market.util.HttpRequester;
import com.flcat.stock_market.util.KiRequestHelper;
import com.flcat.stock_market.util.RequestPaper;
import com.flcat.stock_market.vo.tttt1002u.TTTT1002UOrder;
import com.flcat.stock_market.vo.tttt1002u.TTTT1002URes;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StocksOrderService {

    private final KisConfig kisConfig;
    private final HttpRequester httpRequester;
    private final AuthenticationManager authenticationManager;
    private final SlackService slackService;
    private String accessToken;

    private String macAddresss;
    private String ipAddress;
    private String accountNo;

    private String stockCode;
    private String quantity;

    @PostConstruct
    protected void init() throws IOException, InterruptedException, FailedResponseException, ParseException {
        log.info("[init] AccessToken 초기화 시작");
        this.authenticationManager.createToken();
        macAddresss = getMacAddress();
        ipAddress = getIpV4Address();
        scheduleAccessTokenRefresh();
        accountNo = this.kisConfig.getAccountNo();
        log.info("[init] AccessToken 초기화 완료");
    }

    private void scheduleAccessTokenRefresh() {
        TimerTask timerTask = new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                try {
                    accessToken = authenticationManager.getToken();
                    System.out.println(accessToken);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Timer timer = new Timer();
        long delay = 0;
        long interval = 12 * 60 * 60 * 1000;

        timer.scheduleAtFixedRate(timerTask, delay, interval);
    }

    private String getMacAddress() throws SocketException {
        Enumeration<NetworkInterface> networkInterface = NetworkInterface.getNetworkInterfaces();

        while (networkInterface.hasMoreElements()) {

            NetworkInterface network = networkInterface.nextElement();
            byte[] mac = network.getHardwareAddress();

            if (mac != null) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                return sb.toString();
            }
        }
        return null;
    }

    private String getIpV4Address() {
        String result = "";
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        result = ip.toString();
        return result;
    }

    public void execute() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        if (!this.authenticationManager.isAuth()) {
            throw new FailedAuthenticationException();
        }

        RequestPaper requestPaper = this.createPaper();
        System.out.println("request Paper : " + this.createPaper().toString());
        TTTT1002URes response = this.httpRequester.call(requestPaper, TTTT1002URes.class);
        Object object = response.getOutput();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String json = objectMapper.writeValueAsString(object);
        List<TTTT1002URes> dtos = Collections.singletonList(objectMapper.readValue(json, TTTT1002URes.class));

        if (object == null) {
            return;
        }

        String message = this.createMessage(dtos);
        sendMessageToSlack(message);
    }

    private void sendMessageToSlack(String message) {
        HashMap<String, String> data = new HashMap<>();
        data.put("Order Result", message);
        slackService.sendMessage("Overseas Stock Order Result", data);
    }

    public TTTT1002UOrder orderRequest() {
        OrderData orderDataDto = new OrderData();
        stockCode = orderDataDto.getStockCode();
        quantity = orderDataDto.getQuantity();
        TTTT1002UOrder orderResponse = new TTTT1002UOrder();
        orderResponse.setCANO(accountNo.substring(0, 8));
        orderResponse.setACNT_PRDT_CD(accountNo.substring(8, 10));
        orderResponse.setOVRS_EXCG_CD("NASD");
        orderResponse.setPDNO(stockCode);
        orderResponse.setORD_QTY(quantity);
        orderResponse.setOVRS_ORD_UNPR("0");
        orderResponse.setCTAC_TLNO("01046217342");
        orderResponse.setMGCO_APTM_ODNO("");
        orderResponse.setORD_SVR_DVSN_CD("0");
        orderResponse.setORD_DVSN("00");
        return orderResponse;
    }

    private RequestPaper createPaper() throws JsonProcessingException {
        return new RequestPaper()
                .setMethod("POST")
                .setUri(kisConfig.getREST_BASE_URL() + "/uapi/overseas-stock/v1/trading/order")
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8")
                .putHeader("authorization", "Bearer " + this.authenticationManager.getToken())
                .putHeader("appkey", this.kisConfig.getAPPKEY())
                .putHeader("appsecret", this.kisConfig.getAPPSECRET())
                .putHeader("tr_id", "TTTT1002U")
                .putHeader("tr_cont", "")
                .putHeader("custtype", "P")
                .putHeader("mac_address", macAddresss)
                .putHeader("phone_number", "01046217342")
                .putHeader("ip_addr", ipAddress)
                .putHeader("gt_uid", KiRequestHelper.makeGtUid())
                .setBody(orderRequest());
    }

    private String createMessage(List<TTTT1002URes> dtos) {
        StringBuilder message = new StringBuilder();

        for (TTTT1002URes row : dtos) {
            message.append("Success or Failure: ").append(row.getTr_cd()).append("\n");
            message.append("Response Code: ").append(row.getMsg_cd()).append("\n");
            message.append("Response Message: ").append(row.getMsg1()).append("\n");
            message.append("Response Details: ").append(row.getOutput()).append("\n");
            message.append("KRX Forwarding Order Organization Number: ").append(row.getKRX_FWDG_ORD_ORGNO()).append("\n");
            message.append("Order Number: ").append(row.getODNO()).append("\n");
            message.append("Order Time: ").append(row.getORD_TMD()).append("\n");
        }

        return message.toString();
    }
}