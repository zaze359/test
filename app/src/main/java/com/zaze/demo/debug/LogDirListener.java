package com.zaze.demo.debug;

import android.os.FileObserver;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.io.File;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-08-03 - 19:16
 */
public class LogDirListener extends FileObserver {
    private String observerDir;
    private Face face;

    public void setFace(Face face) {
        this.face = face;
    }

    public LogDirListener(String path) {
        super(path);
        observerDir = path;
        face = new Face() {
            @Override
            public void onCloseWrite(File file) {

            }
        };
    }

    @Override
    public void onEvent(int event, String path) {
        ZLog.i(ZTag.TAG_DEBUG, "-------------------");
        String absolutePath = observerDir + File.separator + path;
        final int action = event & FileObserver.ALL_EVENTS;
        switch (action) {
            case FileObserver.CLOSE_WRITE:
                if (face != null) {
                    face.onCloseWrite(new File(absolutePath));
                }
                ZLog.i(ZTag.TAG_DEBUG, "event: 文件或目录停止写");
                break;
            case FileObserver.ACCESS:
                ZLog.i(ZTag.TAG_DEBUG, "event: 文件或目录被访问");
                break;
            case FileObserver.DELETE:
                ZLog.i(ZTag.TAG_DEBUG, "event: 文件或目录被删除");
                break;

            case FileObserver.OPEN:
                ZLog.i(ZTag.TAG_DEBUG, "event: 文件或目录被打开");
                break;

            case FileObserver.MODIFY:
                ZLog.i(ZTag.TAG_DEBUG, "event: 文件或目录被修改");
                break;
            default:
                break;
        }
        ZLog.i(ZTag.TAG_DEBUG, "action : " + action + ", path: " + absolutePath);
        ZLog.i(ZTag.TAG_DEBUG, "-------------------");

    }

    public interface Face {
        /**
         * file
         *
         * @param file file
         */
        void onCloseWrite(File file);
    }
}