package com.flcat.stock_market.config;

public enum ColorConfig {
    GREEN("#36a64f"),
    RED("#ff0000"),
    BLUE("#0000ff"),
    YELLOW("#ffff00"),
    BLACK("#000000"),
    WHITE("#ffffff");

    private final String code;

    ColorConfig(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
