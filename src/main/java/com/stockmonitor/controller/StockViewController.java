package com.stockmonitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 股票视图控制器
 */
@Controller
@RequestMapping("/stocks")
public class StockViewController {

    private static final Logger logger = LoggerFactory.getLogger(StockViewController.class);

    /**
     * 股票监控主页 - 包含热度排行、提醒设置和基本信息查询
     */
    @GetMapping({ "", "/", "/index", "/monitor" })
    public String monitorPage(Model model) {
        logger.info("访问股票监控主页");
        return "stock-heat"; // 复用原heat页面作为主页
    }

    /**
     * 股票热度排行榜页面（已合并到主页，保留此方法用于兼容性）
     */
    @GetMapping("/heat")
    public String heatPage(Model model) {
        logger.info("访问股票热度排行榜页面");
        return "stock-heat";
    }

    /**
     * 股票详情页面
     */
    @GetMapping("/detail/{symbol}")
    public String detailPage(@PathVariable String symbol, Model model) {
        logger.info("访问股票详情页面，股票代码：{}", symbol);
        model.addAttribute("symbol", symbol);
        return "stock-detail"; // 这个视图尚未创建，后续可以添加
    }
}