package com.stockmonitor.model.response;

import lombok.Data;
import java.util.List;

import com.stockmonitor.model.mysql.StockRanking;

@Data
public class StockRankingResponse {
    private StockRankingDataWrapper data;

    @Data
    public static class StockRankingDataWrapper {
        private List<StockRanking> list;
    }
}