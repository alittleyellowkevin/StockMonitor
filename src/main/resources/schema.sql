CREATE TABLE IF NOT EXISTS `stock` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `code` VARCHAR(10) NOT NULL COMMENT '股票代码',
    `name` VARCHAR(50) NOT NULL COMMENT '股票名称',
    `price` DECIMAL(10,2) COMMENT '当前价格',
    `change` DECIMAL(10,2) COMMENT '涨跌幅',
    `volume` BIGINT COMMENT '成交量',
    `high` DECIMAL(10,2) COMMENT '最高价',
    `low` DECIMAL(10,2) COMMENT '最低价',
    `close` DECIMAL(10,2) COMMENT '收盘价',
    `macd` DECIMAL(10,4) COMMENT 'MACD指标',
    `kdj` DECIMAL(10,4) COMMENT 'KDJ指标',
    `rsi` DECIMAL(10,4) COMMENT 'RSI指标',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='股票信息表';

CREATE TABLE IF NOT EXISTS `user_position` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `stock_code` VARCHAR(10) NOT NULL COMMENT '股票代码',
    `quantity` INT NOT NULL COMMENT '持仓数量',
    `cost_price` DECIMAL(10,2) NOT NULL COMMENT '成本价',
    `current_price` DECIMAL(10,2) COMMENT '当前价格',
    `profit` DECIMAL(10,2) COMMENT '盈亏金额',
    `profit_rate` DECIMAL(10,4) COMMENT '盈亏比例',
    `monitor_high` DECIMAL(10,2) COMMENT '监控高价',
    `monitor_low` DECIMAL(10,2) COMMENT '监控低价',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_stock` (`user_id`, `stock_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户持仓表'; 