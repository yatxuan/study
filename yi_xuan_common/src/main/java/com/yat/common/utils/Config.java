package com.yat.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Description: 读取配置文件 默认的config.properties 和自定义都支持 </p>
 *
 * @author Yat-Xuan
 * @date 2020/8/7 15:27
 */
@SuppressWarnings("all")
public class Config {

    private static final String DEFAULT_CONF = "config.properties";

    private static final Map<String, Config> INSTANCES = new ConcurrentHashMap<>(16);

    private Properties configuration = new Properties();

    private Config() {
        initConfig(DEFAULT_CONF);
    }

    private Config(String configFile) {
        initConfig(configFile);
    }

    private void initConfig(String configFile) {
        try (InputStream is = Config.class.getClassLoader().getResourceAsStream(configFile)) {
            configuration.load(is);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 获得Configuration实例。 默认为config.property
     *
     * @return Configuration实例
     */
    public static Config getInstance() {
        return getInstance(DEFAULT_CONF);
    }

    /**
     * 自定义文件解析 **.property
     *
     * @param configFile 、
     * @return 、
     */
    public static Config getInstance(String configFile) {
        Config config = INSTANCES.get(configFile);
        if (config == null) {
            synchronized (INSTANCES) {
                config = INSTANCES.get(configFile);
                if (config == null) {
                    config = new Config(configFile);
                    INSTANCES.put(configFile, config);
                }
            }
        }
        return config;
    }

    /**
     * 获取 String 类型配置项数据。
     *
     * @param key 配置关键字
     * @return 配置项
     */
    public String getStringValue(String key) {
        return configuration.getProperty(key);
    }

    /**
     * 获取 String 类型配置项数据,若数据为空，给一个默认的初始值
     *
     * @param key          配置关键字
     * @param defaultValue 默认值
     * @return 配置项
     */
    public String getStringValue(String key, String defaultValue) {
        String value = this.getStringValue(key);
        if (value == null) {
            return defaultValue;
        } else {
            return value;
        }
    }

    /**
     * 获取 int 类型配置项数据,若数据为空，给一个默认的初始值
     *
     * @param key          配置关键字
     * @param defaultValue 默认值
     * @return 配置项
     */
    public int getIntValue(String key, int defaultValue) {
        return LangUtil.parseInt(configuration.getProperty(key), defaultValue);
    }

    /**
     * 获取 int 类型配置项数据
     *
     * @param key 配置关键字
     * @return 配置项
     */
    public int getIntValue(String key) {
        return LangUtil.parseInt(configuration.getProperty(key));
    }

    /**
     * 获取 Double 类型配置项数据,若数据为空，给一个默认的初始值
     *
     * @param key          配置关键字
     * @param defaultValue 默认值
     * @return 配置项
     */
    public double getDoubleValue(String key, Double defaultValue) {
        return LangUtil.parseDouble(configuration.getProperty(key), defaultValue);
    }

    /**
     * 获取 Double 类型配置项数据
     *
     * @param key 配置关键字
     * @return 配置项
     */
    public double getDoubleValue(String key) {
        return LangUtil.parseDouble(configuration.getProperty(key));
    }

    /**
     * 获取 Long 类型字段数据,若数据为空，给一个默认的初始值
     *
     * @param key          配置关键字
     * @param defaultValue 默认值
     * @return 配置项
     */
    public double getLongValue(String key, Long defaultValue) {
        return LangUtil.parseLong(configuration.getProperty(key), defaultValue);
    }

    /**
     * 获取 Long 类型配置项数据
     *
     * @param key 配置关键字
     * @return 配置项
     */
    public double getLongValue(String key) {
        return LangUtil.parseLong(configuration.getProperty(key));
    }

    /**
     * 获取 Boolean 类型字段数据,若数据为空，给一个默认的初始值
     *
     * @param key          配置关键字
     * @param defaultValue 默认值
     * @return 配置项
     */
    public Boolean getBooleanValue(String key, Boolean defaultValue) {
        return LangUtil.parseBoolean(configuration.getProperty(key), defaultValue);
    }

    /**
     * 获取 Boolean 类型配置项数据
     *
     * @param key 配置关键字
     * @return 配置项
     */
    public Boolean getBooleanValue(String key) {
        return LangUtil.parseBoolean(configuration.getProperty(key));
    }


    public static void main(String[] args) {
        // 获取boolean类型字段数据
        Boolean enable = Config.getInstance().getBooleanValue("server.ssl.enable");
        // 获取boolean类型字段数据,若数据为空，给一个默认的初始值
        Boolean needsClientAuth = Config.getInstance().getBooleanValue("server.ssl.needsClientAuth", false);
        System.out.println(enable);
        System.out.println(needsClientAuth);
    }
}
