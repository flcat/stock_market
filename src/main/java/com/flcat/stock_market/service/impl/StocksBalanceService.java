package com.flcat.stock_market.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flcat.stock_market.config.KisConfig;
import com.flcat.stock_market.exception.FailedAuthenticationException;
import com.flcat.stock_market.service.SlackService;
import com.flcat.stock_market.util.AuthenticationManager;
import com.flcat.stock_market.util.HttpRequester;
import com.flcat.stock_market.util.KiRequestHelper;
import com.flcat.stock_market.util.RequestPaper;
import com.flcat.stock_market.vo.tttt1002u.TTTT1002UBalanceDTO;
import com.flcat.stock_market.vo.tttt1002u.TTTT1002UDetailDTO;
import com.flcat.stock_market.vo.tttt1002u.TTTT1002URes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class StocksBalanceService {

    @Autowired
    private KisConfig kisConfig;
    @Autowired
    private HttpRequester httpRequester;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SlackService slackService;

    public void execute() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        if (!this.authenticationManager.isAuth()) {
            throw new FailedAuthenticationException();
        }

        RequestPaper requestPaper = this.createPaper();
        System.out.println(this.createPaper().toString());
        TTTT1002URes response = this.httpRequester.call(requestPaper, TTTT1002URes.class);
        Object object = response.getOutput2();
        String json = objectMapper.writeValueAsString(object);
        List<TTTT1002UBalanceDTO> dtos = Arrays.asList(objectMapper.readValue(json, TTTT1002UBalanceDTO.class));

        if (object == null) {
            return;
        }

        String message = this.createMessage(dtos);
        sendMessageToUser(message);
    }

    private void sendMessageToUser(String message) {
        String title = "========================";
        HashMap<String, String> data = new HashMap<>();
        data.put("계좌 잔고", message);
        slackService.sendMessage(title,data);
    }

    private RequestPaper createPaper() {
        String accountNo = this.kisConfig.getAccountNo();
        return new RequestPaper()
                .setMethod("GET")
                .setUri("https://openapi.koreainvestment.com:9443/uapi/overseas-stock/v1/trading/inquire-balance")
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .putHeader("gt_uid", KiRequestHelper.makeGtUid())
                .putHeader("authorization", "Bearer " + this.authenticationManager.getToken())
                .putHeader("appkey", this.kisConfig.getAPPKEY())
                .putHeader("appsecret", this.kisConfig.getAPPSECRET())
                .putHeader("tr_id", "TTTS3012R")
                .putQueryParam("CANO", accountNo.substring(0,8))
                .putQueryParam("ACNT_PRDT_CD", accountNo.substring(8,10))
                .putQueryParam("OVRS_EXCG_CD", "NASD")
                .putQueryParam("TR_CRCY_CD", "USD")
                .putQueryParam("CTX_AREA_FK200", "")
                .putQueryParam("CTX_AREA_NK200", "");
    }

    private String createMessage(List<TTTT1002UBalanceDTO> dtos) {

        StringBuilder message = new StringBuilder();

        for (TTTT1002UBalanceDTO row : dtos) {
            message.append("외화매입금액1 : " + row.getFrcr_pchs_amt1() + "\n");
            message.append("해외실현손익금액 : " + row.getOvrs_rlzt_pfls_amt() + "\n");
            message.append("해외총손익 : " + row.getOvrs_tot_pfls() + "\n");
            message.append("실현수익율 : " + row.getRlzt_erng_rt() + "\n");
            message.append("총평가손익금액 : " + row.getTot_evlu_pfls_amt() + "\n");
            message.append("총수익율 : " + row.getTot_pftrt() + "\n");
            message.append("외화매수금액합계1 : " + row.getFrcr_buy_amt_smtl1() + "\n");
            message.append("해외실현손익금액2 : " + row.getOvrs_rlzt_pfls_amt2() + "\n");
            message.append("외화매수금액합계2 : " + row.getFrcr_buy_amt_smtl2() + "\n");
        }

        return message.toString();
    }
}
