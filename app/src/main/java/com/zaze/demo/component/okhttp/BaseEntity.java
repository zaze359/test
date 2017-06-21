package com.zaze.demo.component.okhttp;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-06-15 - 14:17
 */
public class BaseEntity {

    private int M;
    private String C;
    private String S;


    public BaseEntity() {
        M = 1;
        C = "1000";
        S = "1";
    }

    public int getM() {
        return M;
    }

    public void setM(int m) {
        M = m;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getS() {
        return S;
    }

    public void setS(String s) {
        S = s;
    }
}
