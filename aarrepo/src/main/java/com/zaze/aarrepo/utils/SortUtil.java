package com.zaze.aarrepo.utils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * description : 排序工具类
 *
 * @author : zaze
 * @version : 2015年1月9日 上午11:06:13
 */
public class SortUtil {
    public static final String DESC = "desc";
    public static final String ASC = "asc";

    /**
     * @param list   列表
     * @param method get方法
     * @param sort   排序方式
     * @param <E>    entity
     */
    public static <E> void sortList(List<E> list, final String method, final String sort) {
        Collections.sort(list, new Comparator<E>() {
            // return < 0 : 不交换位置
            // return = 0 : 不交换位置;
            // return > 0 : 交换位置;
            @Override
            public int compare(Object lhs, Object rhs) {
                int flag = 0;
                try {
                    Method m1 = lhs.getClass().getMethod(method, new Class<?>[0]);
                    Method m2 = rhs.getClass().getMethod(method, new Class<?>[0]);
                    if (sort != null && DESC.equalsIgnoreCase(sort)) {
                        // 降序
                        //若第二个大于第一个, 则返回 > 0 交换位置
                        flag = m2.invoke(rhs, new Object[]{}).toString().compareTo(m1.invoke(lhs, new Object[]{}).toString());
                    } else {
                        // 升序
                        //若第一个大于第二个, 则返回 > 0 交换位置
                        flag = m1.invoke(rhs, new Object[]{}).toString().compareTo(m2.invoke(lhs, new Object[]{}).toString());
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                return flag;
            }
        });
    }

}
