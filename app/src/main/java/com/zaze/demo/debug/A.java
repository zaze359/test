package com.zaze.demo.debug;


import javax.annotation.Nullable;

public class A {

    protected @Nullable String a(String a) {
        BKt.topB(null);
        return a;
    }


    protected void aa() {

    }
}

class AA {
    public void aaaa() {
        A a = new A();
        a.aa();
    }
}
