package com.flcat.stock_market.service;

import com.flcat.stock_market.entity.DecryptDataLog;
import com.flcat.stock_market.repository.DecryptDataLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DecryptDataLogService {

    private final DecryptDataLogRepository repository;

    public Long insertDecryptData(DecryptDataLog decryptDataLog) {
        return repository.save(decryptDataLog).getId();
    }



}
