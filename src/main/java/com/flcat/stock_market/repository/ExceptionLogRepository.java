package com.flcat.stock_market.repository;


import com.flcat.stock_market.entity.ExceptionLog;
import org.springframework.data.repository.CrudRepository;

public interface ExceptionLogRepository extends CrudRepository<ExceptionLog, Long> {
}
