// package com.stockmonitor.jobhandler;

// import com.stockmonitor.model.Stock;
// import com.stockmonitor.service.StockService;
// import com.stockmonitor.util.HttpUtil;
// import com.xxl.job.core.context.XxlJobHelper;
// import com.xxl.job.core.handler.annotation.XxlJob;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;

// import java.util.Arrays;
// import java.util.List;
// import java.util.Map;
// import java.util.concurrent.ConcurrentHashMap;

// /**
//  * 股票数据定时任务处理器
//  */
// @Component
// public class StockDataJobHandler {
//     private static final Logger logger = LoggerFactory.getLogger(StockDataJobHandler.class);

//     @Autowired
//     private StockService stockService;

//     /**
//      * 定时拉取股票实时数据
//      * 每5分钟执行一次
//      */
//     @XxlJob("stockDataPullHandler")
//     public void stockDataPullHandler() {
//         try {
//             // 获取需要监控的股票列表
//             List<Stock> stocks = stockService.getAllStocks();
//             if (stocks == null || stocks.isEmpty()) {
//                 logger.warn("没有需要监控的股票");
//                 return;
//             }
//             // 构建请求参数
//             Map<String, String> params = new ConcurrentHashMap<>();
//             StringBuilder stockCodes = new StringBuilder();
//             for (Stock stock : stocks) {
//                 stockCodes.append(stock.getCode()).append(",");
//             }
//             params.put("codes", stockCodes.substring(0, stockCodes.length() - 1));

//             // 调用股票API获取实时数据
//             String url = "http://api.example.com/stocks/realtime";
//             Stock[] realtimeStocks = HttpUtil.get(url, params, Stock[].class);

//             if (realtimeStocks != null && realtimeStocks.length > 0) {
//                 // 更新数据库中的股票数据
//                 for (Stock stock : realtimeStocks) {
//                     stockService.updateStockInfo(stock);
//                 }
//                 logger.info("成功更新{}支股票数据", realtimeStocks.length);
//                 XxlJobHelper.handleSuccess("成功更新" + realtimeStocks.length + "支股票数据");
//             } else {
//                 logger.warn("未获取到股票数据");
//                 XxlJobHelper.handleFail("未获取到股票数据");
//             }
//         } catch (Exception e) {
//             logger.error("股票数据拉取任务执行失败", e);
//             XxlJobHelper.handleFail("股票数据拉取任务执行失败：" + e.getMessage());
//         }
//     }

//     /**
//      * 定时计算股票技术指标
//      * 每天收盘后执行一次
//      */
//     @XxlJob("stockIndicatorCalcHandler")
//     public void stockIndicatorCalcHandler() {
//         logger.info("开始执行股票技术指标计算任务");
//         try {
//             // 获取所有股票
//             List<Stock> stocks = stockService.getAllStocks();
//             if (stocks == null || stocks.isEmpty()) {
//                 logger.warn("没有需要计算技术指标的股票");
//                 return;
//             }

//             // 调用技术指标计算API
//             String url = "http://api.example.com/stocks/indicators";
//             Map<String, String> params = new ConcurrentHashMap<>();
//             StringBuilder stockCodes = new StringBuilder();
//             for (Stock stock : stocks) {
//                 stockCodes.append(stock.getCode()).append(",");
//             }
//             params.put("codes", stockCodes.substring(0, stockCodes.length() - 1));

//             Stock[] stocksWithIndicators = HttpUtil.get(url, params, Stock[].class);

//             if (stocksWithIndicators != null && stocksWithIndicators.length > 0) {
//                 // 更新数据库中的技术指标数据
//                 for (Stock stock : stocksWithIndicators) {
//                     stockService.updateStockInfo(stock);
//                 }
//                 logger.info("成功更新{}支股票技术指标", stocksWithIndicators.length);
//                 XxlJobHelper.handleSuccess("成功更新" + stocksWithIndicators.length + "支股票技术指标");
//             } else {
//                 logger.warn("未获取到股票技术指标数据");
//                 XxlJobHelper.handleFail("未获取到股票技术指标数据");
//             }
//         } catch (Exception e) {
//             logger.error("股票技术指标计算任务执行失败", e);
//             XxlJobHelper.handleFail("股票技术指标计算任务执行失败：" + e.getMessage());
//         }
//     }

//     /**
//      * 定时检查股票预警条件
//      * 每1分钟执行一次
//      */
//     @XxlJob("stockAlertCheckHandler")
//     public void stockAlertCheckHandler() {
//         logger.info("开始执行股票预警检查任务");
//         try {
//             // 获取所有股票
//             List<Stock> stocks = stockService.getAllStocks();
//             if (stocks == null || stocks.isEmpty()) {
//                 logger.warn("没有需要检查预警的股票");
//                 return;
//             }

//             // 检查每支股票的预警条件
//             int alertCount = 0;
//             for (Stock stock : stocks) {
//                 // 检查价格预警
//                 if (stock.getPrice() >= stock.getHigh() ||
//                         stock.getPrice() <= stock.getLow()) {
//                     alertCount++;
//                     logger.info("股票{}触发价格预警，当前价格：{}", stock.getCode(), stock.getPrice());
//                     // TODO: 发送预警通知
//                 }

//                 // 检查技术指标预警
//                 if (stock.getMacd() > 0.8 || stock.getMacd() < -0.8) {
//                     alertCount++;
//                     logger.info("股票{}触发MACD预警，当前MACD：{}", stock.getCode(), stock.getMacd());
//                     // TODO: 发送预警通知
//                 }

//                 if (stock.getKdj() > 80 || stock.getKdj() < 20) {
//                     alertCount++;
//                     logger.info("股票{}触发KDJ预警，当前KDJ：{}", stock.getCode(), stock.getKdj());
//                     // TODO: 发送预警通知
//                 }

//                 if (stock.getRsi() > 70 || stock.getRsi() < 30) {
//                     alertCount++;
//                     logger.info("股票{}触发RSI预警，当前RSI：{}", stock.getCode(), stock.getRsi());
//                     // TODO: 发送预警通知
//                 }
//             }

//             logger.info("预警检查完成，触发{}个预警", alertCount);
//             XxlJobHelper.handleSuccess("预警检查完成，触发" + alertCount + "个预警");
//         } catch (Exception e) {
//             logger.error("股票预警检查任务执行失败", e);
//             XxlJobHelper.handleFail("股票预警检查任务执行失败：" + e.getMessage());
//         }
//     }
// }