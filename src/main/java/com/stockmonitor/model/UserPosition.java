package com.stockmonitor.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserPosition {
    private Long id;
    private Long userId; // 用户ID
    private String stockCode; // 股票代码
    private Integer quantity; // 持仓数量
    private BigDecimal costPrice; // 成本价
    private BigDecimal currentPrice; // 当前价格
    private BigDecimal profit; // 盈亏金额
    private BigDecimal profitRate; // 盈亏比例
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间

    // 监控设置
    private BigDecimal upperLimit; // 价格上限
    private BigDecimal lowerLimit; // 价格下限
    private Boolean enableAlert; // 是否启用预警
    private Boolean enableEmail; // 是否启用邮件通知
    private Boolean enableSms; // 是否启用短信通知
    private Boolean enableWechat; // 是否启用微信通知
}