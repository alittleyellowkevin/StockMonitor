INSERT INTO stock (code, name, price, `change`, volume, high, low, close, macd, kdj, rsi)
VALUES 
('600000', '浦发银行', 10.25, 2.5, 1000000, 10.50, 10.00, 10.25, 0.5, 65.5, 55.5),
('601318', '中国平安', 45.67, -1.2, 2000000, 46.00, 45.20, 45.67, -0.2, 45.5, 48.2);

INSERT INTO user_position (user_id, stock_code, quantity, cost_price, current_price, profit, profit_rate, monitor_high, monitor_low)
VALUES 
(1, '600000', 1000, 9.80, 10.25, 450.00, 4.59, 11.00, 9.50),
(1, '601318', 500, 46.20, 45.67, -265.00, -1.14, 47.00, 45.00); 