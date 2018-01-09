package com.zaze.utils.config;

import com.zaze.utils.ZFileUtil;

import java.util.Map;
import java.util.Properties;

/**
 * Description : 本地 偏好 设置
 * date : 2016-01-07 - 10:46
 *
 * @author : zaze
 * @version : 1.0
 */
public class ZConfigHelper {
    public static final String KEY_CREATE_TIME = "create_time";

    private String filePath;

    public static ZConfigHelper newInstance(String filePath) {
        return new ZConfigHelper(filePath);
    }

    private ZConfigHelper(String filePath) {
        this.filePath = filePath;
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
        ZPropertiesUtil.store(filePath, properties);
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
        ZPropertiesUtil.store(filePath, properties);
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
        ZPropertiesUtil.store(filePath, properties);
    }

    // --------------------------------------------------

    /**
     * 获取值
     *
     * @return String value
     */
    public String getProperty(String key) {
        return ZPropertiesUtil.getProperty(filePath, key);
    }

    /**
     * 加载配置信息
     *
     * @return Properties
     */
    public Properties load() {
        boolean isNew = false;
        if (!ZFileUtil.INSTANCE.isFileExist(filePath)) {
            ZFileUtil.INSTANCE.createFileNotExists(filePath);
            isNew = true;
        }
        Properties properties = ZPropertiesUtil.load(filePath);
        if (isNew) {
            properties.setProperty(KEY_CREATE_TIME, String.valueOf(System.currentTimeMillis()));
        }
        return properties;
    }
}
