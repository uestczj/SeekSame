package com.example.caizejian.seeksamehobbies.db;

import cn.bmob.v3.BmobUser;

/**
 * Created by caizejian on 2017/3/27.
 */

public class User extends BmobUser {
    private String userId;
    private String userDescribe;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserDescribe() {
        return userDescribe;
    }

    public void setUserDescribe(String userDescribe) {
        this.userDescribe = userDescribe;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

 }
