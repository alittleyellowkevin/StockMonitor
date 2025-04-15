package com.stockmonitor.model.mysql;

import lombok.Data;

@Data
public class StockRanking {
    private String volume;
    private String symbol;
    private String high;
    private Long update_time;
    private String low;
    private String price;
    private String change;
    private String name;
    private String preclose;
    private String changeRate;
    private String value;
    private String open;
}