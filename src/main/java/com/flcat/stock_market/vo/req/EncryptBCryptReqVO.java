package com.flcat.stock_market.vo.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EncryptBCryptReqVO {

    @NotBlank(message = "planeText 값을 필수입니다.")
    private String planeText;

    @Min(value = 4, message = "strength 값은 4 이상이어야 합니다.")
    @Max(value = 12, message = "strength 값은 12 이하여야 합니다.")
    private int strength;
}
