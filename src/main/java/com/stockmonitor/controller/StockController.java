package com.stockmonitor.controller;

import com.stockmonitor.model.Stock;
import com.stockmonitor.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/{code}")
    public Stock getStock(@PathVariable String code) {
        return stockService.getStockByCode(code);
    }

    @GetMapping
    public List<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }

    @PostMapping("/update")
    public void updateStock(@RequestBody Stock stock) {
        stockService.updateStockInfo(stock);
    }
} 