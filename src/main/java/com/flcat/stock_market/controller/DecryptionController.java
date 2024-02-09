package com.flcat.stock_market.controller;

import com.flcat.stock_market.code.ResponseCode;
import com.flcat.stock_market.service.DecryptService;
import com.flcat.stock_market.type.AesType;
import com.flcat.stock_market.util.ApiResponse;
import com.flcat.stock_market.vo.common.ResponseVO;
import com.flcat.stock_market.vo.req.DecryptAesReqVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dec")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DecryptionController {

    private final DecryptService decryptService;

    @PostMapping("/aes")
    public ResponseVO<String> decryptAes(@RequestBody @Valid DecryptAesReqVO param) {
        if (param.getType().equals(AesType.EMPTY)) {
            return ApiResponse.fail(ResponseCode.INVALID_MODE_ERROR);
        }

        return ApiResponse.of(decryptService.decodeAes(param));
    }
}
