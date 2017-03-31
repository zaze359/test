package com.zaze.demo.component.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.zaze.demo.component.provider.sqlite.DBOpenHelper;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-03-28 - 15:14
 */
public class ZazeProvider extends ContentProvider {
    public static final String AUTHORITY = "com.zaze.peopleprovider";
    // --------------------------------------------------
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/xh/");
    // --------------------------------------------------
    public static final int MULTIPLE_PEOPLE = 1;
    public static final int SINGLE_PEOPLE = 2;


    public static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//        uriMatcher.addURI(AUTHORITY, PATH_SINGLE, SINGLE_PEOPLE);
//        uriMatcher.addURI(AUTHORITY, PATH_MULTIPLE, MULTIPLE_PEOPLE);
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
//        switch (MATCHER.match(uri)) {
//            case PERSONS:
//                return db.query("person", projection, selection, selectionArgs,
//                        null, null, sortOrder);
//
//            case PERSON:
//                long id = ContentUris.parseId(uri);
//                String where = "_id=" + id;
//                if (selection != null && !"".equals(selection)) {
//                    where = selection + " and " + where;
//                }
//                return db.query("person", projection, where, selectionArgs, null,
//                        null, sortOrder);
//
//            default:
//                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
//        }


        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

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
