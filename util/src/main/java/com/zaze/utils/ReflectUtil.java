package com.zaze.utils;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-07-27 - 16:44`
 */
public class ReflectUtil {
    public static boolean showLog = false;

    private static final String TAG = "ReflectUtil";

    /**
     * 反射执行方法
     * 需要注意 null 也是参数
     */
    public static Object executeMethodByName(String classPath, String functionName, Object... args) throws Exception {
        if (TextUtils.isEmpty(classPath) || TextUtils.isEmpty(functionName)) {
            return null;
        }
        return executeMethod(Class.forName(classPath), null, functionName, args);

    }

    /**
     * 反射执行方法
     */
    public static Object executeMethod(Object self, String functionName, Object... args) throws Exception {
        if (self == null) {
            return null;
        }
        return executeMethod(self.getClass(), self, functionName, args);
    }

    public static @Nullable Object executeMethodNoExp(Object self, String functionName, Object... args) {
        try {
            return executeMethod(self, functionName, args);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    private static Object executeMethod(Class<?> clazz, Object receiver, String functionName, Object... args) throws Exception {
        if (showLog) {
            Log.d(TAG, "functionName : " + functionName);
        }
        Class<?>[] classes = null;
        if (args != null && args.length > 0) {
            classes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                classes[i] = args[i].getClass();
                classes[i] = dealPrimitive(classes[i]);
                if (classes[i].isPrimitive()) {
                    classes[i] = dealPrimitive(classes[i]);
                }
                if (showLog) {
                    Log.d(TAG, "clazz[" + i + "] " + classes[i]);
                }
            }
        }
        Method method = clazz.getDeclaredMethod(functionName, classes);
        method.setAccessible(true);
        return method.invoke(receiver, args);
    }


    public static Object getFieldValue(Object self, String field) throws Exception {
        return getField(self.getClass(), field).get(self);
    }
    public static @Nullable Object getFieldValueNoExp(Object self, String field) {
        try {
            return getField(self.getClass(), field).get(self);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Field getField(Class<?> clazz, String field) throws Exception {
        return clazz.getField(field);
    }

    public static void setFieldValue(@NonNull Object obj, String fieldName, Object value) throws Exception {
        setFieldValue(obj, obj.getClass(), fieldName, value);
    }

    public static void setFieldValue(@Nullable Object obj, Class<?> clazz, String fieldName, Object value) throws Exception {
        if (showLog) {
            Log.d(TAG, "setFieldValue fieldName: " + fieldName);
        }
        Field field = getField(clazz, fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    /**
     * 处理8种基础类型的反射
     */
    private static Class<?> dealPrimitive(Class<?> clazz) {
        if (Integer.class.equals(clazz)) {
            return int.class;
        } else if (Boolean.class.equals(clazz)) {
            return boolean.class;
        } else if (Long.class.equals(clazz)) {
            return long.class;
        } else if (Short.class.equals(clazz)) {
            return short.class;
        } else if (Float.class.equals(clazz)) {
            return float.class;
        } else if (Double.class.equals(clazz)) {
            return double.class;
        } else if (Byte.class.equals(clazz)) {
            return byte.class;
        } else if (Character.class.equals(clazz)) {
            return char.class;
        } else {
            return clazz;
        }
    }

}
