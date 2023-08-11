//
// Created by 35963 on 2023/7/31.
//
#include <jni.h>
#include <string>
#include <android/bitmap.h>
#include <malloc.h>
#include "common/log.h"
#include <jpeglib.h>
#include <setjmp.h>

typedef u_int8_t COLOR;

struct my_error_mgr {
    struct jpeg_error_mgr pub;
    jmp_buf jmp_env;
};

// 定义一个指针类型
typedef struct my_error_mgr *my_error_ptr;

// 定义jpeg 错误处理函数，默认是 退出进程。
METHODDEF(void) my_error_exit(j_common_ptr commonPtr) {
    my_error_ptr errorPtr = (my_error_ptr) commonPtr->err;
    (*commonPtr->err->output_message)(commonPtr);
    ALOGI("--------- my_error_exit(%d) %s", errorPtr->pub.msg_code, errorPtr->pub.jpeg_message_table[errorPtr->pub.msg_code]);
//    ALOGI(errorPtr->pub.jpeg_message_table[errorPtr->pub.msg_code], errorPtr->pub.msg_parm.i[0], errorPtr->pub.msg_parm.i[1]);
    // 直接跳回到 setjmp处，返回1，，后续代码不会被执行。
    longjmp(errorPtr->jmp_env, 1);
}


int jpegCompress(COLOR *data, uint32_t width, uint32_t height, jint quality, const char *out_path,
                 jboolean optimize) {
    ALOGI("--------- jpegCompress %s", "---------");
    // jpeg 压缩 配置。
    jpeg_compress_struct jcs;
    // 定义一个错误函数，发生错误时回调。
    my_error_mgr jem;
    jcs.err = jpeg_std_error(&jem.pub);
    // 设置错误处理函数
    jem.pub.error_exit = my_error_exit;
    // 创建本地的jmp_buf缓冲区,用于将来跳转回此处
    // 第一返回 0，后续通过 longjmp 返回 1
    if (setjmp(jem.jmp_env)) {
        // 发生错误后回调，直接中断返回。
        return 0;
    }
    // 正常流程，初始化 jpeg_compress_struct
    jpeg_create_compress(&jcs);
    // 打开文件
    FILE *file = fopen(out_path, "wb");
    if (file == NULL) {
        ALOGI("--------- file open error -----------");
        return 0;
    }
    // 添加 输出路径
    jpeg_stdio_dest(&jcs, file);
    // 设置 图像宽高。
    jcs.image_width = width;
    jcs.image_height = height;

    // 设置颜色通道， 1灰度图，3彩色位图
    jcs.input_components = 3;
    // 颜色空间 RGB
    jcs.in_color_space = JCS_RGB;

    // false 表示开启 huffman 算法
    jcs.arith_code = false;
    // true 计算哈夫曼表。
    jcs.optimize_coding = optimize;
    // 添加 默认配置
    jpeg_set_defaults(&jcs);
    // 设置 压缩比。
    jpeg_set_quality(&jcs, quality, true);

    // 开始压缩
    ALOGI("--------- jpeg_start_compress -----------");
    jpeg_start_compress(&jcs, true);
    // 一行的字节数量，
    int row_byte_count = jcs.image_width * jcs.input_components;
    // 逐行扫描压缩, row_ptr 表示指向 一行图像像素数据样本的 首地址。
    JSAMPROW row_ptr[1];
    while (jcs.next_scanline < jcs.image_height) {
        // 赋值为 每行首地址。
        row_ptr[0] = &data[jcs.next_scanline * row_byte_count];
        jpeg_write_scanlines(&jcs, row_ptr, 1);
    }
    ALOGI("--------- jpeg_finish_compress -----------");
    // 结束压缩
    jpeg_finish_compress(&jcs);
    // 释放资源
    jpeg_destroy_compress(&jcs);
    fclose(file);
    return 1;
}


extern "C"
JNIEXPORT jint JNICALL
Java_com_zaze_core_nativelib_MyJpegCompressor_compress(JNIEnv *env, jobject thiz, jobject bitmap,
                                                       jint quality, jstring out_path,
                                                       jboolean optimize) {
    const char *c_out_file = env->GetStringUTFChars(out_path, 0);

    AndroidBitmapInfo bitmapInfo;

    int ret;
    // 获取 bitmap 信息
    if ((ret = AndroidBitmap_getInfo(env, bitmap, &bitmapInfo)) < 0) {
        // 无法获取到信息。
        ALOGI("AndroidBitmap_getInfo() error(%d)", ret);
        return ret;
    }
    COLOR *pixelsColor;
    // 获取 bitmap 颜色像素。
    if ((ret = AndroidBitmap_lockPixels(env, bitmap, (void **) &pixelsColor))) {
        ALOGI("AndroidBitmap_lockPixels() error(%d)", ret);
        return ret;
    }
    // 从上往下，从左到右，遍历所有像素点
    int color;
    COLOR r, b, g;
    // 申请一个内存，存储转换后的像素，仅需要RGB即可。
    auto *data = (COLOR *) malloc(bitmapInfo.height * bitmapInfo.width * 3);
    // 创建一个临时指针，指向 刚分配的内存地址，因为后续回操作 会修改data
    auto *temp = data;
//    ALOGI("--------- malloc %d %s", (bitmapInfo.height * bitmapInfo.width * 3), "---------");
    for (int i = 0; i < bitmapInfo.height; i++) { // 行
        for (int j = 0; j < bitmapInfo.width; j++) { // 列
            if (bitmapInfo.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
                // 色值存储位置是 ABGR，高位是透明通道。
                // 以int 来读取数据，方便一次读取四个字节
                color = *(int *) pixelsColor;
                b = (color >> 16) & 0xFF;
                g = (color >> 8) & 0xFF;
                r = color & 0xFF;
                // 依次保存 rgb
                *data = r;
                *(data + 1) = g;
                *(data + 2) = b;
                // data内存地址移动 3字节
                data += 3;
                // pixelsColor 内存地址移动 4字节
                pixelsColor += 4;
            } else {
                ret = -2;
                goto Done;
            }
        }
    }

    Done:;
    ALOGI("--------- AndroidBitmap_unlockPixels %s", "---------");

    AndroidBitmap_unlockPixels(env, bitmap);
    // 交由 libjpeg 压缩像素数据。
    if (ret >= 0) {
        jpegCompress(temp, bitmapInfo.width, bitmapInfo.height, quality, c_out_file, optimize);
    }
    free(temp);
    temp = NULL;
    env->ReleaseStringUTFChars(out_path, c_out_file);

    return 0;
}
