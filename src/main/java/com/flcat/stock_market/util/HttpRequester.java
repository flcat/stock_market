package com.flcat.stock_market.util;

import com.flcat.stock_market.exception.FailedResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

@Slf4j
@Service
public class HttpRequester {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public <T> T call(RequestPaper paper, Class<T> responseClass) throws IOException, InterruptedException, FailedResponseException {

        this.requestLog(paper);

        HttpRequest request = this.createHttpRequest(paper);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        this.responseLog(response);

        if (response.statusCode() != 200) {
            throw new FailedResponseException(response.statusCode() + " " + response.body());
        }

        return HttpRequestHelper.stringToObject(response.body(), responseClass);

    }

    private HttpRequest createHttpRequest(RequestPaper paper) {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        switch (paper.getMethod()) {
            case "POST":
                builder.POST(paper.getBody()).uri(URI.create(paper.getUri()));
                break;
            case "GET":
                builder.GET().uri(this.createURI(paper));
                break;
            default:
                throw new UnsupportedOperationException(paper.getMethod() + " Method is not Supported");
        }
        this.setHeaders(builder, paper);
        return builder.build();
    }

    private void setHeaders(HttpRequest.Builder builder, RequestPaper paper) {
        Map<String, String> headerMap = paper.getHeader();

        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            builder.setHeader(entry.getKey(), entry.getValue());
        }
    }

    private URI createURI(RequestPaper paper) {
        UriBuilder uriBuilder = UriBuilder.fromUri(paper.getUri());
        Map<String, String> queryParamMap = paper.getQueryParam();

        for (Map.Entry<String, String> entry : queryParamMap.entrySet()) {
            uriBuilder.queryParam(entry.getKey(), entry.getValue());
        }

        return uriBuilder.build();
    }

    private void requestLog(RequestPaper paper) {
        if (log.isInfoEnabled()) {
            log.info("===== Request Paper Info Start =======");
            log.info(paper.toString());
            log.info("===== Request Paper Info End =======");
        }
    }

    private void responseLog(HttpResponse<String> response) {
        if (log.isInfoEnabled()) {
            log.info("Http Status Code : " + response.statusCode());
            HttpHeaders headers = response.headers();
            headers.map().forEach((k, v) -> log.info(k + ":" + v));
            log.info("===== Response Body Message Start =======");
            log.info(response.body());
            log.info("===== Response Body Message End =======");
        }
    }
}
