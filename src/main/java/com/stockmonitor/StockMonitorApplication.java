package com.stockmonitor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.stockmonitor.dao")
public class StockMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockMonitorApplication.class, args);
    }
}