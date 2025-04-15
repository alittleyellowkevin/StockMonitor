package com.stockmonitor.jobhandler.stock;

import com.alibaba.fastjson.JSONObject;
import com.stockmonitor.service.stock.StockPullService;
import com.xxl.job.core.handler.IJobHandler;
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
public class StockPriceJobHandler extends IJobHandler {

    private static final Logger logger = LoggerFactory.getLogger(StockPriceJobHandler.class);

    @Autowired
    private ConsumerFactory<String, String> manualConsumerFactory;

    @Autowired
    private StockPullService stockPullService;

    private static final String STOCK_PRICE_TOPIC = "stock_price";

    @XxlJob("StockPriceJobHandler")
    @Override
    public void execute() throws Exception {
        logger.info("StockPriceJobHandler start");
        // 创建消费者
        Consumer<String, String> consumer = manualConsumerFactory.createConsumer();
        try {
            // 订阅主题
            consumer.subscribe(Collections.singletonList(STOCK_PRICE_TOPIC));
            // 拉取数据
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            logger.info("StockPriceJobHandler poll records: {}", records.count());

            List<JSONObject> data = new ArrayList<>();
            for (ConsumerRecord<String, String> record : records) {
                data.add(JSONObject.parseObject(record.value()));
            }
            if (!data.isEmpty()) {
                try {
                    for (JSONObject item : data) {
                        stockPullService.saveStockPrice(item.getString("symbol"));
                    }
                    consumer.commitAsync();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
            logger.info("StockPriceJobHandler end");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            consumer.close();
        }
    }
}