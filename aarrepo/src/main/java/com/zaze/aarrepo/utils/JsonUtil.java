package com.zaze.aarrepo.utils;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class JsonUtil {
    public static <T> T parseJson(String json, Class<T> classOfT) {
        if (json == null || classOfT == null) {
            return null;
        }
        Gson gson = new Gson();
        json = decode(json);
        return gson.fromJson(json, classOfT);
    }

    public static <T> String objToJson(T t) {
        if (t == null) {
            return null;
        }
        Gson gson = new Gson();
        try {
            return gson.toJson(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析json列表字符串
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> parseJsonToList(String json, Type type) {
        List<T> list = parseJsonToListHasNull(json, type);
        if (list != null) {
            Collection<T> collection = new Vector<>();
            collection.add(null);
            list.removeAll(collection);
        }
        return list;
    }

    /**
     * 解析json列表字符串, 列表内可以包含空对象
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> parseJsonToListHasNull(String json, Type type) {
        if (json == null || type == null) {
            return null;
        }
        try {
            List<T> retObj = null;
            Gson gson = new Gson();
            json = decode(json);
            return gson.fromJson(json, type);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> JSONArray toJsonArray(Collection<T> collection) {
        JSONArray jsonArray = new JSONArray();
        if (collection != null) {
            for (T t : collection) {
                jsonArray.put(t);
            }
        }
        return jsonArray;
    }

    public static <T> JSONArray toJsonArray(T[] arrays) {
        JSONArray jsonArray = new JSONArray();
        if (arrays != null) {
            for (T t : arrays) {
                jsonArray.put(t);
            }
        }
        return jsonArray;
    }

    private static String decode(String json) {
        try {
            json = URLDecoder.decode(json, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}