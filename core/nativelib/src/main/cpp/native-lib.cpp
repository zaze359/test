#include <jni.h>
#include <string>
//#include <android/log.h>
#include "jvmti.h"
#include "common/log.h"

//#define LOG_TAG "jvmti"

//#define ALOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
//#define ALOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
//#define ALOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
//#define ALOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
//#define ALOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)



//extern "C" JNIEXPORT jstring JNICALL
//dddd(JNIEnv *env, jobject  /* this */) {
//    std::string hello = "Hello from C++";
//    return env->NewStringUTF(hello.c_str());
//}
//
//static const JNINativeMethod gMethods[] = {
//        {"test", "()Ljava/lang/String", (void *) dddd}
//};
//
//jint JNI_onLoad(JavaVM *vm, void *reserved) {
//    JNIEnv *env = NULL;
//    int result = -1;
//    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) == JNI_OK) {
//        jclass class_lib = env->FindClass("com.zaze.core.nativelib.MyNativeLib");
//        if (env->RegisterNatives(class_lib, gMethods, sizeof(gMethods) / sizeof(gMethods[0])) ==
//            JNI_OK) {
//            result = JNI_VERSION_1_4;
//        }
//    }
//    return result;
//}

jvmtiEnv *localJvmtiEnv = NULL;

// 创建 JvmtiEvn
jvmtiEnv *CreateJvmtiEnv(JavaVM *vm) {
    jvmtiEnv *jvmti_env;
    jint result = vm->GetEnv((void **) &jvmti_env, JVMTI_VERSION_1_2);
    if (result != JNI_OK) {
        return NULL;
    }
    return jvmti_env;
}

// 开启 JVM TI的功能
void SetAllCapabilities(jvmtiEnv *jvmti) {
    jvmtiCapabilities caps;
    jvmti->GetPotentialCapabilities(&caps);
    jvmti->AddCapabilities(&caps);
}

void SetEventNotification(jvmtiEnv *jvmti, jvmtiEventMode mode,
                          jvmtiEvent event_type) {
    jvmtiError err = jvmti->SetEventNotificationMode(mode, event_type, NULL);
}

void EnableEventNotification(jvmtiEnv *jvmti) {
    SetEventNotification(jvmti, JVMTI_ENABLE, JVMTI_EVENT_VM_OBJECT_ALLOC);
    SetEventNotification(jvmti, JVMTI_ENABLE, JVMTI_EVENT_METHOD_ENTRY);
}

void DisableEventNotification(jvmtiEnv *jvmti) {
    SetEventNotification(jvmti, JVMTI_DISABLE, JVMTI_EVENT_VM_OBJECT_ALLOC);
    SetEventNotification(jvmti, JVMTI_DISABLE, JVMTI_EVENT_METHOD_ENTRY);
}

//
// 获取线程信息
jvmtiThreadInfo GetThreadInfo(jvmtiEnv *jvmti_env, jthread thread) {
    jvmtiThreadInfo threadInfo;
    jvmti_env->GetThreadInfo(thread, &threadInfo);
    return threadInfo;
}

//
jlong obj_tag = 0;

// 监听对象创建
void JNICALL ObjectAllocCallback(jvmtiEnv *jvmti_env,
                                 JNIEnv *jni_env,
                                 jthread thread,
                                 jobject object,
                                 jclass object_klass,
                                 jlong size) {
    obj_tag++;
    jvmti_env->SetTag(object, obj_tag);
    // 获取线程信息
    jvmtiThreadInfo threadInfo = GetThreadInfo(jvmti_env, thread);
    // 获取签名
    char *classSignature;
    jvmti_env->GetClassSignature(object_klass, &classSignature, NULL);
    ALOGI("========== ObjectAlloc callback ======= %s >> %s(%d) {size:%d}", threadInfo.name,
          classSignature, obj_tag, size);
    jvmti_env->Deallocate(reinterpret_cast<unsigned char *>(classSignature));

    // 获取类名
//    jclass cls = jni_env->FindClass("java/lang/Class");
//    jmethodID getName_method = jni_env->GetMethodID(cls, "getName", "()Ljava/lang/String;");
//    jstring nameStr = static_cast<jstring>(jni_env->CallObjectMethod(object_klass, getName_method));
//    const char *classname = jni_env->GetStringUTFChars(nameStr, 0);
//    ALOGI("========== ObjectAlloc callback ======= %s(%d) {size:%d}", classname, obj_tag, size);
//    jni_env->ReleaseStringUTFChars(nameStr, classname);
}

// 监听 方法执行
void JNICALL MethodEntryCallback(jvmtiEnv *jvmti_env,
                                 JNIEnv *jni_env,
                                 jthread thread,
                                 jmethodID method) {
    // 获取类
    jclass klass;
    jvmti_env->GetMethodDeclaringClass(method, &klass);
    // 类签名
    char *classSignature;
    jvmti_env->GetClassSignature(klass, &classSignature, NULL);
    // 方法名
    char *methodName;
    jvmti_env->GetMethodName(method, &methodName, NULL, NULL);

//    ALOGI("========== MethodEntry callback ======= %s.%s", classSignature, methodName);

    char str[500];
    char *format = "========== MethodEntry callback ======= %s.%s";
    sprintf(str, format, classSignature, methodName);
    ALOGI("%s", str);

    jvmti_env->Deallocate(reinterpret_cast<unsigned char *>(classSignature));
    jvmti_env->Deallocate((unsigned char *) (methodName));
}

// 初始化，当 Agent 和 JVM TI 关联时 会被自动调用。
extern "C" JNIEXPORT jint JNICALL Agent_OnAttach(JavaVM *vm, char *options, void *reserved) {
    ALOGI("%s", "========== Agent_OnAttach =======");
    jvmtiEnv *jvmti_env = CreateJvmtiEnv(vm);
    if (localJvmtiEnv) {
        DisableEventNotification(localJvmtiEnv);
    }
    localJvmtiEnv = jvmti_env;
    SetAllCapabilities(jvmti_env);
    EnableEventNotification(jvmti_env);
    return JNI_OK;
}



extern "C" JNIEXPORT jstring JNICALL
Java_com_zaze_core_nativelib_MyJvmtiAgent_test(JNIEnv *env, jobject  /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_zaze_core_nativelib_MyJvmtiAgent_release(JNIEnv *env, jobject thiz) {
    ALOGI("%s", "========== Agent_release =======");
    if (localJvmtiEnv) {
        DisableEventNotification(localJvmtiEnv);
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_zaze_core_nativelib_MyJvmtiAgent_init(JNIEnv *env, jobject thiz) {
    ALOGI("%s; %s", "========== Agent_init =======", localJvmtiEnv);
    if (localJvmtiEnv) {
        // 创建 JVM 监听回调。
        jvmtiEventCallbacks callbacks;
        callbacks.VMObjectAlloc = &ObjectAllocCallback; // 监听对象创建
        callbacks.MethodEntry = &MethodEntryCallback; // 监听 方法执行
        // 设置事件监听 回调
        int error = localJvmtiEnv->SetEventCallbacks(&callbacks, sizeof(callbacks));
        // 开启事件通知
        EnableEventNotification(localJvmtiEnv);
    }
}
