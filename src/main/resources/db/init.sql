-- 创建数据库
CREATE DATABASE IF NOT EXISTS stock_monitor DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE stock_monitor;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `email` VARCHAR(100) COMMENT '邮箱',
    `phone` VARCHAR(20) COMMENT '手机号',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 股票表
CREATE TABLE IF NOT EXISTS `stock` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `code` VARCHAR(10) NOT NULL COMMENT '股票代码',
    `name` VARCHAR(50) NOT NULL COMMENT '股票名称',
    `price` DECIMAL(10,2) NOT NULL COMMENT '当前价格',
    `change` DECIMAL(5,2) NOT NULL COMMENT '涨跌幅',
    `volume` BIGINT NOT NULL COMMENT '成交量',
    `open` DECIMAL(10,2) NOT NULL COMMENT '开盘价',
    `high` DECIMAL(10,2) NOT NULL COMMENT '最高价',
    `low` DECIMAL(10,2) NOT NULL COMMENT '最低价',
    `close` DECIMAL(10,2) NOT NULL COMMENT '收盘价',
    `macd` DECIMAL(10,4) COMMENT 'MACD指标',
    `kdj` DECIMAL(10,4) COMMENT 'KDJ指标',
    `rsi` DECIMAL(10,4) COMMENT 'RSI指标',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='股票表';

-- 用户持仓表
CREATE TABLE IF NOT EXISTS `user_position` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `stock_code` VARCHAR(10) NOT NULL COMMENT '股票代码',
    `quantity` INT NOT NULL COMMENT '持仓数量',
    `cost_price` DECIMAL(10,2) NOT NULL COMMENT '成本价',
    `current_price` DECIMAL(10,2) NOT NULL COMMENT '当前价格',
    `profit` DECIMAL(10,2) NOT NULL COMMENT '盈亏金额',
    `profit_rate` DECIMAL(5,2) NOT NULL COMMENT '盈亏比例',
    `upper_limit` DECIMAL(10,2) COMMENT '价格上限',
    `lower_limit` DECIMAL(10,2) COMMENT '价格下限',
    `enable_alert` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用预警',
    `enable_email` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用邮件通知',
    `enable_sms` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否启用短信通知',
    `enable_wechat` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否启用微信通知',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_stock_code` (`stock_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户持仓表';

-- 预警记录表
CREATE TABLE IF NOT EXISTS `alert_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `stock_code` VARCHAR(10) NOT NULL COMMENT '股票代码',
    `alert_type` VARCHAR(20) NOT NULL COMMENT '预警类型',
    `alert_content` TEXT NOT NULL COMMENT '预警内容',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-未处理，1-已处理',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_stock_code` (`stock_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警记录表'; 