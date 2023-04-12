package com.zaze.demo.feature.storage.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.zaze.core.database.sqlite.DBOpenHelper;
import com.zaze.core.database.sqlite.UserDao;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-03-28 - 15:14
 */
public class ZazeProvider extends ContentProvider {
    // --------------------------------------------------
    public static final String AUTHORITY = "com.zaze.user.provider";
    /**
     * 访问单条数据
     * #：表示 id
     */
    public static final String PATH_SINGLE = "user/#";
    /** 访问所有数据 */
    public static final String PATH_MULTIPLE = "user";
    public static final int MULTIPLE_PEOPLE = 1;
    public static final int SINGLE_PEOPLE = 2;


    public static final String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + PATH_MULTIPLE;

    //
    public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH_SINGLE, SINGLE_PEOPLE);
        uriMatcher.addURI(AUTHORITY, PATH_MULTIPLE, MULTIPLE_PEOPLE);
    }

    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        this.dbOpenHelper = new DBOpenHelper(this.getContext());
        db = dbOpenHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        ZLog.i(ZTag.TAG_PROVIDER, "query: " + selection);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(UserDao.Properties.TABLE_NAME);
        switch (uriMatcher.match(uri)) {
            case MULTIPLE_PEOPLE:
                ZLog.i(ZTag.TAG_PROVIDER, "MULTIPLE_PEOPLE");
                break;
            case SINGLE_PEOPLE:
                // uri.getPathSegments() 将AUTHORITY后的内容按照 / 分割Uri，0是path,
                queryBuilder.appendWhere(UserDao.Properties.USER_ID + "=" + uri.getPathSegments().get(1));
                ZLog.i(ZTag.TAG_PROVIDER, "SINGLE_PEOPLE");
                break;
            default:
                break;
        }
        return queryBuilder.query(
                db, projection, selection,
                selectionArgs, null, null, sortOrder
        );
//        return dbOpenHelper.getUserDao().query(projection, selection, selectionArgs, null, null, sortOrder);
    }

    // 用于返回MIME类型, 主要是 用于隐式启动 Activity的
    // intent.setData(Uri) 这里传入的 ContentProvider Uri会经过getType()转为MIME，然后去匹配对应Action的Activity
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case MULTIPLE_PEOPLE:
                // 多条
                return "vnd.android.cursor.dir/com.zaze.user.provider.user";
            case SINGLE_PEOPLE:
                // 单条
                return "vnd.android.cursor.item/com.zaze.user.provider.user";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long insertId = dbOpenHelper.getUserDao().insert(values);
        // 返回的Uri
        Uri newUri = ContentUris.withAppendedId(CONTENT_URI, insertId);
        getContext().getContentResolver().notifyChange(newUri, null);
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return dbOpenHelper.getUserDao().delete(selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return dbOpenHelper.getUserDao().update(values, selection, selectionArgs);
    }
}
