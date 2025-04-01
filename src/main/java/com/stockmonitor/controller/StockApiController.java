package com.stockmonitor.controller;

import com.stockmonitor.model.response.StockRankingResponse;
import com.stockmonitor.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 股票API控制器
 */
@RestController
@RequestMapping("/api/stocks")
public class StockApiController {

    private static final Logger logger = LoggerFactory.getLogger(StockApiController.class);

    @Autowired
    private HttpUtil httpUtil;

    /**
     * 获取股票排行榜数据
     */
    @GetMapping("/ranking")
    public ResponseEntity<StockRankingResponse> getStockRanking(
            @RequestParam(defaultValue = "1") String page,
            @RequestParam(defaultValue = "10") String limit,
            @RequestParam(defaultValue = "hs_a") String market,
            @RequestParam(defaultValue = "changeRate") String sort,
            @RequestParam(defaultValue = "0") String asc) {

        logger.info("获取股票排行榜数据，参数：page={}, limit={}, market={}, sort={}, asc={}",
                page, limit, market, sort, asc);

        try {
            StockRankingResponse response = httpUtil.getStockRanking(asc, limit, market, page, sort);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("获取股票排行榜数据失败", e);

            // 创建一个错误响应
            StockRankingResponse errorResponse = new StockRankingResponse();
            errorResponse.setSuccess(false);
            errorResponse.setCode(500);
            errorResponse.setMsg("获取股票排行榜数据失败：" + e.getMessage());

            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}