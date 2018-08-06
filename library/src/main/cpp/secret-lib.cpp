//
// Created by  赵臻 on 2018/7/19.
//
#include <jni.h>
#include <string>

extern "C"
jstring Java_com_zz_library_jni_TestJni_verifyKey(JNIEnv *env, jobject) {
    std::string hello = "Hello from C++";
//    int *p = 0;
//    *p = 1;
    return env->NewStringUTF(hello.c_str());
}

