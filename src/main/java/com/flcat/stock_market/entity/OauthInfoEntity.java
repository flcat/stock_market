package com.flcat.stock_market.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class OauthInfoEntity {


    private String grantType;
    private String appKey;
    private String appSecret;

    public OauthInfoEntity(String grantType, String appKey, String appSecret) {
        this.grantType = grantType;
        this.appKey = appKey;
        this.appSecret = appSecret;
    }
}
