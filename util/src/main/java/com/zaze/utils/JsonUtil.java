package com.zaze.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class JsonUtil {

    private static GsonBuilder gsonBuilder;

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
            return create().fromJson(json, classOfT);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * bean 转换为 jsonStr
     *
     * @param obj Object
     * @return jsonStr
     */
    public static String objToJson(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof JSONObject || obj instanceof JSONArray) {
            return obj.toString();
        } else {
            try {
                return create().toJson(obj);
            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * 解析json列表字符串（去除了内部空对象）
     *
     * @param json json
     * @param clazz 泛型T的类型
     * @return List
     */
    public static <T> List<T> parseJsonToList(String json, final Class<T> clazz) {
        return parseJsonToList(json, new ParameterizedType() {
            @NotNull
            @Override
            public Type[] getActualTypeArguments() {
                // 实际类型参数：这里指泛型 T 的真实类型。
                return new Class[]{clazz};
            }

            @NotNull
            @Override
            public Type getRawType() {
                // 原始类型，List
                return List.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        });
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
            return create().fromJson(json, type);
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
                        } catch (Throwable e) {
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

    public static Gson create() {
        if (gsonBuilder == null) {
            gsonBuilder = new GsonBuilder();
        }
        return gsonBuilder.create();
    }


    /**
     * mapToJson
     *
     * @return JSONObject
     */
    public static JSONObject mapToJson(Map<String, Object> map) {
        JSONObject jsonHeadLines = new JSONObject();
        try {
            for (String key : map.keySet()) {
                jsonHeadLines.put(key, map.get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonHeadLines;
    }

    public static <T> List<T> parseJsonToListByClass(String json, final Class<T> clazz) {
        return JsonUtil.parseJsonToList(json, new ParameterizedType() {

            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{clazz};
            }

            @Override
            public Type getRawType() {
                return List.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        });
    }

}
