package com.zaze.aarrepo.utils.helper;


import com.zaze.aarrepo.utils.FileUtil;
import com.zaze.aarrepo.utils.PropertiesUtil;
import com.zaze.aarrepo.utils.StringUtil;

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
    String BASE_PATH = FileUtil.getSDCardRoot();
    private String filePath;

    public static ConfigHelper newInstance(String account) {
        return new ConfigHelper(account);
    }

    public ConfigHelper(String account) {
        String str = "";
        if (!StringUtil.stringIsNull(account)) {
            str = account + File.separator;
        }
        filePath = BASE_PATH + "config/" + str + "gw.properties";
    }


    /**
     * 保存配置信息
     *
     * @param key
     * @param state
     */
    public void store(Object key, Object state) {
        try {
            FileUtil.createFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    // -------------------------------------------
    public static class ConfigKey {
        public static final String DEFAULT_CLASS_LOCK = "default_class_lock";   // 默认班级锁 true 锁定， false 未锁定
        public static final String DEFAULT_CLASS_IDS = "default_class_ids";     // 默认班级   ,分割
        public static final String DEFAULT_BOOK = "default_book";               // 默认书
    }
}
