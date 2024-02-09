package com.flcat.stock_market.code;

public enum ResponseCode {
    SUCCESS("200", "")
    , KEY_LENGTH_ERROR("501", "입력하신 비밀키의 길이를 확인하시기 바랍니다.")
    , ENCRYPTION_ERROR("502", "암호화 중 오류가 발생했습니다.")
    , DECRYPTION_ERROR("503", "복호화 중 오류가 발생했습니다.")
    , BAD_KEY_ERROR("504", "복호화 중 오류가 발생했습니다. 암호문, 비밀키, 암호화방식을 확인해주세요.")
    , INVALID_MODE_ERROR("505", "유효하지 않은 암호화방식 입니다.")
    , NOT_VALID_ERROR("506", "데이터를 확인해주세요.")
    , INVALID_STRENGTH_ERROR("507", "반복 횟수는 필수값 입니다.")
    , SERVER_ERROR("555", "알 수 없는 오류가 발생했습니다. 관리자에게 문의하세요.")
    ;
    private String code;
    private String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return message;
    }
}
