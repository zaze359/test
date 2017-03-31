package com.zaze.demo.component.provider.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zaze.aarrepo.utils.StringUtil;

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
public abstract class BaseDao<T> {
    //    protected final ConcurrentHashMap<String, T> cache = new ConcurrentHashMap<String, T>();
//    protected final ConcurrentHashMap<String, List<T>> listCache = new ConcurrentHashMap<String, List<T>>();
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

    public List<T> queryWhereList(String tableName, String where, String[] selectionArgs) {
        return rawQueryList("SELECT * FROM " + tableName + " " + where, selectionArgs);
    }

    public T queryWhere(String tableName, String where, String[] selectionArgs) {
        return query("SELECT * FROM " + tableName + " " + where, selectionArgs);
    }

    public void insert(T t) {
        String sql = insertSql(t);
        execSQL(sql);
    }

    public void update(T t) {
        execSQL(updateSql(t));
    }

    protected void delete(String where) {
        execSQL(StringUtil.format("DELETE FROM %s %s", getTableName(), where));
    }


    void deleteAll() {
        execSQL(StringUtil.format("DELETE FROM %s", getTableName()));
    }

    public void dropTable() {
        db.execSQL("DROP TABLE IF EXISTS " + getTableName());
    }
    // --------------

    public void execSQL(String sql) {
        db.execSQL(sql);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return db.rawQuery(sql, selectionArgs);
    }


    //
    protected abstract String getTableName();

    public abstract void createTable();

    public abstract void updateTable();

    protected abstract String insertSql(T t);

    protected abstract String updateSql(T t);

    protected abstract T dealCursor(Cursor cursor);


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

//    protected void saveCache(String key, T t) {
//        if (key != null && t != null) {
//            cache.put(key, t);
//        }
//    }

//    protected T getCache(String key) {
//        if (cache.containsKey(key)) {
//            return cache.get(key);
//        }
//        return null;
//    }
//
//    protected void saveListCache(String key, List<T> list) {
//        if (key != null && list != null) {
//            listCache.put(key, list);
//        }
//    }
//
//    protected List<T> getListCache(String key) {
//        if (listCache.containsKey(key)) {
//            return listCache.get(key);
//        }
//        return null;
//    }
//
//    private void clearCache() {
//        cache.clear();
//        listCache.clear();
//    }
}
