package com.zaze.demo.debug;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Description :
 * date : 2016-02-22 - 12:06
 *
 * @author : zaze
 * @version : 1.0
 */
abstract class BaseDao<T> {
    protected SQLiteDatabase db;

    public BaseDao(SQLiteDatabase db) {
        this.db = db;
    }

    public String format(String str, Object... args) {
        return String.format(Locale.getDefault(), str, args);
    }

    // ---------------------------------------
    // 添加一个字段
    public synchronized void updateColumn(String columnName, String columnType) {
        Cursor c = rawQuery("SELECT * from " + getTableName() + " limit 1 ", null);
        boolean flag = false;
        for (int i = 0; i < c.getColumnCount(); i++) {
            if (columnName.equalsIgnoreCase(c.getColumnName(i))) {
                flag = true;
                break;
            }
        }

        if (!flag) {
            String sql = "alter table " + getTableName() + " add " + columnName + " " + columnType;
            execSQL(sql);
        }
        c.close();
    }

    // ---------------------------------------
    protected T query(String sql, String[] selectionArgs) {
        Cursor cursor = rawQuery(sql, selectionArgs);
        if (cursor == null) {
            return null;
        }
        T t = null;
        try {
            if (cursor.moveToNext()) {
                t = dealCursor(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return t;
    }

    public Cursor query(Uri uri, String[] projectionIn, String selection, String[] selectionArgs, String groupBy, String having, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(getTableName());
        return queryBuilder.query(
                this.db, projectionIn, selection,
                selectionArgs, groupBy, having, sortOrder
        );
    }


    protected List<T> rawQueryList(String sql, String[] selectionArgs) {
        List<T> list = new ArrayList<>();
        Cursor cursor = rawQuery(sql, selectionArgs);
        if (cursor == null) {
            return list;
        }
        try {
            while (cursor.moveToNext()) {
                T t = dealCursor(cursor);
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return list;
    }

    public List<T> queryWhereList(String where, String[] selectionArgs) {
        return rawQueryList("SELECT * FROM " + getTableName() + " " + where, selectionArgs);
    }

    public T queryWhere(String where, String[] selectionArgs) {
        return query("SELECT * FROM " + getTableName() + " " + where, selectionArgs);
    }

    public void insert(T t) {
        String sql = insertSql(t);
        execSQL(sql);
    }

    public long insert(ContentValues values) {
        return db.insert(getTableName(), null, values);
    }

    public int update(ContentValues values, String where, String[] whereArgs) {
        return db.update(getTableName(), values, where, whereArgs);
    }

    public void update(T t) {
        execSQL(updateSql(t));
    }

    protected void delete(String where) {
        execSQL(String.format("DELETE FROM %s %s", getTableName(), where));
    }

    public void deleteAll() {
        execSQL(String.format("DELETE FROM %s", getTableName()));
    }

    public int delete(String whereClause, String[] whereArgs) {
        return db.delete(getTableName(), whereClause, whereArgs);
    }

    public void dropTable() {
        db.execSQL("DROP TABLE IF EXISTS " + getTableName());
    }
    // --------------------------------------------------

    public void execSQL(String sql) {
        db.execSQL(sql);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return db.rawQuery(sql, selectionArgs);
    }

    // --------------------------------------------------
    protected abstract String getTableName();

    public abstract void createTable();

    public abstract void updateTable();

    protected abstract String insertSql(T t);

    protected abstract String updateSql(T t);

    public abstract T dealCursor(Cursor cursor);

    // ----------------
    protected String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    protected long getLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    protected int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }
}
