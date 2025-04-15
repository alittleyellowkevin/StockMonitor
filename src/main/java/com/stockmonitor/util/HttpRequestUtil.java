package com.stockmonitor.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpRequestUtil {

    private static HttpRequestUtil instance;

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);

    public static HttpRequestUtil getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }

    private static synchronized void createInstance() {
        if (instance == null) {
            instance = new HttpRequestUtil();
        }
    }

    private HttpRequestUtil() {
    }

    /**
     * 发送Get请求
     * 
     * @param url
     * @param param
     * @param header
     * @param connectionTimeOut //连接过期时间，单位毫秒
     * @param readTimeOut       //读取过期时间，单位毫秒
     * @return
     */
    public String sendGet(String url, String param, Map<String, String> header, Integer connectionTimeOut,
            Integer readTimeOut) {
        String result = "";

        if (param != null) {
            if (url.indexOf("?") != -1) {
                url += "&" + param;
            } else {
                url += "?" + param;
            }
        }

        BufferedReader in = null;
        try {

            // 打开连接
            URL realUrl = new URL(url);

            // //忽略SSL证书
            // trustAllHttpsCertificates();
            // HostnameVerifier hv = new HostnameVerifier() {
            // @Override
            // public boolean verify(String urlHostName, SSLSession session) {
            // return true;
            // }
            // };
            // HttpsURLConnection.setDefaultHostnameVerifier(hv);

            URLConnection connection = realUrl.openConnection();

            // 设置超时时间
            connection.setConnectTimeout(connectionTimeOut);
            connection.setReadTimeout(readTimeOut);

            // 设置请求头
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");

            if (header != null) {
                Iterator<Entry<String, String>> it = header.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, String> entry = it.next();
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            // 建立连接
            connection.connect();

            // 定义BufferedReader输入流读取URL的响应，设置utf8防止中文乱码
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

        } catch (Exception e) {
            logger.error("http error url: " + url);
            logger.error(e.getMessage(), e);

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        return result;
    }

    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }

    public String sendGet(String url, String param, Map<String, String> header) {
        return sendGet(url, param, header, 10000, 10000);
    }

    public String sendGet(String url, String param) {
        return sendGet(url, param, null);
    }

    public String sendGet(String url, Map<String, String> params) {
        String param = null;
        if (params != null) {
            param = parseParam(params);
        }
        return sendGet(url, param);
    }

    public String sendGet(String url) {
        return sendGet(url, null, null);
    }

    public String parseParam(Map<String, String> params) {
        String result = "";
        boolean flag = true;
        for (Entry<String, String> entry : params.entrySet()) {
            if (flag) {
                result = entry.getKey() + "=" + entry.getValue();
                flag = false;

            } else {
                result += "&" + entry.getKey() + "=" + entry.getValue();

            }
        }
        return result;
    }
}
