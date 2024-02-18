package com.flcat.stock_market.service;

import com.flcat.stock_market.config.KisConfig;
import com.flcat.stock_market.exception.FailedAuthenticationException;
import com.flcat.stock_market.util.AuthenticationManager;
import com.flcat.stock_market.util.HttpRequester;
import com.flcat.stock_market.util.KiRequestHelper;
import com.flcat.stock_market.util.RequestPaper;
import com.flcat.stock_market.vo.tttt1002u.TTTT1002UDetailDTO;
import com.flcat.stock_market.vo.tttt1002u.TTTT1002URes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockBalanceService {

    private KisConfig kisConfig;
    private HttpRequester httpRequester;
    private AuthenticationManager authenticationManager;


    public void execute() throws Exception {

        if (!this.authenticationManager.isAuth()) {
            throw new FailedAuthenticationException();
        }

        RequestPaper requestPaper = this.createPaper();
        TTTT1002URes response = this.httpRequester.call(requestPaper, TTTT1002URes.class);

        List<TTTT1002UDetailDTO> detailDTOList = response.getOutput1();
        if (detailDTOList == null || detailDTOList.isEmpty()) {
            return;
        }

    }

    private RequestPaper createPaper() {
        String accountNO = this.kisConfig.getAccountNo();
        return new RequestPaper()
                .setMethod("GET")
                .setUri("https://openapi.koreainvestment.com:9443/uapi/overseas-stock/v1/trading/inquire-balance")
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .putHeader("gt_uid", KiRequestHelper.makeGtUid())
                .putHeader("authorization", "Bearer " + this.authenticationManager.getToken())
                .putHeader("appkey", this.kisConfig.getAPPKEY())
                .putHeader("appsecret", this.kisConfig.getAPPSECRET())
                .putHeader("tr_id", "TTTC8434R")
                .putQueryParam("CANO", accountNO.substring(0, 8))
                .putQueryParam("ACNT_PRDT_CD", accountNO.substring(8, 10))
                .putQueryParam("OVRS_EXCG_CD", "NASD") //NASD : 미국 전체 / NAS : 나스닥 / NYSE : 뉴욕 / AMEX : 아멕스
                .putQueryParam("TR_CRCY_CD", "USD")
                .putQueryParam("CTX_AREA_FK200", "")
                .putQueryParam("CTX_AREA_NK200", "");

    }
}
