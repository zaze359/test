package com.zaze.aarrepo.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Description : 配置信息
 *
 * @author : zaze
 * @version : 2015-11-02 - 16:07
 */
public class PropertiesUtil {

    public static Properties load(String file) {
        Properties properties = new Properties();
        try {
            FileInputStream inputStream = new FileInputStream(file);
            properties.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static void store(String file, Properties properties) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file, false);
            properties.store(outputStream, "");
            outputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
