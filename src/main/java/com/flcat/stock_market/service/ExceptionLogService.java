package com.flcat.stock_market.service;

import com.flcat.stock_market.entity.EncryptDataLog;
import com.flcat.stock_market.entity.ExceptionLog;
import com.flcat.stock_market.repository.EncryptDataLogRepository;
import com.flcat.stock_market.repository.ExceptionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ExceptionLogService {

    private final ExceptionLogRepository repository;

    public Long insertLogMessage(String message) {
        ExceptionLog exceptionInfo = ExceptionLog.builder().message(message).build();
        return repository.save(exceptionInfo).getId();
    }



}
