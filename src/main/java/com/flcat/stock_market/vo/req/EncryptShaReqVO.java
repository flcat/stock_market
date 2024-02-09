package com.flcat.stock_market.vo.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EncryptShaReqVO {

    @NotBlank(message = "planeText 값은 필수입니다.")
    private String planeText;

    @NotNull(message = "type 값은 필수입니다.")
    private String type;
}