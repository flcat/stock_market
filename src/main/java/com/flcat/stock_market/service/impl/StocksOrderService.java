package com.flcat.stock_market.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flcat.stock_market.config.KisConfig;
import com.flcat.stock_market.exception.FailedAuthenticationException;
import com.flcat.stock_market.exception.FailedResponseException;
import com.flcat.stock_market.service.SlackService;
import com.flcat.stock_market.util.AuthenticationManager;
import com.flcat.stock_market.util.HttpRequester;
import com.flcat.stock_market.util.KiRequestHelper;
import com.flcat.stock_market.util.RequestPaper;
import com.flcat.stock_market.vo.tttt1002u.TTTT1002UBalanceDTO;
import com.flcat.stock_market.vo.tttt1002u.TTTT1002UOrder;
import com.flcat.stock_market.vo.tttt1002u.TTTT1002URes;
import jakarta.annotation.PostConstruct;
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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class StocksOrderService {

    @Autowired
    private KisConfig kisConfig;
    @Autowired
    private HttpRequester httpRequester;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SlackService slackService;
    private String accessToken;

    private String macAddresss;
    private String ipAddress;
    private String accountNo;

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
        List<TTTT1002URes> dtos = Arrays.asList(objectMapper.readValue(json, TTTT1002URes.class));

        if (object == null) {
            return;
        }

        String message = this.createMessage(dtos);
        sendMessageToUser(message);
    }

    private void sendMessageToUser(String message) {
        String title = "========================";
        HashMap<String, String> data = new HashMap<>();
        data.put("해외 주식 주문 결과", message);
        slackService.sendMessage(title,data);
    }

    private String selectStock() {
        String stock = "IVP";
        return stock;
    }

    private TTTT1002UOrder orderRequest() {
        TTTT1002UOrder orderResponse = new TTTT1002UOrder();
        orderResponse.setCANO(accountNo.substring(0, 8));
        orderResponse.setACNT_PRDT_CD(accountNo.substring(8, 10));
        orderResponse.setOVRS_EXCG_CD("NASD");
        orderResponse.setPDNO(selectStock());
        orderResponse.setORD_QTY("1");
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
            message.append("성공 실패 여부 : " + row.getTr_cd() + "\n");
            message.append("응답코드 : " + row.getMsg_cd() + "\n");
            message.append("응답메세지 : " + row.getMsg1() + "\n");
            message.append("응답상세 : " + row.getOutput() + "\n");
            message.append("한국거래소전송주문조직번호 : " + row.getKRX_FWDG_ORD_ORGNO() + "\n");
            message.append("주문번호 : " + row.getODNO() + "\n");
            message.append("주문시각 : " + row.getORD_TMD() + "\n");
        }

        return message.toString();
    }


}
