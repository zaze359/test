package com.zaze.aarrepo.utils;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-03-24 - 00:56
 */
public interface ZTag {
    String TAG_BASE = "[ZAZE]";
    String TAG_ERROR = "ERROR[错误]" + TAG_BASE;
    String TAG_DEBUG = "Debug[调试]" + TAG_BASE;
    String TAG_MEMORY = "MemoryCache[内存]" + TAG_BASE;
    String TAG_TASK = "Task[任务]" + TAG_BASE;
    String TAG_ABOUT_APP = "AboutApp[应用相关]" + TAG_BASE;
    String TAG_PROVIDER = "ContentProvider[]" + TAG_BASE;
}
