package com.flcat.stock_market.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flcat.stock_market.service.EncryptService;
import com.flcat.stock_market.type.AesType;
import com.flcat.stock_market.vo.req.EncryptAesReqVO;
import com.flcat.stock_market.vo.req.EncryptShaReqVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class EncryptionControllerTest {
    private final String PLANE_TEXT = "안녕하세요";
    private final String SECRET_KEY = "flcat!@#$%^&*";

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private EncryptService service;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    public void encryptAesCase01() throws Exception {
        // given
        EncryptAesReqVO param = EncryptAesReqVO
                .builder()
                .planeText(this.PLANE_TEXT)
                .secretKey(this.SECRET_KEY)
                .type(AesType.CBC)
                .build();
        final String encodingText = "Y+Qx8ykqfFrRlGzkRSfSDhA9TLrxV7gnBs2dgTNni/HXfuDgwFT5PYNbVUB939lDMjGrnXqscdsiyvsPjynOHg==";

        // when
        when(service.encodeAes(any(EncryptAesReqVO.class))).thenReturn(encodingText);

        // then
        this.mockMvc
                .perform(post("/api/enc/aes")
                        .content(objectMapper.writeValueAsString(param))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(encodingText)))
        ;
    }

    @Test
    public void encryptSha512Case01() throws Exception {
        // given
        EncryptShaReqVO param = EncryptShaReqVO
                .builder()
                .planeText(this.PLANE_TEXT)
                .type("SHA-512")
                .build();
        final String encodingText = "e72e84f429955c237e7ec4b7ad071ee1eee33dd27c2854448693b70bbc623ead426c91d922b7ebfa6ffa0d29be907d42358c35809389d3e1c95a3c7ff0ae1643";

        // when
        when(service.encodeSha(any(EncryptShaReqVO.class))).thenReturn(encodingText);

        // then
        this.mockMvc
                .perform(post("/api/enc/sha")
                        .content(objectMapper.writeValueAsString(param))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(encodingText)))
        ;
    }
}