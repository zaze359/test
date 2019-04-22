package com.zaze.demo.debug;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.zaze.utils.FileUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.io.File;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-03-30 - 00:20
 */
public class MyOpenHelper extends SQLiteOpenHelper {

    public MyOpenHelper(Context context, String dir, String databaseName) {
        super(getContentWrapper(context, dir), databaseName, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private static ContextWrapper getContentWrapper(Context context, final String storageDirectory) {
        return new ContextWrapper(context) {
            @Override
            public File getDatabasePath(String name) {
                if (FileUtil.isSdcardEnable()) {
                    if (!TextUtils.isEmpty(storageDirectory)) {
                        String filePath = storageDirectory.endsWith(File.separator) ? storageDirectory + name : storageDirectory + File.separator + name;
                        try {
                            FileUtil.createFileNotExists(filePath);
                            return new File(filePath);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    ZLog.e(ZTag.TAG_ERROR, "sdcard不可用!!");
                }
                return super.getDatabasePath(name);
            }

            @Override
            public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
                return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
            }

            @Override
            public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
                return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
            }
        };
    }
}
