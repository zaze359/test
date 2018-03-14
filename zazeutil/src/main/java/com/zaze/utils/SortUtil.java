package com.zaze.utils;

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
     * @param sort   排序方式 默认升序
     * @param <E>    entity
     */
    public static <E> void sortList(List<E> list, final String method, final String sort) {
        // >0 交换位置
        Collections.sort(list, new Comparator<E>() {
            // return <= 0 : 不交换位置
            // return > 0 : 交换位置;
            @Override
            public int compare(Object lhs, Object rhs) {
                int flag = 0;
                try {
                    String leftValue = lhs.getClass().getMethod(method).invoke(lhs, new Object[]{}).toString();
                    String rightValue = rhs.getClass().getMethod(method).invoke(rhs, new Object[]{}).toString();
                    if (sort != null && SortUtil.DESC.equalsIgnoreCase(sort)) {
                        //若第二个大于第一个, 返回 > 0, 交换位置
                        flag = rightValue.compareTo(leftValue);
                    } else {
                        //若第一个大于第二个, 则返回 > 0 交换位置
                        flag = leftValue.compareTo(rightValue);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                return flag;
            }
        });
    }

}
