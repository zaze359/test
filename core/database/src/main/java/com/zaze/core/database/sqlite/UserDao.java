package com.zaze.core.database.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zaze.core.database.model.UserEntity;
import com.zaze.utils.CursorHelper;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-03-30 - 00:28
 */
public class UserDao extends BaseDao<UserEntity> {
    public static final String PATH_SINGLE = "user/#";
    public static final String PATH_MULTIPLE = "user";

    public static class Properties {
        public final static String TABLE_NAME = "user";
        public final static String ID = "_id";
        public final static String USER_ID = "user_id";
        public final static String USER_NAME = "user_name";
    }

    @Override
    public void insert(UserEntity userEntity) {
        //        String sql = insertSql(t);
        //        execSQL(sql);
        ContentValues values = new ContentValues();
        values.put(Properties.USER_ID, userEntity.getUserId());
        values.put(Properties.USER_NAME, userEntity.getUsername());
        insert(values);
    }

    @Override
    public void update(UserEntity userEntity) {
        //        execSQL(updateSql(t));
        ContentValues values = new ContentValues();
//        values.put(Properties.USER_ID, userEntity.getUserId());
        values.put(Properties.USER_NAME, userEntity.getUsername());
        update(values, Properties.USER_ID + "=?", new String[]{userEntity.getUserId() + ""});
    }

    @Override
    protected String getTableName() {
        return Properties.TABLE_NAME;
    }

    @Override
    public void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Properties.TABLE_NAME +
                "(" + Properties.ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", " + Properties.USER_ID + " LONG" +
                ", " + Properties.USER_NAME + " VARCHAR" +
                ")");
    }

    @Override
    public void updateTable(SQLiteDatabase db) {

    }

//    @Override
//    protected String insertSql(UserEntity user) {
//        String columns = format("%s, %s"
//                , Properties.USER_ID
//                , Properties.USER_NAME
//        );
//        String values = format("'%s', '%s'",
//                user.getUserId(),
//                user.getUsername()
//        );
//        return format("INSERT INTO %s(%s) VALUES(%s);", Properties.TABLE_NAME, columns, values);
//    }

//    @Override
//    protected String updateSql(UserEntity user) {
//        return String.format(Locale.getDefault(),
//                "UPDATE %s SET %s='%s' WHERE %s='%s';",
//                Properties.TABLE_NAME,
//                Properties.USER_NAME, user.getUsername(),
//                Properties.USER_ID, user.getUserId()
//        );
//    }

    @Override
    public UserEntity dealCursor(Cursor cursor) {
        return new UserEntity(
                CursorHelper.getInt(cursor, Properties.ID),
                CursorHelper.getLong(cursor, Properties.USER_ID),
                CursorHelper.getString(cursor, Properties.USER_NAME)
        );
    }
}
