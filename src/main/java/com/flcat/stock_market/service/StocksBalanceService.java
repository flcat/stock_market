package com.flcat.stock_market.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flcat.stock_market.config.KisConfig;
import com.flcat.stock_market.exception.FailedAuthenticationException;
import com.flcat.stock_market.service.SlackService;
import com.flcat.stock_market.util.AuthenticationManager;
import com.flcat.stock_market.util.HttpRequester;
import com.flcat.stock_market.util.KiRequestHelper;
import com.flcat.stock_market.util.RequestPaper;
import com.flcat.stock_market.vo.tttt1002u.TTTT1002UBalanceDTO;
import com.flcat.stock_market.vo.tttt1002u.TTTT1002URes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StocksBalanceService {

    private final KisConfig kisConfig;
    private final HttpRequester httpRequester;
    private final AuthenticationManager authenticationManager;
    private final SlackService slackService;

    public void execute() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        if (!this.authenticationManager.isAuth()) {
            throw new FailedAuthenticationException();
        }

        RequestPaper requestPaper = this.createPaper();
        TTTT1002URes response = this.httpRequester.call(requestPaper, TTTT1002URes.class);
        Object object = response.getOutput2();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String json = objectMapper.writeValueAsString(object);
        List<TTTT1002UBalanceDTO> dtos = Collections.singletonList(objectMapper.readValue(json, TTTT1002UBalanceDTO.class));

        if (object == null) {
            return;
        }

        String message = this.createMessage(dtos);
        sendMessageToSlack(message);
    }

    private void sendMessageToSlack(String message) {
        HashMap<String, String> data = new HashMap<>();
        data.put("Account Balance", message);
        slackService.sendMessage("Account Balance", data);
    }

    private RequestPaper createPaper() {
        String accountNo = this.kisConfig.getAccountNo();
        return new RequestPaper()
                .setMethod("GET")
                .setUri(kisConfig.getREST_BASE_URL() + "/uapi/overseas-stock/v1/trading/inquire-balance")
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .putHeader("gt_uid", KiRequestHelper.makeGtUid())
                .putHeader("authorization", "Bearer " + this.authenticationManager.getToken())
                .putHeader("appkey", this.kisConfig.getAPPKEY())
                .putHeader("appsecret", this.kisConfig.getAPPSECRET())
                .putHeader("tr_id", "TTTS3012R")
                .putQueryParam("CANO", accountNo.substring(0, 8))
                .putQueryParam("ACNT_PRDT_CD", accountNo.substring(8, 10))
                .putQueryParam("OVRS_EXCG_CD", "NASD")
                .putQueryParam("TR_CRCY_CD", "USD")
                .putQueryParam("CTX_AREA_FK200", "")
                .putQueryParam("CTX_AREA_NK200", "");
    }

    private String createMessage(List<TTTT1002UBalanceDTO> dtos) {
        StringBuilder message = new StringBuilder();

        for (TTTT1002UBalanceDTO row : dtos) {
            message.append("Foreign Currency Purchase Amount 1: ").append(row.getFrcr_pchs_amt1()).append("\n");
            message.append("Overseas Realized Profit/Loss Amount: ").append(row.getOvrs_rlzt_pfls_amt()).append("\n");
            message.append("Overseas Total Profit/Loss: ").append(row.getOvrs_tot_pfls()).append("\n");
            message.append("Realized Profit Rate: ").append(row.getRlzt_erng_rt()).append("\n");
            message.append("Total Evaluation Profit/Loss Amount: ").append(row.getTot_evlu_pfls_amt()).append("\n");
            message.append("Total Profit Rate: ").append(row.getTot_pftrt()).append("\n");
            message.append("Foreign Currency Purchase Amount Total 1: ").append(row.getFrcr_buy_amt_smtl1()).append("\n");
            message.append("Overseas Realized Profit/Loss Amount 2: ").append(row.getOvrs_rlzt_pfls_amt2()).append("\n");
            message.append("Foreign Currency Purchase Amount Total 2: ").append(row.getFrcr_buy_amt_smtl2()).append("\n");
        }

        return message.toString();
    }
}