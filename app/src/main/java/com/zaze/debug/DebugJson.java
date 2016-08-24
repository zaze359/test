package com.zaze.debug;

import com.google.gson.reflect.TypeToken;
import com.zz.library.util.JsonUtil;

import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-06-03 - 14:09
 */
public class DebugJson {
    public static void main(String args[]) {
        String aStr = "[{a:1, b:3, c:2}, {a:4, b:4, c:4}]";
        List<A> list = JsonUtil.parseJsonToList(aStr, new TypeToken<List<A>>(){}.getType());
        System.out.println("" + list);
    }
    public class A {
        int c;
    }
}
