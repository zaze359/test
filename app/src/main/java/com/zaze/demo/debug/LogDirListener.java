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
        File file = new File(observerDir + File.separator + path);
        final int action = event & FileObserver.ALL_EVENTS;
        switch (action) {
            case FileObserver.CLOSE_WRITE:
                if (face != null) {
                    face.onCloseWrite(file);
                }
                ZLog.i(ZTag.TAG_DEBUG, "event: 文件或目录停止写, path: " + path);
                break;
            case FileObserver.ACCESS:
                ZLog.i(ZTag.TAG_DEBUG, "event: 文件或目录被访问, path: " + path);
                break;

            case FileObserver.DELETE:
                ZLog.i(ZTag.TAG_DEBUG, "event: 文件或目录被删除, path: " + path);
                break;

            case FileObserver.OPEN:
                ZLog.i(ZTag.TAG_DEBUG, "event: 文件或目录被打开, path: " + path);
                break;

            case FileObserver.MODIFY:
                ZLog.i(ZTag.TAG_DEBUG, "event: 文件或目录被修改, path: " + path);
                break;
            default:
                break;
        }
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