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
public class EncryptDataLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private String encryptType;
    private String planeText;
    private String secretKey;
    private String encodingText;

    @Builder
    public EncryptDataLog(String encryptType, String planeText, String secretKey, String encodingText) {
        this.encryptType = encryptType;
        this.planeText = planeText;
        this.secretKey = secretKey;
        this.encodingText = encodingText;
    }
}
