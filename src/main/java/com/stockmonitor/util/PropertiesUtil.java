package com.stockmonitor.util;

import java.util.Properties;

import org.omg.CORBA.portable.InputStream;

public class PropertiesUtil {
    private static PropertiesUtil instance;

    public static PropertiesUtil getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }

    private static synchronized void createInstance() {
        if (instance == null) {
            instance = new PropertiesUtil();
        }
    }

    private PropertiesUtil() {
    }

    /**
     * 加载配置文件
     * 
     * @param filePath //文件路径
     * @return Properties
     */
    public Properties getAllProperties(String filePath) throws Exception {
        Properties properties = new Properties();
        InputStream inputStream = (InputStream) PropertiesUtil.class.getResourceAsStream(filePath);
        properties.load(inputStream);
        return properties;
    }

    /**
     * 从配置文件中读取数据
     * 
     * @param properties //配置文件
     * @param key        //加载主键
     * @return String
     */
    public String getValueByKey(Properties properties, String key) {
        String value = properties.getProperty(key);
        return value == null ? null : value.trim();
    }
}
