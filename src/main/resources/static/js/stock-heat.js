$(document).ready(function () {
    // 初始化图表对象
    let upChart = null;
    let downChart = null;
    let volumeChart = null;
    let valueChart = null;

    // 默认参数
    let currentPage = 1;
    let limit = 10;
    let market = 'hs_a';
    let sort = 'changeRate';
    let asc = '0';

    // 初始化图表
    function initCharts() {
        if (!upChart) upChart = echarts.init(document.getElementById('upChart'));
        if (!downChart) downChart = echarts.init(document.getElementById('downChart'));
        if (!volumeChart) volumeChart = echarts.init(document.getElementById('volumeChart'));
        if (!valueChart) valueChart = echarts.init(document.getElementById('valueChart'));

        // 响应窗口大小变化
        window.addEventListener('resize', function () {
            upChart.resize();
            downChart.resize();
            volumeChart.resize();
            valueChart.resize();
        });
    }

    // 初始加载
    initCharts();
    loadStockRanking();

    // 绑定筛选条件变化事件
    $('#marketSelect, #limitSelect').change(function () {
        market = $('#marketSelect').val();
        limit = $('#limitSelect').val();
        loadStockRanking();
    });

    // 绑定刷新按钮事件
    $('#refreshBtn').click(function () {
        loadStockRanking();
    });

    // 绑定表格上方分类按钮
    $('.card-header .btn-group .btn').click(function () {
        $(this).siblings().removeClass('active');
        $(this).addClass('active');

        sort = $(this).data('sort');
        asc = $(this).data('asc');
        currentPage = 1;
        loadStockRanking();
    });

    // 加载股票排行数据
    function loadStockRanking() {
        $.ajax({
            url: '/api/stocks/ranking',
            type: 'GET',
            data: {
                page: currentPage,
                limit: limit,
                market: market,
                sort: sort,
                asc: asc
            },
            success: function (response) {
                if (response.success) {
                    renderStockTable(response.data.list);
                    renderPagination(response.data.totalPages || 5, currentPage);

                    // 同时获取四种不同排序的数据
                    loadChartData();
                } else {
                    showError('获取股票数据失败：' + response.msg);
                }
            },
            error: function (xhr) {
                showError('请求失败：' + xhr.statusText);
            }
        });
    }

    // 加载四个图表的数据
    function loadChartData() {
        // 涨幅榜
        $.ajax({
            url: '/api/stocks/ranking',
            type: 'GET',
            data: {
                page: 1,
                limit: 10,
                market: market,
                sort: 'changeRate',
                asc: 0
            },
            success: function (response) {
                if (response.success) {
                    renderUpChart(response.data.list);
                }
            }
        });

        // 跌幅榜
        $.ajax({
            url: '/api/stocks/ranking',
            type: 'GET',
            data: {
                page: 1,
                limit: 10,
                market: market,
                sort: 'changeRate',
                asc: 1
            },
            success: function (response) {
                if (response.success) {
                    renderDownChart(response.data.list);
                }
            }
        });

        // 成交量榜
        $.ajax({
            url: '/api/stocks/ranking',
            type: 'GET',
            data: {
                page: 1,
                limit: 10,
                market: market,
                sort: 'volume',
                asc: 0
            },
            success: function (response) {
                if (response.success) {
                    renderVolumeChart(response.data.list);
                }
            }
        });

        // 成交额榜
        $.ajax({
            url: '/api/stocks/ranking',
            type: 'GET',
            data: {
                page: 1,
                limit: 10,
                market: market,
                sort: 'value',
                asc: 0
            },
            success: function (response) {
                if (response.success) {
                    renderValueChart(response.data.list);
                }
            }
        });
    }

    // 渲染涨幅榜图表
    function renderUpChart(stocks) {
        if (!stocks || stocks.length === 0) return;

        const names = [];
        const values = [];

        // 使用 reverse 是为了让最大涨幅的在顶部
        stocks.reverse().forEach(stock => {
            if (parseFloat(stock.changeRate) > 0) {
                names.push(stock.name);
                values.push(parseFloat(stock.changeRate));
            }
        });

        const option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                },
                formatter: function (params) {
                    return params[0].name + ': ' + params[0].value + '%';
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: {
                type: 'value',
                position: 'top',
                axisLabel: {
                    formatter: '{value}%'
                }
            },
            yAxis: {
                type: 'category',
                data: names,
                axisLabel: {
                    fontSize: 10
                }
            },
            series: [
                {
                    name: '涨幅',
                    type: 'bar',
                    data: values,
                    itemStyle: {
                        color: function (params) {
                            // 根据数值大小渐变颜色从浅红到深红
                            const value = params.value;
                            const maxValue = Math.max(...values);
                            const ratio = value / maxValue;
                            return {
                                type: 'linear',
                                x: 0, y: 0, x2: 1, y2: 0,
                                colorStops: [{
                                    offset: 0, color: '#ffcccc'
                                }, {
                                    offset: ratio, color: '#ff3333'
                                }]
                            };
                        }
                    },
                    label: {
                        show: true,
                        position: 'right',
                        formatter: '{c}%'
                    }
                }
            ]
        };

        upChart.setOption(option);
    }

    // 渲染跌幅榜图表
    function renderDownChart(stocks) {
        if (!stocks || stocks.length === 0) return;

        const names = [];
        const values = [];

        // 获取跌幅股票并进行排序
        stocks.forEach(stock => {
            if (parseFloat(stock.changeRate) < 0) {
                names.push(stock.name);
                values.push(parseFloat(stock.changeRate));
            }
        });

        const option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                },
                formatter: function (params) {
                    return params[0].name + ': ' + params[0].value + '%';
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: {
                type: 'value',
                position: 'top',
                axisLabel: {
                    formatter: '{value}%'
                }
            },
            yAxis: {
                type: 'category',
                data: names,
                axisLabel: {
                    fontSize: 10
                }
            },
            series: [
                {
                    name: '跌幅',
                    type: 'bar',
                    data: values,
                    itemStyle: {
                        color: function (params) {
                            // 根据数值大小渐变颜色从浅绿到深绿
                            const value = Math.abs(params.value);
                            const maxValue = Math.max(...values.map(v => Math.abs(v)));
                            const ratio = value / maxValue;
                            return {
                                type: 'linear',
                                x: 0, y: 0, x2: 1, y2: 0,
                                colorStops: [{
                                    offset: 0, color: '#ccffcc'
                                }, {
                                    offset: ratio, color: '#33cc33'
                                }]
                            };
                        }
                    },
                    label: {
                        show: true,
                        position: 'right',
                        formatter: '{c}%'
                    }
                }
            ]
        };

        downChart.setOption(option);
    }

    // 渲染成交量榜图表
    function renderVolumeChart(stocks) {
        if (!stocks || stocks.length === 0) return;

        const names = [];
        const values = [];

        // 使用 reverse 是为了让最高成交量的在顶部
        stocks.reverse().forEach(stock => {
            names.push(stock.name);
            values.push(parseInt(stock.volume) / 10000); // 转为万手
        });

        const option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                },
                formatter: function (params) {
                    return params[0].name + ': ' + params[0].value.toFixed(2) + '万手';
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: {
                type: 'value',
                position: 'top',
                axisLabel: {
                    formatter: '{value}万手'
                }
            },
            yAxis: {
                type: 'category',
                data: names,
                axisLabel: {
                    fontSize: 10
                }
            },
            series: [
                {
                    name: '成交量',
                    type: 'bar',
                    data: values,
                    itemStyle: {
                        color: function (params) {
                            // 根据数值大小渐变颜色从浅蓝到深蓝
                            const value = params.value;
                            const maxValue = Math.max(...values);
                            const ratio = value / maxValue;
                            return {
                                type: 'linear',
                                x: 0, y: 0, x2: 1, y2: 0,
                                colorStops: [{
                                    offset: 0, color: '#ccf2ff'
                                }, {
                                    offset: ratio, color: '#0099cc'
                                }]
                            };
                        }
                    },
                    label: {
                        show: true,
                        position: 'right',
                        formatter: function (params) {
                            return params.value.toFixed(2) + '万手';
                        }
                    }
                }
            ]
        };

        volumeChart.setOption(option);
    }

    // 渲染成交额榜图表
    function renderValueChart(stocks) {
        if (!stocks || stocks.length === 0) return;

        const names = [];
        const values = [];

        // 使用 reverse 是为了让最高成交额的在顶部
        stocks.reverse().forEach(stock => {
            names.push(stock.name);
            values.push(parseInt(stock.value) / 100000000); // 转为亿元
        });

        const option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                },
                formatter: function (params) {
                    return params[0].name + ': ' + params[0].value.toFixed(2) + '亿元';
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: {
                type: 'value',
                position: 'top',
                axisLabel: {
                    formatter: '{value}亿元'
                }
            },
            yAxis: {
                type: 'category',
                data: names,
                axisLabel: {
                    fontSize: 10
                }
            },
            series: [
                {
                    name: '成交额',
                    type: 'bar',
                    data: values,
                    itemStyle: {
                        color: function (params) {
                            // 根据数值大小渐变颜色从浅黄到深黄
                            const value = params.value;
                            const maxValue = Math.max(...values);
                            const ratio = value / maxValue;
                            return {
                                type: 'linear',
                                x: 0, y: 0, x2: 1, y2: 0,
                                colorStops: [{
                                    offset: 0, color: '#fff2cc'
                                }, {
                                    offset: ratio, color: '#ffcc00'
                                }]
                            };
                        }
                    },
                    label: {
                        show: true,
                        position: 'right',
                        formatter: function (params) {
                            return params.value.toFixed(2) + '亿元';
                        }
                    }
                }
            ]
        };

        valueChart.setOption(option);
    }

    // 渲染股票表格
    function renderStockTable(stocks) {
        const tableBody = $('#stockTableBody');
        tableBody.empty();

        if (!stocks || stocks.length === 0) {
            tableBody.append('<tr><td colspan="10" class="text-center">暂无数据</td></tr>');
            return;
        }

        stocks.forEach((stock, index) => {
            const changeClass = parseFloat(stock.changeRate) >= 0 ? 'positive-change' : 'negative-change';
            const updateTime = formatDateTime(stock.updateTime * 1000);

            const row = `
                <tr data-symbol="${stock.symbol}">
                    <td>${(currentPage - 1) * limit + index + 1}</td>
                    <td>${stock.symbol}</td>
                    <td class="stock-name">${stock.name}</td>
                    <td>${stock.price}</td>
                    <td class="${changeClass}">${stock.changeRate}%</td>
                    <td>${formatNumber(stock.volume / 10000)}万手</td>
                    <td>${formatNumber(stock.value / 100000000)}亿元</td>
                    <td>${stock.high}</td>
                    <td>${stock.low}</td>
                    <td>${updateTime}</td>
                </tr>
            `;
            tableBody.append(row);
        });

        // 绑定行点击事件，可以跳转到详情页
        $('#stockTableBody tr').click(function () {
            const symbol = $(this).data('symbol');
            window.location.href = '/stocks/detail/' + symbol;
        });
    }

    // 渲染分页
    function renderPagination(totalPages, currentPage) {
        const pagination = $('#pagination');
        pagination.empty();

        // 上一页
        const prevDisabled = currentPage <= 1 ? 'disabled' : '';
        pagination.append(`
            <li class="page-item ${prevDisabled}">
                <a class="page-link" href="#" data-page="${currentPage - 1}">&laquo;</a>
            </li>
        `);

        // 页码
        const startPage = Math.max(1, currentPage - 2);
        const endPage = Math.min(totalPages, startPage + 4);

        for (let i = startPage; i <= endPage; i++) {
            const active = i === currentPage ? 'active' : '';
            pagination.append(`
                <li class="page-item ${active}">
                    <a class="page-link" href="#" data-page="${i}">${i}</a>
                </li>
            `);
        }

        // 下一页
        const nextDisabled = currentPage >= totalPages ? 'disabled' : '';
        pagination.append(`
            <li class="page-item ${nextDisabled}">
                <a class="page-link" href="#" data-page="${currentPage + 1}">&raquo;</a>
            </li>
        `);

        // 绑定分页点击事件
        $('.page-link').click(function (e) {
            e.preventDefault();
            if (!$(this).parent().hasClass('disabled') && !$(this).parent().hasClass('active')) {
                currentPage = parseInt($(this).data('page'));
                loadStockRanking();
            }
        });
    }

    // 格式化数字（加千位分隔符）
    function formatNumber(num) {
        if (!num) return '0';
        return parseFloat(num).toLocaleString();
    }

    // 格式化日期时间
    function formatDateTime(timestamp) {
        const date = new Date(timestamp);
        const hours = date.getHours().toString().padStart(2, '0');
        const minutes = date.getMinutes().toString().padStart(2, '0');
        const seconds = date.getSeconds().toString().padStart(2, '0');
        return `${hours}:${minutes}:${seconds}`;
    }

    // 显示错误信息
    function showError(message) {
        $('#stockTableBody').html(`<tr><td colspan="10" class="text-center text-danger">${message}</td></tr>`);
    }
});