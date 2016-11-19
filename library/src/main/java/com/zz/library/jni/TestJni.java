package com.zz.library.jni;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-11-10 - 14:25
 */
public class TestJni {
    public static TestJni newInstance() {
        return new TestJni();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}
