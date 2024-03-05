package com.flcat.stock_market.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flcat.stock_market.config.KisConfig;
import com.flcat.stock_market.exception.FailedResponseException;
import com.flcat.stock_market.vo.*;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

@Component
public class AuthenticationManager {

    private final HttpRequester httpRequester;
    private final TokenCreateReq tokenCreateReq;
    private final TokenRemoveReq tokenRemoveReq;
    private TokenCreateRes tokenCreateRes;
    private final ApprovalKeyReq approvalKeyReq;
    private ApprovalKeyRes approvalKeyRes;
    private boolean auth = false;

    @Autowired
    public AuthenticationManager(KisConfig kisConfig) {
        this.httpRequester = new HttpRequester();
        this.tokenCreateReq = new TokenCreateReq(kisConfig);
        this.tokenRemoveReq = new TokenRemoveReq(kisConfig);
        this.approvalKeyReq = new ApprovalKeyReq(kisConfig);
    }

    public boolean isAuth() {
        return auth;
    }

    public String getToken() {
        if (isAuth() && this.tokenCreateRes != null) {
            return this.tokenCreateRes.getAccessToken();
        } else {
            return null;
        }
    }

    public String getAccessKey() {
        if (isAuth() && this.approvalKeyRes != null) {
            return this.approvalKeyRes.getApproval_key();
        } else {
            return null;
        }
    }

    public synchronized void createToken() throws ParseException, IOException, InterruptedException, FailedResponseException {
        if (!isAuth() || this.tokenCreateRes == null || DateHelper.stringToLong("yyyy-MM-dd HH:mm:ss", this.tokenCreateRes.getExpired()) < new Date().getTime()) {
            RequestPaper tokenPaper = this.createTokenPaper();
            this.tokenCreateRes = this.httpRequester.call(tokenPaper, TokenCreateRes.class);
            this.auth = true;
        }
    }

    public synchronized void createAccessKey() throws IOException, InterruptedException, FailedResponseException {
        if (!isAuth() || this.approvalKeyRes == null) {
            RequestPaper accessKeyPaper = this.createWebsocketAccessKey();
            this.approvalKeyRes = this.httpRequester.call(accessKeyPaper, ApprovalKeyRes.class);
            this.auth = true;
        }
    }

    private RequestPaper createTokenPaper() throws JsonProcessingException {
        RequestPaper requestPaper = new RequestPaper();
        return requestPaper
                .setMethod("POST")
                .setUri("https://openapi.koreainvestment.com:9443/oauth2/tokenP")
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .putHeader("gt_uid", KiRequestHelper.makeGtUid())
                .setBody(HttpRequestHelper.objectToString(this.tokenCreateReq));
    }

    private RequestPaper createWebsocketAccessKey() throws JsonProcessingException {
        RequestPaper requestPaper = new RequestPaper();
        return requestPaper
                .setMethod("POST")
                .setUri("https://openapi.koreainvestment.com:9443/oauth2/Approval")
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .setBody(HttpRequestHelper.objectToString(this.approvalKeyReq));
    }

    public synchronized void removeToken() throws IOException, InterruptedException, FailedResponseException {
        if (!this.auth) {
            return;
        }

        this.tokenRemoveReq.setToken(this.tokenCreateRes.getAccessToken());
        RequestPaper tokenRemovePaper = this.createTokenPaper();
        TokenRemoveRes tokenRemoveRes = this.httpRequester.call(tokenRemovePaper, TokenRemoveRes.class);
        if (tokenRemoveRes.getCode() == 200) {
            this.auth = false;
        }
    }

    private RequestPaper createRomoveTokenPaper() throws JsonProcessingException {
        RequestPaper requestPaper = new RequestPaper();
        return requestPaper
                .setMethod("POST")
                .setUri("https://openapi.koreainvestment.com:9443/oauth2/revokeP")
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .putHeader("gt_uid", KiRequestHelper.makeGtUid())
                .setBody(HttpRequestHelper.objectToString(this.tokenRemoveReq));
    }

}
