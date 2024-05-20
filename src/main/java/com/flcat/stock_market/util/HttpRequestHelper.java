package com.flcat.stock_market.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpRequestHelper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public HttpRequestHelper() {
        throw new IllegalStateException("Utility Class");
    }

    public static String objectToString(Object object) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(object);
    }

    public static <T> T stringToObject(String jsonStr, Class<T> class1) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(jsonStr, class1);
    }
}
