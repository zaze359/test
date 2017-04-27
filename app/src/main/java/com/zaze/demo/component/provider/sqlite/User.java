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

    private long id;
    private long userId;
    private String username;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
