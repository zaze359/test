package com.zaze.debug;

/**
 * Description :
 * date : 2016-04-21 - 12:26
 *
 * @author : zaze
 * @version : 1.0
 */
public class FuncDebug {

    protected static void out(String tag, String result) {
        System.out.println("\n---------------------------------------------");
        System.out.println(String.format("<!--   %s   -->", tag));
        outTag("result", result);
        System.out.println("---------------------------------------------");

    }
    protected static void out(String func, String tag[], String result[]) {
        System.out.println("\n---------------------------------------------");
        System.out.println(String.format("<!--   %s   -->", func));
        for (int i = 0; i < tag.length; i ++) {
            outTag(tag[i], result[i]);
        }
        System.out.println("---------------------------------------------");

    }
    protected static void outTag(String tag, String result) {
        System.out.println(String.format("<%s = %s>", tag, result));
    }
}
