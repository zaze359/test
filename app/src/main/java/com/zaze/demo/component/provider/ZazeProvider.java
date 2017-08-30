package com.zaze.demo.component.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.zaze.demo.component.provider.sqlite.DBOpenHelper;
import com.zaze.demo.component.provider.sqlite.User;
import com.zaze.demo.component.provider.sqlite.UserDao;
import com.zaze.utils.ZStringUtil;
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
    public static final int MULTIPLE_PEOPLE = 1;
    public static final int SINGLE_PEOPLE = 2;

    public static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(User.AUTHORITY, User.PATH_SINGLE, SINGLE_PEOPLE);
        uriMatcher.addURI(User.AUTHORITY, User.PATH_MULTIPLE, MULTIPLE_PEOPLE);
    }

    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        this.dbOpenHelper = new DBOpenHelper(this.getContext());
        db = dbOpenHelper.getWritableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        ZLog.i(ZTag.TAG_PROVIDER, "query");
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(UserDao.Properties.TABLE_NAME);
        switch (uriMatcher.match(uri)) {
            case MULTIPLE_PEOPLE:
                ZLog.i(ZTag.TAG_PROVIDER, "MULTIPLE_PEOPLE");
                break;
            case SINGLE_PEOPLE:
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
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String username = values.getAsString(UserDao.Properties.USER_NAME);
        long userId = values.getAsLong(UserDao.Properties.USER_ID);
        ZLog.i(ZTag.TAG_PROVIDER, ZStringUtil.format("insert %s %s", username, userId));
        User user = new User();
        user.setUsername(username);
        user.setUserId(userId);
        long _id = dbOpenHelper.getUserDao().insert(values);
        Uri newUri = ContentUris.withAppendedId(User.CONTENT_URI, _id);
        getContext().getContentResolver().notifyChange(newUri, null);
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
