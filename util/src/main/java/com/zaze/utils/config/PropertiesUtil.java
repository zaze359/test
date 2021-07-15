package com.zaze.utils.config;

import android.text.TextUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Description : 配置信息
 *
 * @author : zaze
 * @version : 2015-11-02 - 16:07
 */
public class PropertiesUtil {

    public static final String CREATE_TIME_KEY = "create_time";
    private static final String VERSION_KEY = "version_key";
    private static final String VERSION_VALUE = "1";

    /**
     * 加载
     *
     * @param file
     * @return
     */
    public static Properties load(String file) {
        Properties properties = new Properties();
        if (!TextUtils.isEmpty(file)) {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
                properties.load(inputStream);
            } catch (FileNotFoundException e) {
                //
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return properties;
    }

    public static String getProperty(String file, String key) {
        if (!TextUtils.isEmpty(file) && !TextUtils.isEmpty(key)) {
            Properties properties = load(file);
            return properties.getProperty(key);
        } else {
            return null;
        }
    }


    /**
     * 保存
     *
     * @param file       file
     * @param properties properties
     */
    public static void store(String file, Properties properties) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file, false);
            properties.put(VERSION_KEY, VERSION_VALUE);
            properties.store(outputStream, "");
        } catch (FileNotFoundException e) {
            //
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
