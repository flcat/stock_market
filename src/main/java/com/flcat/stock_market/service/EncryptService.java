package com.flcat.stock_market.service;

import com.flcat.stock_market.entity.EncryptDataLog;
import com.flcat.stock_market.exception.EncryptException;
import com.flcat.stock_market.type.AesType;
import com.flcat.stock_market.vo.req.EncryptAesReqVO;
import com.flcat.stock_market.vo.req.EncryptBCryptReqVO;
import com.flcat.stock_market.vo.req.EncryptShaReqVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
@Transactional
public class EncryptService {
    private final EncryptDataLogService encryptDataLogService;
    private final String BCRYPT_TYPE = "BCrypt";

    public String encodeAes(EncryptAesReqVO param) {
        String planeText = param.getPlaneText();
        String secretKey = param.getSecretKey();
        String encodingText;
        try {
            Key key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance(param.getType().getValue());

            //암호화 타입에 맞게 분리
            if (AesType.CBC.equals(param.getType())) {
                String iv = secretKey.substring(0, 16);
                cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
            } else if (AesType.ECB.equals(param.getType())) {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            }

            byte[] encrypted = cipher.doFinal(planeText.getBytes(StandardCharsets.UTF_8));
            encodingText = DatatypeConverter.printBase64Binary(encrypted);
        } catch (GeneralSecurityException e) {
            throw new EncryptException(e);
        }
        EncryptDataLog encryptDataLog = EncryptDataLog.builder()
                .encryptType(param.getType().getValue())
                .planeText(param.getPlaneText())
                .secretKey(param.getSecretKey())
                .encodingText(encodingText)
                .build();
        this.insertSuccessAes(encryptDataLog);
        return encodingText;
    }

    public String encodeSha(EncryptShaReqVO param) {
        String planeText = param.getPlaneText();
        String encodingText = "";
        String shaType = param.getType();
        try {
            MessageDigest md = MessageDigest.getInstance(shaType);
            md.update(planeText.getBytes(StandardCharsets.UTF_8));
            encodingText = DatatypeConverter.printBase64Binary(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException(e);
        }
        EncryptDataLog encryptDataLog = EncryptDataLog.builder()
                .encryptType(param.getType())
                .planeText(param.getPlaneText())
                .encodingText(encodingText)
                .build();
        this.insertSuccessAes(encryptDataLog);
        return encodingText;
    }

    public String encodeBcrypt(EncryptBCryptReqVO param) {

        String encodingText = new BCryptPasswordEncoder(param.getStrength()).encode(param.getPlaneText());

        EncryptDataLog encryptDataLog = EncryptDataLog.builder()
                .encryptType(BCRYPT_TYPE)
                .planeText(param.getPlaneText())
                .encodingText(encodingText)
                .build();
        this.insertSuccessAes(encryptDataLog);
        return encodingText;
    }

    private Long insertSuccessAes(EncryptDataLog encryptDataLog) {
        return encryptDataLogService.insertEncryptData(encryptDataLog);
    }
}
