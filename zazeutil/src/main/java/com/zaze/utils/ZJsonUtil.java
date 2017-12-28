package com.zaze.utils;


import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Arrays;
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
public class ZJsonUtil {
    /**
     * json String 转换为bean
     *
     * @param json     json字符串
     * @param classOfT 转换bean
     * @param <T>      T
     * @return T
     */
    public static <T> T parseJson(String json, Class<T> classOfT) {
        if (json == null || classOfT == null) {
            return null;
        }
        try {
            return new Gson().fromJson(json, classOfT);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * bean 转换为 jsonStr
     *
     * @param t   bean
     * @param <T> T
     * @return jsonStr
     */
    public static <T> String objToJson(T t) {
        if (t == null) {
            return null;
        }
        if (t instanceof JSONObject || t instanceof JSONArray) {
            return t.toString();
        } else {
            try {
                Gson gson = new Gson();
                return gson.toJson(t);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * 解析json列表字符串（去除了内部空对象）
     *
     * @param json json
     * @param type json : new TypeToken<List<T>>(){}.getType()
     * @param <T>  T
     * @return List
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
     * 解析json列表字符串, List内部可以为空
     *
     * @param json json
     * @param type type
     * @param <T>  T
     * @return List
     */
    public static <T> List<T> parseJsonToListHasNull(String json, Type type) {
        if (json == null || type == null) {
            return null;
        }
        try {
            return new Gson().fromJson(json, type);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> parseJsonArrayToList(JSONArray jsonArray, Type type) {
        return parseJsonToList(jsonArray.toString(), type);
    }

    // --------------------------------------------------


    // --------------------------------------------------

    /**
     * Collection 转换为 JSONArray
     *
     * @param collection collection
     * @param <T>        T
     * @return JSONArray
     */
    public static <T> JSONArray toJsonArray(Collection<T> collection) {
        JSONArray jsonArray = new JSONArray();
        if (collection != null) {
            for (T t : collection) {
                if (t != null) {
                    if (t instanceof JSONObject ||
                            t instanceof JSONArray ||
                            t instanceof Boolean ||
                            t instanceof Byte ||
                            t instanceof Character ||
                            t instanceof Double ||
                            t instanceof Float ||
                            t instanceof Integer ||
                            t instanceof Long ||
                            t instanceof Short ||
                            t instanceof String) {
                        jsonArray.put(t);
                    } else {
                        try {
                            jsonArray.put(new JSONObject(objToJson(t)));
                        } catch (JSONException e) {
                            jsonArray.put(t);
                        }
                    }
                }
            }
        }
        return jsonArray;
    }

    /**
     * 数组转换为JSONArray
     *
     * @param arrays 数组
     * @param <T>    T
     * @return JSONArray
     */
    public static <T> JSONArray toJsonArray(T[] arrays) {
        return toJsonArray(Arrays.asList(arrays));
    }

    // --------------------------------------------------
}
