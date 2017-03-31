package com.zaze.demo.component.provider.sqlite;

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
