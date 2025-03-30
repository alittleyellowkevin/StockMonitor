# 股票交易监控系统

## 项目简介
本项目是一个实时股票持仓监控系统，通过对接交易所实时行情数据，持续跟踪用户持仓股票的价格、涨跌幅和成交量等关键指标变化。系统实时计算MACD、KDJ、RSI等技术指标，并支持用户自定义监控规则，当价格突破设定阈值或技术指标出现特定形态时，通过短信、邮件等多渠道及时推送预警信息。

## 技术栈
- 后端框架：Spring Boot 2.7.0
- 数据库：MySQL 8.0
- 缓存：Redis
- 消息队列：Kafka
- 定时任务：XXL-Job
- 持久层：MyBatis
- 实时通信：WebSocket
- 股票数据：腾讯股票API

## 主要功能
1. 实时数据抓取
   - 定时获取股票市场最新价格
   - 更新用户持仓信息
   - 计算技术指标

2. 持仓监控
   - 价格监控
   - 技术指标监控
   - 自定义监控规则

3. 预警通知
   - 邮件通知
   - 短信通知
   - 微信通知
   - WebSocket实时推送

4. 系统优化
   - Redis缓存
   - Kafka异步处理
   - 数据库索引优化

## 快速开始

### 环境要求
- JDK 1.8+
- MySQL 8.0+
- Redis 6.0+
- Kafka 2.8.0+
- XXL-Job 2.3.1+

### 安装步骤
1. 克隆项目
```bash
git clone https://github.com/yourusername/stock-monitor.git
```

2. 初始化数据库
```bash
mysql -u root -p < src/main/resources/db/init.sql
```

3. 修改配置
编辑 `src/main/resources/application.yml` 文件，配置数据库、Redis、Kafka等信息

4. 启动项目
```bash
mvn spring-boot:run
```

## 项目结构
```
stock-monitor/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── stockmonitor/
│   │   │           ├── config/     # 配置类
│   │   │           ├── controller/ # 控制器
│   │   │           ├── service/    # 服务层
│   │   │           ├── dao/        # 数据访问层
│   │   │           ├── model/      # 实体类
│   │   │           └── util/       # 工具类
│   │   └── resources/
│   │       ├── application.yml     # 主配置文件
│   │       ├── mapper/            # MyBatis映射文件
│   │       └── db/                # 数据库脚本
│   └── test/                      # 测试代码
└── pom.xml                        # Maven配置文件
```

## 贡献指南
1. Fork 本仓库
2. 创建新的分支 `git checkout -b feature/your-feature`
3. 提交更改 `git commit -am 'Add some feature'`
4. 推送到分支 `git push origin feature/your-feature`
5. 提交 Pull Request

## 许可证
本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件 