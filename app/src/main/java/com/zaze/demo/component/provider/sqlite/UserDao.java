package com.zaze.demo.component.provider.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Locale;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-03-30 - 00:28
 */
public class UserDao extends BaseDao<User> {
    public static final String PATH_SINGLE = "user/#";
    public static final String PATH_MULTIPLE = "user";


    public static class Properties {
        public final static String TABLE_NAME = "user";
        public final static String ID = "_id";
        public final static String USER_ID = "user_id";
        public final static String USER_NAME = "user_name";
    }

    public UserDao(SQLiteDatabase db) {
        super(db);
    }

    @Override
    protected String getTableName() {
        return Properties.TABLE_NAME;
    }

    @Override
    public void createTable() {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Properties.TABLE_NAME +
                "(" + Properties.ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", " + Properties.USER_ID + " LONG" +
                ", " + Properties.USER_NAME + " VARCHAR" +
                ")");
    }

    @Override
    public void updateTable() {

    }

    @Override
    protected String insertSql(User user) {
        String columns = format("%s, %s"
                , Properties.USER_ID
                , Properties.USER_NAME
        );
        String values = format("'%s', '%s'",
                user.getUserId(),
                user.getUsername()
        );
        return format("INSERT INTO %s(%s) VALUES(%s);", Properties.TABLE_NAME, columns, values);
    }

    @Override
    protected String updateSql(User user) {
        return String.format(Locale.getDefault(),
                "UPDATE %s SET %s='%s' WHERE %s='%s';",
                Properties.TABLE_NAME,
                // --------------------------------------------------
                Properties.USER_NAME, user.getUsername(),
                // --------------------------------------------------
                Properties.USER_ID, user.getUserId()

        );
    }

    @Override
    public User dealCursor(Cursor cursor) {
        User user = new User();
        user.setId(getLong(cursor, Properties.ID));
        user.setUserId(getLong(cursor, Properties.USER_ID));
        user.setUsername(getString(cursor, Properties.USER_NAME));
        return user;
    }
}
