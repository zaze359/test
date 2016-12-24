package com.zaze.aarrepo.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author : zaze
 * @version : 2015年1月9日 上午11:06:13
 * @Description : 排序工具类
 */
public class SortUtil {

    public static <E> void sortList(List<E> list, final String method, final String sort) {
        Collections.sort(list, new Comparator<E>() {
            // return < 0 : 不交换位置
            // return = 0 : 不交换位置;
            // return > 0 : 交换位置;
            @Override
            public int compare(Object lhs, Object rhs) {
                int flag = 0;
                try {
                    Method m1 = ((E) lhs).getClass().getMethod(method, new Class<?>[0]);
                    Method m2 = ((E) rhs).getClass().getMethod(method, new Class<?>[0]);
                    if (sort != null && "desc".equalsIgnoreCase(sort)) {
                        // 降序
                        //若第二个大于第一个, 则返回 > 0 交换位置
                        flag = m2.invoke(((E) rhs), new Object[]{}).toString().compareTo(m1.invoke(((E) lhs), new Object[]{}).toString());
                    } else {
                        // 升序
                        //若第一个大于第二个, 则返回 > 0 交换位置
                        flag = m1.invoke(((E) rhs), new Object[]{}).toString().compareTo(m2.invoke(((E) lhs), new Object[]{}).toString());
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                return flag;
            }
        });
    }

}
