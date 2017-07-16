package com.zz.library.util;

import android.os.FileObserver;
import android.util.Log;

import com.zaze.aarrepo.utils.ZTag;

/**
 * SD卡中的目录创建监听器。
 *
 * @author mayingcai
 */
public class DirListener extends FileObserver {
    public DirListener(String path) {
        super(path);
    }
//    String dirPath = null;
//    String packageName = null;

    /**
     * 这种构造方法是默认监听所有事件的,如果使用 super(String,int)这种构造方法， 则int参数是要监听的事件类型.
     **/
//    public DirListener(String path) {
//        super(path);
//        dirPath = path;
//        Log.d(LcTag.TAG_DEBUG, "dirPath:" + dirPath);
//    }
    @Override
    public void onEvent(int event, String path) {
        Log.i(ZTag.TAG_DEBUG, "onEvent : " + path);
    }

    //    @Override
//    public void onEvent(int event, String fileName) {
//        switch (event) {
//            case FileObserver.CLOSE_WRITE:
//                Log.d(LcTag.TAG_DEBUG, "CLOSE_WRITE:" + fileName);
//                String filePath = dirPath + "/" + fileName;
//                File file = new File(filePath);
//                if (file.exists() && file.isFile() && file.canRead()) {
//
//                } else {
//
//                }
//                break;
//        }
//    }


//    File file = new File(PubConstants.LOG_DIR);
//                if (file.exists() && file.length() > 10 << 20) {
//        // 当日志大于10MB时 清空一下
//        deleteDir(PubConstants.LOG_DIR);
//    }
//                FileUtil.writeLogFile(PubConstants.LOG_DIR
//                        , LOG_FILE_NAME
//                        , dataStr
//                        , 1 << 20);

}
