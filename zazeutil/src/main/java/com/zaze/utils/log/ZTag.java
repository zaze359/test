package com.zaze.utils.log;


/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-03-24 - 00:56
 */
public interface ZTag {
    String TAG_BASE = "[ZZ]";
    String TAG_ERROR = "Error" + TAG_BASE;
    String TAG_FILE = "File" + TAG_BASE;
    String TAG_DEBUG = "Debug" + TAG_BASE;
    String TAG_CMD = "Cmd" + TAG_BASE;
    String TAG_MEMORY = "MemoryCache" + TAG_BASE;
    String TAG_TASK = "Task" + TAG_BASE;
    String TAG_ABOUT_APP = "AboutApp" + TAG_BASE;
    String TAG_SYSTEM = "System" + TAG_BASE;
    String TAG_PROVIDER = "ContentProvider" + TAG_BASE;
    String TAG_ANALYZE = "Analyze" + TAG_BASE;
    String TAG_XML = "XML" + TAG_BASE;

}
