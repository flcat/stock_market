package com.flcat.stock_market.service.impl;

import com.flcat.stock_market.entity.BalanceEntity;
import com.flcat.stock_market.service.LiveStocksWebSocketService;
import com.flcat.stock_market.vo.tttt1002u.TTTT1002UBalanceDTO;
import com.flcat.stock_market.vo.tttt1002u.TTTT1002UPriceDTO;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.*;

@Slf4j
@Service
public class LiveStocksWebSocketServiceImpl implements LiveStocksWebSocketService {
    private String approvalToken;
    @Value("${app_key}")
    private String app_Key;
    @Value("${app_secret}")
    private String app_Secret;

    @PostConstruct
    protected void init() {
        log.info("ApprovalToken 초기화 시작");
//        scheduleTokenRefresh();
        log.info("ApprovalToken 초기화 완료");
    }

    private void scheduleTokenRefresh() {
        TimerTask timerTask = new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                try {
                    JSONObject body = createBody();
                    approvalToken = createApprovalToken(body);
                    System.out.println(approvalToken);
                } catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Timer timer = new Timer();
        long delay = 0;
        long interval = 12 * 60 * 60 * 1000;
        timer.scheduleAtFixedRate(timerTask, delay, interval);
    }

    public JSONObject createBody() throws JSONException {
        JSONObject body = new JSONObject();
        body.put("grant_type", "client_credentials");
        body.put("appkey", app_Key);
        body.put("appsecret", app_Secret);
        return body;
    }

