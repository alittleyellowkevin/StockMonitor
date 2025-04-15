package com.stockmonitor.jobhandler.stock;

import com.stockmonitor.service.stock.StockPullService;
import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class StockRankingPullJobHandler {

    private static final Logger logger = LoggerFactory.getLogger(StockRankingPullJobHandler.class);

    @Autowired
    private ConsumerFactory<String, String> manualConsumerFactory;
    @Autowired
    private StockPullService stockPullService;

    private static final String STOCK_RANKING_TOPIC = "stock_ranking";

    @XxlJob(value = "stockRankingJobHandler")
    public void stockRankingJobHandler() {
        logger.info("StockRankingJobHandler start");
        // 创建消费者
        Consumer<String, String> consumer = manualConsumerFactory.createConsumer();
        try {
            // 订阅主题
            consumer.subscribe(Collections.singletonList(STOCK_RANKING_TOPIC));
            // 拉取数据
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100000));
            logger.info("StockPullJobHandler poll records: {}", records.count());
            consumer.commitAsync();
            List<JSONObject> data = new ArrayList<>();
            for (ConsumerRecord<String, String> record : records) {
                logger.info("StockPullJobHandler poll record: {}", record.value());
                data.add(JSONObject.parseObject(record.value()));
            }
            if (!data.isEmpty()) {
                try {
                    for (JSONObject item : data) {
                        stockPullService.saveStockRanking(
                                item.getString("asc"),
                                item.getString("limit"),
                                item.getString("market"),
                                item.getString("page"),
                                item.getString("sort"));
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
            logger.info("StockPullJobHandler end");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            consumer.close();
        }
    }
}
