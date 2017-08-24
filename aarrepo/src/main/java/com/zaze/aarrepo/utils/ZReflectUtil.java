package com.zaze.aarrepo.utils;


import com.zaze.aarrepo.commons.log.ZLog;

import java.lang.reflect.Method;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-07-27 - 16:44
 */
public class ZReflectUtil {
    /**
     * 反射执行方法
     */
    public static Object executeMethod(Object self, String functionName, Object... args) throws Exception {
        if (self == null) {
            return null;
        }
        ZLog.d(ZTag.TAG_DEBUG, "functionName : " + functionName);
        Class<?>[] classes = null;
        if (args != null && args.length > 0) {
            classes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                classes[i] = args[i].getClass();
                classes[i] = dealPrimitive(classes[i]);
                if (classes[i].isPrimitive()) {
                    classes[i] = dealPrimitive(classes[i]);
                }
                ZLog.d(ZTag.TAG_DEBUG, "clazz[" + i + "] " + classes[i]);
            }
        }
        Method method = self.getClass().getDeclaredMethod(functionName, classes);
        return method.invoke(self, args);
    }

    /**
     * 处理8种基础类型的反射
     */
    private static Class<?> dealPrimitive(Class<?> clazzs) {
        if (Integer.class.equals(clazzs)) {
            return int.class;
        } else if (Boolean.class.equals(clazzs)) {
            return boolean.class;
        } else if (Long.class.equals(clazzs)) {
            return long.class;
        } else if (Short.class.equals(clazzs)) {
            return short.class;
        } else if (Float.class.equals(clazzs)) {
            return float.class;
        } else if (Double.class.equals(clazzs)) {
            return double.class;
        } else if (Byte.class.equals(clazzs)) {
            return byte.class;
        } else if (Character.class.equals(clazzs)) {
            return char.class;
        } else {
            return clazzs;
        }
    }

}
