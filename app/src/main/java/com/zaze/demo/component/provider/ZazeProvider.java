package com.zaze.demo.component.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.zaze.aarrepo.commons.log.ZLog;
import com.zaze.aarrepo.utils.ZTag;
import com.zaze.demo.component.provider.sqlite.DBOpenHelper;
import com.zaze.demo.component.provider.sqlite.User;

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

    @Override
    public boolean onCreate() {
        this.dbOpenHelper = new DBOpenHelper(this.getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        ZLog.i(ZTag.TAG_PROVIDER, "query");
        switch (uriMatcher.match(uri)) {
            case MULTIPLE_PEOPLE:
//                return db.query("person", projection, selection, selectionArgs, null, null, sortOrder);
                ZLog.i(ZTag.TAG_PROVIDER, "MULTIPLE_PEOPLE");
                return null;
            case SINGLE_PEOPLE:
                ZLog.i(ZTag.TAG_PROVIDER, "SINGLE_PEOPLE");
//                long id = ContentUris.parseId(uri);
//                String where = "_id=" + id;
//                if (selection != null && !"".equals(selection)) {
//                    where = selection + " and " + where;
//                }
//                return db.query("person", projection, where, selectionArgs, null, null, sortOrder);
                return null;
            default:
                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        ZLog.i(ZTag.TAG_PROVIDER, "insert");
        return null;
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
