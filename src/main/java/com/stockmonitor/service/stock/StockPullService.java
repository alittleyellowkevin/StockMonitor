package com.stockmonitor.service.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.alibaba.fastjson.JSON;
import com.stockmonitor.dao.stock.StockRankingDao;
import com.stockmonitor.model.mysql.StockRanking;
import com.stockmonitor.model.response.StockRankingResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Data;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * HTTP请求工具类
 */
@Component
public class StockPullService {

    // 云市场分配的密钥Id
    @Value("${tencent.api.secretId}")
    private String secretId;
    // 云市场分配的密钥Key
    @Value("${tencent.api.secretKey}")
    private String secretKey;

    // 股票排行榜URL
    private static final String STOCK_RANKING_URL = "https://ap-guangzhou.cloudmarket-apigw.com/service-b40nqs3o/finance/a-shares-ranking";

    // 股票报价URL
    private static final String STOCK_PRICE_URL = "https://ap-guangzhou.cloudmarket-apigw.com/service-b40nqs3o/finance/a-shares-price";

    // 股票K线URL
    private static final String STOCK_KLINE_URL = "https://ap-guangzhou.cloudmarket-apigw.com/service-b40nqs3o/finance/a-shares-kline";

    private static final Logger logger = LoggerFactory.getLogger(StockPullService.class);

    @Autowired
    private StockRankingDao stockRankingDao;

