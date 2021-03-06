package com.zaze.utils.config;

import android.content.Context;

import com.zaze.utils.FileUtil;

import java.io.File;
import java.util.Map;
import java.util.Properties;

/**
 * Description : 本地 偏好 设置
 * date : 2016-01-07 - 10:46
 *
 * @author : zaze
 * @version : 1.0
 */
public class ConfigHelper {
    /**
     * 最大 10MB
     */
    private static final long MAX_SIZE = 10 << 20;

    private String filePath;
    private int saveMode;


    public static ConfigHelper newInstance(File file) {
        return newInstance(file.getAbsolutePath());
    }

    public static ConfigHelper newInstance(String filePath) {
        return newInstance(filePath, Context.MODE_PRIVATE);
    }

    public static ConfigHelper newInstance(String filePath, int saveMode) {
        return new ConfigHelper(filePath, saveMode);
    }

    private ConfigHelper(String filePath, int saveMode) {
        this.filePath = filePath;
        this.saveMode = saveMode;
    }


    // --------------------------------------------------

    /**
     * 保存数据
     *
     * @param key
     * @param value
     */
    public void setProperty(String key, String value) {
        if (key == null || value == null) {
            return;
        }
        Properties properties = load();
        properties.setProperty(key, value);
        PropertiesUtil.store(filePath, properties);
    }

    /**
     * 保存配置信息
     *
     * @param map
     */
    public void setProperty(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        Properties properties = load();
        properties.putAll(map);
        PropertiesUtil.store(filePath, properties);
    }
    // --------------------------------------------------


    /**
     * @param key 移除
     */
    public void remove(String key) {
        if (key == null) {
            return;
        }
        Properties properties = load();
        properties.remove(key);
        PropertiesUtil.store(filePath, properties);
    }

    // --------------------------------------------------

    /**
     * 获取值
     *
     * @return String value
     */
    public String getProperty(String key) {
        Properties properties = load();
        return properties.getProperty(key);
    }

    /**
     * 加载配置信息
     *
     * @return Properties
     */
    public Properties load() {
        boolean isNew = false;
        if (!FileUtil.exists(filePath) || new File(this.filePath).length() > MAX_SIZE) {
            FileUtil.reCreateFile(filePath);
            isNew = true;
        }
        Properties properties = PropertiesUtil.load(this.filePath);
        if (isNew) {
            properties.setProperty(PropertiesUtil.CREATE_TIME_KEY, String.valueOf(System.currentTimeMillis()));
            PropertiesUtil.store(filePath, properties);
        }
        return properties;
    }
}
