#include <jni.h>
#include <string>
#include <android/log.h>

extern "C" {
#include "./bsdiff/bspatch.h"
#include "./bsdiff/bsdiff.h"
}

#define LOG_TAG "zaze.bsdiff"
#define ALOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
#define ALOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define ALOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define ALOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define ALOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_zaze_demo_core_bsdiff_NativeLib_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    ALOGD("stringFromJNI: %s", "Hello from C++");
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_zaze_demo_core_bsdiff_AppPatchUtils_applyPatch(JNIEnv *env, jobject thiz,
                                                        jstring old_apk_path,
                                                        jstring new_apk_path,
                                                        jstring patch_path) {

    const char *oldPath = env->GetStringUTFChars(old_apk_path, NULL);
    const char *patchPath = env->GetStringUTFChars(patch_path, NULL);
    const char *newPath = env->GetStringUTFChars(new_apk_path, NULL);
    ALOGD("applyPatch: %s", "bspatch start");
    int argc = 4;
    const char *argv[argc];
    argv[0] = "bspatch";
    argv[1] = const_cast<char *>(env->GetStringUTFChars(old_apk_path, NULL));
    argv[2] = const_cast<char *>(env->GetStringUTFChars(new_apk_path, NULL));
    argv[3] = const_cast<char *>(env->GetStringUTFChars(patch_path, NULL));
    int result = patchMain(argc, argv);
    ALOGD("applyPatch: %s", "bspatch end");

    env->ReleaseStringUTFChars(old_apk_path, oldPath);
    env->ReleaseStringUTFChars(patch_path, patchPath);
    env->ReleaseStringUTFChars(new_apk_path, newPath);
    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_zaze_demo_core_bsdiff_AppPatchUtils_diff(JNIEnv *env, jobject thiz, jstring old_apk_path,
                                                  jstring new_apk_path, jstring patch_path) {
    const char *oldPath = env->GetStringUTFChars(old_apk_path, NULL);
    const char *patchPath = env->GetStringUTFChars(patch_path, NULL);
    const char *newPath = env->GetStringUTFChars(new_apk_path, NULL);

    int argc = 4;
    const char *argv[argc];
    argv[0] = "bsdiff";
    argv[1] = const_cast<char *>(env->GetStringUTFChars(old_apk_path, NULL));
    argv[2] = const_cast<char *>(env->GetStringUTFChars(new_apk_path, NULL));
    argv[3] = const_cast<char *>(env->GetStringUTFChars(patch_path, NULL));
    int result = diffMain(argc, argv);
    env->ReleaseStringUTFChars(old_apk_path, oldPath);
    env->ReleaseStringUTFChars(patch_path, patchPath);
    env->ReleaseStringUTFChars(new_apk_path, newPath);
    return result;
}