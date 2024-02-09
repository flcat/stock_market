package com.flcat.stock_market.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class DecryptDataLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private String decryptType;
    private String planeText;
    private String secretKey;

    private String encodingText;
    private String decodingText;

    @Builder
    public DecryptDataLog(String decryptType, String planeText, String secretKey, String encodingText, String decodingText) {
        this.decryptType = decryptType;
        this.planeText = planeText;
        this.secretKey = secretKey;
        this.encodingText = encodingText;
        this.decodingText = decodingText;
    }
}
