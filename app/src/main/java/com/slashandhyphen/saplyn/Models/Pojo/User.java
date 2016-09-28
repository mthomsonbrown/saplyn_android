package com.slashandhyphen.saplyn.Models.Pojo;

import android.util.Log;

/**
 * Created by Mike on 9/17/2016.
 */
public class User {
    private static final String TAG = "~User~";

    public String successString;

    public Integer id;
    public String email;
    public String createdAt;
    public String updatedAt;
    public String authToken;
    public String username;

    public User(String authToken) {
        this.authToken = authToken;

        Log.d(TAG, "getStuffFromModel: Getting Stuff");

    }

    public String getUsername() {
        return this.username;
    }
}
