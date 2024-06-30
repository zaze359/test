package com.zaze.core.database.sqlite;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zaze.utils.FileUtil;

import java.io.File;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-03-30 - 00:20
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "zaze_provider.db"; //数据库名称
    private static final int DATABASE_VERSION = 2; //数据库版本

    private UserDao userDao;

    public DBOpenHelper(Context context) {
        super(new ContextWrapper(context) {
            @Override
            public File getDatabasePath(String name) {
                File path = new File("/data/data/" + context.getPackageName() + "/zaze/databases/" + name);
                FileUtil.createFileNotExists(path);
                return path;
            }
        }, DATABASE_NAME, null, DATABASE_VERSION);
        userDao = new UserDao();
        userDao.init(getWritableDatabase());
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        userDao.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        userDao.dropTable(db);
        onCreate(db);
    }

    public UserDao getUserDao() {
        return userDao;
    }
}
