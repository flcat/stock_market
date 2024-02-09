package com.flcat.stock_market.vo.req;

import com.flcat.stock_market.type.AesType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Arrays;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DecryptAesReqVO {

    @NotBlank(message = "encodingText 값은 필수입니다.")
    private String encodingText;

    @NotBlank(message = "secretKey 값은 필수입니다.")
    private String secretKey;
    @NotNull(message = "type 값은 필수입니다.")
    private AesType type;

    public void setType(String type) {
        this.type = Arrays.stream(AesType.values())
                .filter(t -> t.getType().equals(type))
                .findFirst()
                .orElse(AesType.EMPTY);
    }
}