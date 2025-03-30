package com.stockmonitor.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Stock {
    private Long id;
    private String code; // 股票代码
    private String name; // 股票名称
    private BigDecimal price; // 当前价格
    private BigDecimal change; // 涨跌幅
    private Long volume; // 成交量
    private BigDecimal open; // 开盘价
    private BigDecimal high; // 最高价
    private BigDecimal low; // 最低价
    private BigDecimal close; // 收盘价
    private LocalDateTime updateTime; // 更新时间

    // 技术指标
    private BigDecimal macd; // MACD指标
    private BigDecimal kdj; // KDJ指标
    private BigDecimal rsi; // RSI指标
}