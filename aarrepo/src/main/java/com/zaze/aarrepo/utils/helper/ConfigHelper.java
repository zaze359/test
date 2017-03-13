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
     * 保存配置信息
     *
     * @param key
     * @param state
     */
    public void store(Object key, Object state) {
        if (key == null || state == null) {
            return;
        }
        try {
            FileUtil.createFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        LogKit.d("filePath : " + filePath);
        Properties properties = PropertiesUtil.load(filePath);
        properties.put(key, state);
        PropertiesUtil.store(filePath, properties);
    }

    /**
     * 保存配置信息
     *
     * @param map
     */
    public void storeAll(Map map) {
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

    /**
     * 加载配置信息
     *
     * @return
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
