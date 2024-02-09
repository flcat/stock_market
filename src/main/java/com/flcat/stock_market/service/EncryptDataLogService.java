package com.flcat.stock_market.service;

import com.flcat.stock_market.entity.EncryptDataLog;
import com.flcat.stock_market.repository.EncryptDataLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EncryptDataLogService {

    private final EncryptDataLogRepository repository;

    public Long insertEncryptData(EncryptDataLog encryptDataLog) {
        return repository.save(encryptDataLog).getId();
    }



}
