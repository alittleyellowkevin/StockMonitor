package com.stockmonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/health")
public class HealthCheckController {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private XxlJobSpringExecutor xxlJobExecutor;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String HEALTH_CHECK_TOPIC = "health_check_topic";

    @GetMapping("/check")
    public Map<String, Object> healthCheck() {
        Map<String, Object> result = new HashMap<>();
        Map<String, Boolean> components = new HashMap<>();
        Map<String, String> details = new HashMap<>();

        // 检查MySQL连接
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            components.put("mysql", true);
            details.put("mysql", "连接正常");
        } catch (Exception e) {
            logger.error("MySQL连接检查失败", e);
            components.put("mysql", false);
            details.put("mysql", "连接失败: " + e.getMessage());
        }

        // 检查Redis连接
        try {
            redisTemplate.opsForValue().get("health_check_test");
            components.put("redis", true);
            details.put("redis", "连接正常");
        } catch (Exception e) {
            logger.error("Redis连接检查失败", e);
            components.put("redis", false);
            details.put("redis", "连接失败: " + e.getMessage());
        }

        // 检查XXL-Job连接
        try {
            boolean xxlJobRunning = xxlJobExecutor != null;
            components.put("xxl-job", xxlJobRunning);
            details.put("xxl-job", xxlJobRunning ? "连接正常" : "未启动");
        } catch (Exception e) {
            logger.error("XXL-Job连接检查失败", e);
            components.put("xxl-job", false);
            details.put("xxl-job", "连接失败: " + e.getMessage());
        }

        // 检查Kafka连接
        try {
            ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(HEALTH_CHECK_TOPIC, "health_check",
                    "test");
            future.get(5, TimeUnit.SECONDS); // 等待最多5秒
            components.put("kafka", true);
            details.put("kafka", "连接正常");
        } catch (Exception e) {
            logger.error("Kafka连接检查失败", e);
            components.put("kafka", false);
            details.put("kafka", "连接失败: " + e.getMessage());
        }

        // 汇总结果
        result.put("status", components.containsValue(false) ? "unhealthy" : "healthy");
        result.put("components", components);
        result.put("details", details);
        result.put("timestamp", System.currentTimeMillis());

        return result;
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}