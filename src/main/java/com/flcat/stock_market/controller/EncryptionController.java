package com.flcat.stock_market.controller;

import com.flcat.stock_market.code.ResponseCode;
import com.flcat.stock_market.service.EncryptService;
import com.flcat.stock_market.type.AesType;
import com.flcat.stock_market.util.ApiResponse;
import com.flcat.stock_market.vo.common.ResponseVO;
import com.flcat.stock_market.vo.req.EncryptAesReqVO;
import com.flcat.stock_market.vo.req.EncryptBCryptReqVO;
import com.flcat.stock_market.vo.req.EncryptShaReqVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/enc")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EncryptionController {

    private final EncryptService encryptService;

    @PostMapping("/aes")
    public ResponseVO<String> encryptAes(@RequestBody @Valid EncryptAesReqVO param) {
        if (param.getType().equals(AesType.EMPTY)) {
            return ApiResponse.fail(ResponseCode.INVALID_MODE_ERROR);
        }
        return ApiResponse.of(encryptService.encodeAes(param));
    }

    @PostMapping("/sha")
    public ResponseVO<String> encryptSha(@RequestBody @Valid EncryptShaReqVO param) {
        String type = param.getType();
        if (type.equals("")) {
            return ApiResponse.fail(ResponseCode.INVALID_MODE_ERROR);
        }
        return ApiResponse.of(encryptService.encodeSha(param));
    }

    @PostMapping("/bcrypt")
    public ResponseVO<String> encryptBCrypt(@RequestBody @Valid EncryptBCryptReqVO param) {
        return ApiResponse.of(encryptService.encodeBcrypt(param));
    }
}
