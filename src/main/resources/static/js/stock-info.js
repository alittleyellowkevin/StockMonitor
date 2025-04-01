$(document).ready(function () {
    let klineChart = null;

    // 初始化ECharts
    function initECharts() {
        if (klineChart) return;
        klineChart = echarts.init(document.getElementById('klineChart'));
    }

    // 绑定查询按钮点击事件
    $('#searchStockBtn').click(function () {
        const symbol = $('#stockSearchSymbol').val().trim();
        if (symbol) {
            searchStockInfo(symbol);
        } else {
            alert('请输入股票代码');
        }
    });

    // 绑定股票代码输入框回车事件
    $('#stockSearchSymbol').keypress(function (e) {
        if (e.which === 13) {
            $('#searchStockBtn').click();
        }
    });

    // 查询股票信息
    function searchStockInfo(symbol) {
        showLoading();
        hideError();
        hideContent();

        // 这里应该从服务器获取股票信息
        // 为了演示，我们使用模拟数据
        setTimeout(() => {
            if (symbol.startsWith('sh60') || symbol.startsWith('sz00')) {
                const mockData = getMockStockData(symbol);
                renderStockInfo(mockData);
                getStockKline(symbol);
            } else {
                showError('无效的股票代码');
                hideLoading();
            }
        }, 1000);
    }

    // 获取股票K线数据
    function getStockKline(symbol) {
        // 这里应该从服务器获取K线数据
        // 为了演示，我们使用模拟数据
        setTimeout(() => {
            const mockKlineData = getMockKlineData();
            renderKlineChart(mockKlineData);
            hideLoading();
            showContent();
        }, 1000);
    }

    // 渲染股票基本信息
    function renderStockInfo(data) {
        const basicInfo = $('#stockBasicInfo');
        const marketInfo = $('#stockMarketInfo');

        basicInfo.empty();
        marketInfo.empty();

        // 渲染基本资料
        basicInfo.append(`
            <tr><th width="30%">股票名称</th><td>${data.name}</td></tr>
            <tr><th>股票代码</th><td>${data.symbol}</td></tr>
            <tr><th>所属行业</th><td>${data.industry}</td></tr>
            <tr><th>上市日期</th><td>${data.listingDate}</td></tr>
            <tr><th>总股本</th><td>${data.totalShares}亿股</td></tr>
            <tr><th>流通股本</th><td>${data.circulatingShares}亿股</td></tr>
            <tr><th>地区</th><td>${data.region}</td></tr>
        `);

        // 渲染市场表现
        marketInfo.append(`
            <tr><th width="30%">当前价格</th><td>${data.price}</td></tr>
            <tr><th>涨跌幅</th><td class="${data.changeRate >= 0 ? 'positive-change' : 'negative-change'}">${data.changeRate}%</td></tr>
            <tr><th>今日开盘</th><td>${data.open}</td></tr>
            <tr><th>昨日收盘</th><td>${data.preClose}</td></tr>
            <tr><th>今日最高</th><td>${data.high}</td></tr>
            <tr><th>今日最低</th><td>${data.low}</td></tr>
            <tr><th>成交量</th><td>${data.volume}万手</td></tr>
            <tr><th>成交额</th><td>${data.value}亿元</td></tr>
            <tr><th>市盈率</th><td>${data.pe}</td></tr>
            <tr><th>市净率</th><td>${data.pb}</td></tr>
        `);
    }

    // 渲染K线图
    function renderKlineChart(data) {
        initECharts();

        const option = {
            title: {
                text: '股票K线图'
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross'
                }
            },
            legend: {
                data: ['K线', 'MA5', 'MA10', 'MA20', 'MA30']
            },
            grid: {
                left: '10%',
                right: '10%',
                bottom: '15%'
            },
            xAxis: {
                type: 'category',
                data: data.dates,
                scale: true,
                boundaryGap: false,
                axisLine: { onZero: false },
                splitLine: { show: false },
                splitNumber: 20
            },
            yAxis: {
                scale: true,
                splitArea: {
                    show: true
                }
            },
            dataZoom: [
                {
                    type: 'inside',
                    start: 50,
                    end: 100
                },
                {
                    show: true,
                    type: 'slider',
                    bottom: '5%',
                    start: 50,
                    end: 100
                }
            ],
            series: [
                {
                    name: 'K线',
                    type: 'candlestick',
                    data: data.klineData,
                    itemStyle: {
                        color: '#c23531',
                        color0: '#314656',
                        borderColor: '#c23531',
                        borderColor0: '#314656'
                    }
                },
                {
                    name: 'MA5',
                    type: 'line',
                    data: data.ma5,
                    smooth: true,
                    lineStyle: {
                        opacity: 0.5
                    }
                },
                {
                    name: 'MA10',
                    type: 'line',
                    data: data.ma10,
                    smooth: true,
                    lineStyle: {
                        opacity: 0.5
                    }
                },
                {
                    name: 'MA20',
                    type: 'line',
                    data: data.ma20,
                    smooth: true,
                    lineStyle: {
                        opacity: 0.5
                    }
                },
                {
                    name: 'MA30',
                    type: 'line',
                    data: data.ma30,
                    smooth: true,
                    lineStyle: {
                        opacity: 0.5
                    }
                }
            ]
        };

        klineChart.setOption(option);

        // 窗口大小变化时重绘图表
        window.addEventListener('resize', function () {
            klineChart.resize();
        });
    }

    // 显示加载中状态
    function showLoading() {
        $('#stockInfoLoading').removeClass('d-none');
    }

    // 隐藏加载中状态
    function hideLoading() {
        $('#stockInfoLoading').addClass('d-none');
    }

    // 显示内容
    function showContent() {
        $('#stockInfoContent').removeClass('d-none');
    }

    // 隐藏内容
    function hideContent() {
        $('#stockInfoContent').addClass('d-none');
    }

    // 显示错误信息
    function showError(message) {
        $('#stockInfoError').removeClass('d-none').html(message);
    }

    // 隐藏错误信息
    function hideError() {
        $('#stockInfoError').addClass('d-none');
    }

    // 模拟股票基本数据
    function getMockStockData(symbol) {
        const isShanghai = symbol.startsWith('sh');
        return {
            name: isShanghai ? '上海示例股票' : '深圳示例股票',
            symbol: symbol,
            industry: '金融服务',
            listingDate: '2000-01-01',
            totalShares: '100.5',
            circulatingShares: '80.2',
            region: isShanghai ? '上海' : '深圳',
            price: '15.67',
            changeRate: isShanghai ? 2.34 : -1.25,
            open: '15.45',
            preClose: '15.31',
            high: '15.89',
            low: '15.21',
            volume: '1256.78',
            value: '196.53',
            pe: '15.2',
            pb: '1.8'
        };
    }

    // 模拟K线数据
    function getMockKlineData() {
        const dates = [];
        const klineData = [];
        const ma5 = [];
        const ma10 = [];
        const ma20 = [];
        const ma30 = [];

        const baseDate = new Date();
        let basePrice = 15;

        // 生成30天的数据
        for (let i = 29; i >= 0; i--) {
            const date = new Date(baseDate);
            date.setDate(date.getDate() - i);
            dates.push(formatDate(date));

            const open = basePrice + (Math.random() * 2 - 1);
            const close = open + (Math.random() * 2 - 1);
            const low = Math.min(open, close) - Math.random() * 0.5;
            const high = Math.max(open, close) + Math.random() * 0.5;

            klineData.push([open.toFixed(2), close.toFixed(2), low.toFixed(2), high.toFixed(2)]);

            basePrice = close;

            // 计算移动平均线
            if (i <= 24) ma5.push((basePrice + Math.random() * 0.5).toFixed(2));
            else ma5.push('-');

            if (i <= 19) ma10.push((basePrice + Math.random() * 1 - 0.5).toFixed(2));
            else ma10.push('-');

            if (i <= 9) ma20.push((basePrice + Math.random() * 1.5 - 0.75).toFixed(2));
            else ma20.push('-');

            if (i < 0) ma30.push((basePrice + Math.random() * 2 - 1).toFixed(2));
            else ma30.push('-');
        }

        return { dates, klineData, ma5, ma10, ma20, ma30 };
    }

    // 格式化日期
    function formatDate(date) {
        const year = date.getFullYear();
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        return `${year}-${month}-${day}`;
    }
});