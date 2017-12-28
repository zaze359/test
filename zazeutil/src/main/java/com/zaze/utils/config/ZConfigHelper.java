package com.zaze.utils.config;

import com.zaze.utils.ZFileUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

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
        boolean isNew = false;
        if (!ZFileUtil.INSTANCE.isFileExist(filePath)) {
            ZFileUtil.INSTANCE.createFileNotExists(filePath);
            isNew = true;
        }
        Properties properties = ZPropertiesUtil.load(filePath);
        properties.setProperty(key, value);
        if (isNew) {
            properties.setProperty(KEY_CREATE_TIME, String.valueOf(System.currentTimeMillis()));
        }
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
        try {
            ZFileUtil.INSTANCE.createFileNotExists(filePath);
        } catch (Exception e) {
            ZLog.e(ZTag.TAG_ERROR, e.getMessage());
            e.printStackTrace();
        }
        Properties properties = ZPropertiesUtil.load(filePath);
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
        try {
            ZFileUtil.INSTANCE.createFileNotExists(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Properties properties = ZPropertiesUtil.load(filePath);
        properties.remove(key);
        ZPropertiesUtil.store(filePath, properties);
    }

    // --------------------------------------------------

    /**
     * 加载配置信息
     *
     * @return String value
     */
    public String getProperty(String key) {
        return ZPropertiesUtil.getProperty(filePath, key);
    }

    // --------------------------------------------------

    /**
     * 加载配置信息
     *
     * @return Properties
     */
    public Properties load() {
        File file = new File(filePath);
        if (file.exists()) {
            return ZPropertiesUtil.load(filePath);
        } else {
            return new Properties();
        }
    }

}
