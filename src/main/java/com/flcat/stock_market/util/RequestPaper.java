package com.flcat.stock_market.util;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

public class RequestPaper {

    private String method;
    private String uri;
    private final Map<String, String> header;
    private final Map<String, String> queryParam;
    private HttpRequest.BodyPublisher body;
    private String bodyStr;

    public RequestPaper(){
        this.header = new HashMap<>();
        this.queryParam = new HashMap<>();
    }

    public String getUri() {
        return uri;
    }

    public RequestPaper setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public RequestPaper putHeader(String key, String value) {
        this.header.put(key, value);
        return this;
    }

    public Map<String, String> getQueryParam() {
        return queryParam;
    }

    public RequestPaper putQueryParam(String key, String value) {
        this.queryParam.put(key, value);
        return this;
    }

    public RequestPaper setBody(Object object) throws JsonProcessingException {
        String jsonStr;
        if (object instanceof String) {
            jsonStr = (String) object;
        } else {
            jsonStr = HttpRequestHelper.objectToString(object);
        }
        this.bodyStr = jsonStr;
        this.body = HttpRequest.BodyPublishers.ofString(jsonStr);

        return this;
    }

    public HttpRequest.BodyPublisher getBody() {
        return body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.lineSeparator());
        this.mapToString("header", this.header, sb);
        this.mapToString("queryParam", this.queryParam, sb);
        sb.append("bodySTR : ").append(this.bodyStr);
        return sb.toString();
    }

    public RequestPaper setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getMethod() {
        return method;
    }

    private void mapToString(String name, Map<String, String> map, StringBuilder sb) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(name)
                    .append(" [")
                    .append(entry.getKey())
                    .append("] :")
                    .append(entry.getValue())
                    .append(System.lineSeparator());
        }
    }


}
