package com.zaze.demo.component.provider.sqlite;

import android.net.Uri;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-03-30 - 00:43
 */
public class User {

    public static final String AUTHORITY = "com.zaze.user.provider";
    public static final String PATH_SINGLE = "user/#";
    public static final String PATH_MULTIPLE = "user";
    public static final String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + PATH_MULTIPLE;
    public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);


    public static final String KEY_ID = "id";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_USERNAME = "username";

    private int id;
    private int userId;
    private String username;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
