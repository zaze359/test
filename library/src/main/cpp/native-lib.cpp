#include <jni.h>
#include <string>

extern "C"
jstring Java_com_zz_library_jni_TestJni_stringFromJNI(JNIEnv *env, jobject) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
