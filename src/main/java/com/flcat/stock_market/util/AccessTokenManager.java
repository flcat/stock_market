package com.flcat.stock_market.util;

import com.flcat.stock_market.config.KisConfig;
import com.flcat.stock_market.entity.OauthInfoEntity;
import com.flcat.stock_market.entity.TokenInfoEntity;
import org.springframework.stereotype.Component;

//@Component
public class AccessTokenManager {
//
//    private final WebClient webClient;
//    private String ACCESS_TOKEN;
//    private long lastAuthTime = 0;
//
//    public AccessTokenManager(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.baseUrl(KisConfig.REST_BASE_URL).build();
//    }
//
//    public String getAccessToken() {
//        if (ACCESS_TOKEN == null) {
//            ACCESS_TOKEN = generateAccessToken();
//            System.out.println("generate ACCESS_TOKEN : " + ACCESS_TOKEN);
//        }
//        return ACCESS_TOKEN;
//    }
//
//    public String generateAccessToken() {
//        String url = KisConfig.REST_BASE_URL + "/oauth2/token";
//        OauthInfoEntity bodyInfo = new OauthInfoEntity();
//        bodyInfo.setGrantType("client_credentials");
//        bodyInfo.setAppKey(KisConfig.APPKEY);
//        bodyInfo.setAppSecret(KisConfig.APPSECRET);
//
//        Mono<TokenInfoEntity> mono = webClient.post()
//                .uri(url)
//                .header("content-type", "application/json; utf-8")
//                .bodyValue(bodyInfo)
//                .retrieve()
//                .bodyToMono(TokenInfoEntity.class);
//
//        TokenInfoEntity tokenInfoEntity = mono.block();
//        if (tokenInfoEntity == null) {
//            throw new RuntimeException("엑세스 토큰을 가져올 수 없습니다.");
//        }
//
//        ACCESS_TOKEN = tokenInfoEntity.getAccessToken();
//
//        return ACCESS_TOKEN;
//    }
}
