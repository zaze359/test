package com.zaze.utils.log;


/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-03-24 - 00:56
 */
public interface ZTag {
    String TAG_BASE = "[ZAZE]";
    String TAG_ERROR = "Error[错误]" + TAG_BASE;
    String TAG_FILE = "File[文件操作]" + TAG_BASE;
    String TAG_DEBUG = "Debug[调试]" + TAG_BASE;
    String TAG_CDM = "Cmd[执行命令]" + TAG_BASE;
    String TAG_MEMORY = "MemoryCache[内存]" + TAG_BASE;
    String TAG_TASK = "Task[任务]" + TAG_BASE;
    String TAG_ABOUT_APP = "AboutApp[应用相关]" + TAG_BASE;
    String TAG_SYSTEM = "System[系统相关]" + TAG_BASE;
    String TAG_PROVIDER = "ContentProvider[数据]" + TAG_BASE;
    String TAG_ANALYZE = "Analyze[分析]" + TAG_BASE;
}
