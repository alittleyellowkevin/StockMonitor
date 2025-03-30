package com.stockmonitor.service.impl;

import com.stockmonitor.dao.StockDao;
import com.stockmonitor.model.Stock;
import com.stockmonitor.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockDao stockDao;

    @Override
    public Stock getStockByCode(String code) {
        return stockDao.findByCode(code);
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockDao.findAll();
    }

    @Override
    public void updateStockInfo(Stock stock) {
        stockDao.updateStock(stock);
    }
} 