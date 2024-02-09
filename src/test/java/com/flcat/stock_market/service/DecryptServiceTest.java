package com.flcat.stock_market.service;
import com.flcat.stock_market.type.AesType;
import com.flcat.stock_market.vo.req.DecryptAesReqVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DecryptServiceTest {
    private final String PLANE_TEXT = "안녕하세요";
    private final String SECRET_KEY = "flcat!@#$%^&*";
    @Autowired
    private EncryptService encryptService;
    @Autowired
    private DecryptService decryptService;

    /* SECRET_KEY가 변경되면 테스트가 실패할 수 있음 */
    @Test
    public void decryptAesCase01() {
        // given
        final String encodingText = "Y+Qx8ykqfFrRlGzkRSfSDhA9TLrxV7gnBs2dgTNni/HXfuDgwFT5PYNbVUB939lDMjGrnXqscdsiyvsPjynOHg==";

        DecryptAesReqVO decryptParam = DecryptAesReqVO
                .builder()
                .encodingText(encodingText)
                .secretKey(this.SECRET_KEY)
                .type(AesType.CBC)
                .build();

        // when
        String decodingText = decryptService.decodeAes(decryptParam);

        // then
        assertNotNull(decodingText);
        assertNotEquals(encodingText, decodingText);
        assertEquals(this.PLANE_TEXT, decodingText);
    }
}