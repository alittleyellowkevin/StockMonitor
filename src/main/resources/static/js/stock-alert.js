$(document).ready(function () {
    // 初始化提醒表格
    loadAlerts();

    // 绑定表单提交事件
    $('#alertForm').submit(function (e) {
        e.preventDefault();
        addAlert();
    });

    // 绑定股票代码输入事件，自动获取股票名称
    $('#stockSymbol').blur(function () {
        const symbol = $(this).val().trim();
        if (symbol) {
            getStockInfo(symbol);
        }
    });

    // 加载已设置的提醒
    function loadAlerts() {
        // 这里应该从服务器获取已设置的提醒
        // 临时使用模拟数据
        const mockAlerts = [
            { id: 1, symbol: 'sh600000', name: '浦发银行', type: 'above', value: 10.5, notifyType: 'email', contact: 'user@example.com', status: 'active' },
            { id: 2, symbol: 'sz000001', name: '平安银行', type: 'below', value: 15.2, notifyType: 'app', contact: '', status: 'active' },
            { id: 3, symbol: 'sh601318', name: '中国平安', type: 'change', value: 5, notifyType: 'sms', contact: '13800138000', status: 'inactive' }
        ];
        renderAlertTable(mockAlerts);
    }

    // 渲染提醒表格
    function renderAlertTable(alerts) {
        const tableBody = $('#alertTableBody');
        tableBody.empty();

        if (!alerts || alerts.length === 0) {
            tableBody.append('<tr><td colspan="6" class="text-center">暂无提醒设置</td></tr>');
            return;
        }

        alerts.forEach(alert => {
            const typeText = getTypeText(alert.type, alert.value);
            const statusBadge = alert.status === 'active' ?
                '<span class="badge badge-success">已启用</span>' :
                '<span class="badge badge-secondary">已禁用</span>';

            const row = `
                <tr data-id="${alert.id}">
                    <td>${alert.symbol}<br><small>${alert.name}</small></td>
                    <td>${typeText}</td>
                    <td>${alert.value}</td>
                    <td>${getNotifyTypeText(alert.notifyType, alert.contact)}</td>
                    <td>${statusBadge}</td>
                    <td>
                        <button class="btn btn-sm btn-info edit-alert">编辑</button>
                        <button class="btn btn-sm btn-danger delete-alert">删除</button>
                        ${alert.status === 'active' ?
                    '<button class="btn btn-sm btn-secondary disable-alert">禁用</button>' :
                    '<button class="btn btn-sm btn-success enable-alert">启用</button>'}
                    </td>
                </tr>
            `;
            tableBody.append(row);
        });

        // 绑定操作按钮事件
        $('.edit-alert').click(function () {
            const id = $(this).closest('tr').data('id');
            editAlert(id);
        });

        $('.delete-alert').click(function () {
            const id = $(this).closest('tr').data('id');
            deleteAlert(id);
        });

        $('.disable-alert, .enable-alert').click(function () {
            const id = $(this).closest('tr').data('id');
            const enable = $(this).hasClass('enable-alert');
            toggleAlertStatus(id, enable);
        });
    }

    // 获取提醒类型文本
    function getTypeText(type, value) {
        switch (type) {
            case 'above': return `价格高于 ${value}`;
            case 'below': return `价格低于 ${value}`;
            case 'change': return `涨跌幅超过 ${value}%`;
            case 'volume': return `成交量超过 ${value}万手`;
            default: return type;
        }
    }

    // 获取通知方式文本
    function getNotifyTypeText(type, contact) {
        switch (type) {
            case 'email': return `邮件通知<br><small>${contact || '未设置'}</small>`;
            case 'sms': return `短信通知<br><small>${contact || '未设置'}</small>`;
            case 'app': return '应用内通知';
            default: return type;
        }
    }

    // 添加新提醒
    function addAlert() {
        const symbol = $('#stockSymbol').val().trim();
        const name = $('#stockName').val().trim();
        const type = $('#priceType').val();
        const value = $('#priceValue').val();
        const notifyType = $('#notifyType').val();
        const contact = $('#contactInfo').val().trim();

        if (!symbol) {
            alert('请输入股票代码');
            return;
        }

        if (!value) {
            alert('请输入提醒阈值');
            return;
        }

        if ((notifyType === 'email' || notifyType === 'sms') && !contact) {
            alert('请输入联系信息');
            return;
        }

        // 这里应该将数据发送到服务器
        // 为了演示，我们只是刷新表格
        alert('添加提醒成功！');
        $('#alertForm')[0].reset();
        loadAlerts();
    }

    // 编辑提醒
    function editAlert(id) {
        // 这里应该从服务器获取提醒详情并填充表单
        // 为了演示，我们只是显示一个消息
        alert(`编辑提醒 ID: ${id}`);
    }

    // 删除提醒
    function deleteAlert(id) {
        if (confirm('确定要删除这个提醒吗？')) {
            // 这里应该向服务器发送删除请求
            // 为了演示，我们只是刷新表格
            alert(`删除提醒 ID: ${id}`);
            loadAlerts();
        }
    }

    // 切换提醒状态
    function toggleAlertStatus(id, enable) {
        // 这里应该向服务器发送状态更改请求
        // 为了演示，我们只是刷新表格
        alert(`${enable ? '启用' : '禁用'}提醒 ID: ${id}`);
        loadAlerts();
    }

    // 获取股票信息
    function getStockInfo(symbol) {
        // 这里应该从服务器获取股票信息
        // 为了演示，我们使用模拟数据
        setTimeout(() => {
            if (symbol.startsWith('sh60') || symbol.startsWith('sz00')) {
                $('#stockName').val(symbol.startsWith('sh') ? '上海股票' : '深圳股票');
            } else {
                $('#stockName').val('');
                alert('无效的股票代码');
            }
        }, 500);
    }
});