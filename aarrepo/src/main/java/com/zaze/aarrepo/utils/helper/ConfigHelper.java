package com.zaze.aarrepo.utils.helper;

import com.zaze.aarrepo.utils.FileUtil;
import com.zaze.aarrepo.utils.PropertiesUtil;

import java.io.File;
import java.io.IOException;
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
    private String filePath;

    public static ConfigHelper newInstance(String filePath) {
        return new ConfigHelper(filePath);
    }

    private ConfigHelper(String filePath) {
        this.filePath = filePath;
    }


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
        try {
            FileUtil.createFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Properties properties = PropertiesUtil.load(filePath);
        properties.setProperty(key, value);
        PropertiesUtil.store(filePath, properties);
    }


    /**
     * 保存配置信息
     *
     * @param key
     * @param value
     */
    @Deprecated
    public <K, V> void store(K key, V value) {
        if (key == null || value == null) {
            return;
        }
        try {
            FileUtil.createFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Properties properties = PropertiesUtil.load(filePath);
        properties.put(key, value);
        PropertiesUtil.store(filePath, properties);
    }

    /**
     * 保存配置信息
     *
     * @param map
     */
    public void storeAll(Map<String, String> map) {
        if (map == null) {
            return;
        }
        try {
            FileUtil.createFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Properties properties = PropertiesUtil.load(filePath);
        properties.putAll(map);
        PropertiesUtil.store(filePath, properties);
    }
    // --------------------------------------------------

    /**
     * 加载配置信息
     *
     * @return String value
     */
    public String getProperty(String key) {
        return PropertiesUtil.getProperty(filePath, key);
    }

    /**
     * 加载配置信息
     *
     * @return Properties
     */
    public Properties load() {
        File file = new File(filePath);
        if (file.exists()) {
            return PropertiesUtil.load(filePath);
        } else {
            return new Properties();
        }
    }


}
