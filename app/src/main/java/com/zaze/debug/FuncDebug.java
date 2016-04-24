package com.zaze.debug;

/**
 * Description :
 * date : 2016-04-21 - 12:26
 *
 * @author : zaze
 * @version : 1.0
 */
public class FuncDebug {
    private static int position = 0;
    protected static void out(String tag, String result) {
        printHead(tag);
        outTag("result", result);

    }
    protected static void out(String func, String tag[], String result[]) {
        printHead(func);
        for (int i = 0; i < tag.length; i ++) {
            outTag(tag[i], result[i]);
        }

    }
    protected static void outTag(String tag, String result) {
        System.out.println(String.format("<%s = %s>", tag, result));
    }

    private static void printHead(String tag) {
        position ++;
        System.out.println(String.format("\n%s. %s", position, tag));
//        System.out.println(String.format("\n%s.<!--   %s   -->", position, tag));
    }
}
