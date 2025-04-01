package com.stockmonitor.service;

import com.stockmonitor.model.Stock;
import java.util.List;

public interface StockService {
    Stock getStockByCode(String code);

    List<Stock> getAllStocks();

    void updateStockInfo(Stock stock);
}