    /**
     * 计算API请求的授权签名
     * 
     * @param secretId  云市场分配的密钥Id
     * @param secretKey 云市场分配的密钥Key
     * @param datetime  请求时间，格式为：EEE, dd MMM yyyy HH:mm:ss 'GMT'
     * @return 授权签名字符串
     */
    public static String calcAuthorization(String secretId, String secretKey, String datetime)
            throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String signStr = "x-date: " + datetime;
        Mac mac = Mac.getInstance("HmacSHA1");
        Key sKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), mac.getAlgorithm());
        mac.init(sKey);
        byte[] hash = mac.doFinal(signStr.getBytes("UTF-8"));
        String sig = Base64.getEncoder().encodeToString(hash);

        String auth = "{\"id\":\"" + secretId + "\", \"x-date\":\"" + datetime + "\", \"signature\":\"" + sig + "\"}";
        return auth;
    }

    /**
     * 将Map参数转换为URL编码的查询字符串
     * 
     * @param map 需要转换的参数Map
     * @return URL编码后的查询字符串
     */
    public static String urlencode(Map<?, ?> map) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    URLEncoder.encode(entry.getKey().toString(), "UTF-8"),
                    URLEncoder.encode(entry.getValue().toString(), "UTF-8")));
        }
        return sb.toString();
    }

    /**
     * 获取股票排行榜数据
     */
    public String saveStockRanking(String asc, String limit, String market, String page, String sort) {
        String result = "";
        BufferedReader in = null;
        try {
            // 获取当前时间，用于生成签名
            Calendar cd = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            String datetime = sdf.format(cd.getTime());

            // 计算签名
            String authorization = calcAuthorization(secretId, secretKey, datetime);

            // 请求头
            String uuid = UUID.randomUUID().toString();
            Map<String, String> headers = new HashMap<>();
            headers.put("request-id", uuid);
            headers.put("Authorization", authorization);
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            headers.put("Host", "ap-guangzhou.cloudmarket-apigw.com");
            headers.put("X-Date", datetime);

            // body参数
            Map<String, String> bodyParams = new HashMap<>();
            bodyParams.put("asc", asc);
            bodyParams.put("limit", limit);
            bodyParams.put("market", market);
            bodyParams.put("page", page);
            bodyParams.put("sort", sort);
            String bodyParamStr = urlencode(bodyParams);

            URL realUrl = new URL(STOCK_RANKING_URL);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // 设置请求头
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }

            // 写入请求体
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = bodyParamStr.getBytes("UTF-8");
                os.write(input, 0, input.length);
            }

            // 获取响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
            StockRankingResponse response = JSON.parseObject(result, StockRankingResponse.class);
            List<StockRanking> data = response.getData().getList();

            // 批量插入股票排行榜数据
            stockRankingDao.batchInsert(data);

            logger.info("股票排行榜 API 响应结果：{}", result);
        } catch (Exception e) {
            logger.error("获取股票排行榜数据失败", e);
            throw new RuntimeException("获取股票排行榜数据失败", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("关闭输入流失败", e);
                }
            }
        }
        return result;
    }

    /**
     * 获取股票报价
     * 
     * @param symbol 股票代码
     * @return 股票报价数据
     */
    public String saveStockPrice(String symbol) {
        String result = ""; // 初始化返回值
        BufferedReader in = null;

        try {
            Calendar cd = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            String datetime = sdf.format(cd.getTime());
            // 签名
            String auth = calcAuthorization(secretId, secretKey, datetime);
            // 请求方法
            String method = "POST";
            // 请求头
            String uuid = UUID.randomUUID().toString();
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("request-id", uuid);
            headers.put("Authorization", auth);

            // 查询参数
            Map<String, String> queryParams = new HashMap<String, String>();

            // body参数
            Map<String, String> bodyParams = new HashMap<String, String>();
            bodyParams.put("symbol", symbol);
            String bodyParamStr = urlencode(bodyParams);

            // url参数拼接
            String url = STOCK_PRICE_URL;
            if (!queryParams.isEmpty()) {
                url += "?" + urlencode(queryParams);
            }

            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod(method);

            // request headers
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }

            // request body
            Map<String, Boolean> methods = new HashMap<>();
            methods.put("POST", true);
            methods.put("PUT", true);
            methods.put("PATCH", true);
            Boolean hasBody = methods.get(method);
            if (hasBody != null) {
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                conn.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(bodyParamStr);
                out.flush();
                out.close();
            }

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

            logger.info("股票报价 API 响应结果：{}", result);
        } catch (Exception e) {
            logger.error("股票报价 API 请求失败：", e);
            throw new RuntimeException("获取股票报价失败", e); // 抛出异常而不是返回null
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    logger.error("关闭输入流失败：", e);
                }
            }
        }
        return result; // 确保总是返回非null值
    }

    /**
     * 获取股票K线数据
     * 
     * @param limit  每页条数
     * @param ma     均线
     * @param symbol 股票代码
     * @param type   类型
     * @return 股票K线数据
     */
    public String saveStockKline(String limit, String ma, String symbol, String type) {
        String result = ""; // 初始化返回值
        BufferedReader in = null;

        try {
            Calendar cd = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            String datetime = sdf.format(cd.getTime());
            // 签名
            String auth = calcAuthorization(secretId, secretKey, datetime);
            // 请求方法
            String method = "POST";
            // 请求头
            String uuid = UUID.randomUUID().toString();
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("request-id", uuid);
            headers.put("Authorization", auth);

            // 查询参数
            Map<String, String> queryParams = new HashMap<String, String>();

            // body参数
            Map<String, String> bodyParams = new HashMap<String, String>();
            bodyParams.put("limit", limit);
            bodyParams.put("ma", ma);
            bodyParams.put("symbol", symbol);
            bodyParams.put("type", type);
            String bodyParamStr = urlencode(bodyParams);

            // url参数拼接
            String url = STOCK_KLINE_URL;
            if (!queryParams.isEmpty()) {
                url += "?" + urlencode(queryParams);
            }

            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod(method);

            // request headers
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }

            // request body
            Map<String, Boolean> methods = new HashMap<>();
            methods.put("POST", true);
            methods.put("PUT", true);
            methods.put("PATCH", true);
            Boolean hasBody = methods.get(method);
            if (hasBody != null) {
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                conn.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(bodyParamStr);
                out.flush();
                out.close();
            }

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

            logger.info("股票K线 API 响应结果：{}", result);
        } catch (Exception e) {
            logger.error("股票K线 API 请求失败：", e);
            throw new RuntimeException("获取股票K线数据失败", e); // 抛出异常而不是返回null
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    logger.error("关闭输入流失败：", e);
                }
            }
        }
        return result; // 确保总是返回非null值
    }
}