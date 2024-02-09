package com.flcat.stock_market.type;

public enum AesType {
    EMPTY("EMPTY",""),
    CBC("CBC", "AES/CBC/PKCS5Padding"),
    ECB("ECB", "AES/ECB/PKCS5Padding");

    private String type;
    private String value;

    AesType(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