    public String createApprovalToken(JSONObject body) throws IOException, JSONException {
        String apiUrl = "https://openapi.koreainvestment.com:9443/oauth2/tokenP";
        JSONObject result;
        HttpURLConnection connection;
        URL url = new URL(apiUrl);

        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = body.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            result = new JSONObject(response.toString());
        }
        return result.getString("access_token");
    }

    @Override
    public String getStocks() throws IOException, JSONException {
        BufferedReader br = null;
        HttpURLConnection connection = null;
        StringBuffer sb = new StringBuffer();
        String responseData = "";
        String returnData = "";

        try {
            String apiUrl = "https://openapi.koreainvestment.com:9443/uapi/overseas-stock/v1/trading/inquire-balance";
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("CANO", "64085748");
            params.put("ACNT_PRDT_CD", "01");
            params.put("OVRS_EXCG_CD", "NASD");
            params.put("TR_CRCY_CD", "USD");
            params.put("CTX_AREA_FK200", "");
            params.put("CTX_AREA_NK200", "");

            StringBuilder data = new StringBuilder();
            for(Map.Entry<String, Object> param : params.entrySet()) {
                if(data.length() != 0) data.append('&');
                data.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                data.append('=');
                data.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] dataBytes = data.toString().getBytes("UTF-8");

            URL url = new URL(apiUrl);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("authorization", "Bearer" + approvalToken);
            connection.setRequestProperty("appkey", app_Key);
            connection.setRequestProperty("appsecret", app_Secret);
            connection.setRequestProperty("tr_id", "TTTS3012R");
            connection.setRequestProperty("content-type", "application/json");
            connection.setDoOutput(true);
            connection.getOutputStream().write("?".getBytes("UTF-8"));
            connection.getOutputStream().write(dataBytes);

            try (OutputStream os = connection.getOutputStream()) {
                byte request_data[] = apiUrl.getBytes("UTF-8");
                os.write(request_data);
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            connection.connect();
            System.out.println();
            System.out.println("Http 요청 주소 : " + connection.getOutputStream().toString());

            br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            } catch (IOException e) {
            br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
            } finally {
                try {
                    sb = new StringBuffer();
                    while ((responseData = br.readLine()) != null) {
                        sb.append(responseData);
                    }
                    returnData = sb.toString();

                    String responseCode = String.valueOf(connection.getResponseCode());
                    System.out.println("Http 응답 코드 : " + responseCode);
                    System.out.println("Http 응답 데이터" + returnData);
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
                }
            }
        return "ok";
            }

    @Override
    public BalanceEntity getBalance(BalanceEntity entity) throws IOException, JSONException {
        HttpUrl.Builder urlBuilder = HttpUrl
                .parse("https://openapi.koreainvestment.com:9443/uapi/overseas-stock/v1/trading/inquire-balance")
                .newBuilder();
        urlBuilder.addQueryParameter("CANO", "64085748");
        urlBuilder.addQueryParameter("ACNT_PRDT_CD", "01");
        urlBuilder.addQueryParameter("OVRS_EXCG_CD", "NASD");
        urlBuilder.addQueryParameter("TR_CRCY_CD", "USD");
        urlBuilder.addQueryParameter("CTX_AREA_FK200", "");
        urlBuilder.addQueryParameter("CTX_AREA_NK200", "");
        String url = urlBuilder.build().toString();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("authorization", "Bearer" + approvalToken)
                .header("appkey", app_Key)
                .header("appsecret", app_Secret)
                .header("tr_id", "TTTS3012R")
                .header("content-type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONObject outputs = jsonObject.getJSONObject("output");
                entity.setCano((String) outputs.get("cano"));
                entity.setAcnt_prdt_cd((String) outputs.get("acnt_prdt_cd"));
                entity.setPrdt_type_cd((String) outputs.get("prdt_type_cd"));
                entity.setOvrs_pdno((String) outputs.get("ovrs_pdno"));
                entity.setOvrs_item_name((String) outputs.get("ovrs_item_name"));
                NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
                entity.setFrcr_evlu_pfls_amt(nf.format(Double.parseDouble((String) outputs.get("frcr_evlu_pfls_amt"))));
                entity.setEvlu_pfls_rt(nf.format(Double.parseDouble((String) outputs.get("evlu_pfls_rt"))));
                entity.setPchs_avg_pric(nf.format(Double.parseDouble((String) outputs.get("pchs_avg_pric"))));
                entity.setOvrs_cblc_qty(nf.format(Double.parseDouble((String) outputs.get("ovrs_cblc_qty"))));
                entity.setOrd_psbl_qty(nf.format(Double.parseDouble((String) outputs.get("ord_psbl_qty"))));
                entity.setFrcr_pchs_amt1(nf.format(Double.parseDouble((String) outputs.get("frcr_pchs_amt1"))));
                entity.setOvrs_stck_evlu_amt(nf.format(Double.parseDouble((String) outputs.get("ovrs_stck_evlu_amt"))));
                entity.setNow_pric2(nf.format(Double.parseDouble((String) outputs.get("now_pric2"))));

                entity.setTr_crcy_cd((String) outputs.get("tr_crcy_cd"));
                entity.setOvrs_excg_cd((String) outputs.get("ovrs_excg_cd"));
                entity.setLoan_type_cd((String) outputs.get("loan_type_cd"));
                entity.setLoan_dt((String) outputs.get("loan_dt"));
                entity.setExpd_dt((String) outputs.get("expd_dt"));
                entity.setOutput2((String) outputs.get("output2"));

                entity.setFrcr_pchs_amt1(nf.format(Double.parseDouble((String) outputs.get("frcr_pchs_amt1"))));
                entity.setOvrs_rlzt_pfls_amt(nf.format(Double.parseDouble((String) outputs.get("ovrs_rlzt_pfls_amt"))));
                entity.setOvrs_tot_pfls(nf.format(Double.parseDouble((String) outputs.get("ovrs_tot_pfls"))));
                entity.setRlzt_erng_rt(nf.format(Double.parseDouble((String) outputs.get("rlzt_erng_rt"))));
                entity.setTot_pftrt(nf.format(Double.parseDouble((String) outputs.get("tot_pftrt"))));
                entity.setFrcr_buy_amt_smtl1(nf.format(Double.parseDouble((String) outputs.get("frcr_buy_amt_smtl1"))));
                entity.setOvrs_rlzt_pfls_amt2(nf.format(Double.parseDouble((String) outputs.get("ovrs_rlzt_pfls_amt2"))));
                entity.setFrcr_buy_amt_smtl2(nf.format(Double.parseDouble((String) outputs.get("frcr_buy_amt_smtl2"))));

                entity.setRt_cd((String) outputs.get("rt_cd"));
                entity.setMsg_cd((String) outputs.get("msg_cd"));
                entity.setMsg1((String) outputs.get("msg1"));

                return entity;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}

