package com.slashandhyphen.saplyn.Models.Pojo;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mike on 9/17/2016.
 *
 * Deserialization container for json User objects provisioned from the backend.  This also holds
 * local functions to act on that data.
 */
public class User {
    private static final String TAG = "~User~";

    public Integer id;
    public String email;
    private String createdAt;
    public String updatedAt;
    private String authToken;
    private String username;
    private String password;

    /**
     * Instantiates a user object with an auth token
     *
     * @param authToken the token used to authenticate the user to the server, held in
     *                  sharedPreferences.
     */
    public User(String authToken) {
        this.authToken = authToken;
    }

    /**
     * Creates a user object without an authToken to be used to obtain one.
     *
     * @param email ur email
     * @param password your password
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * @return the auth token
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * Created at date from the server.
     *
     * @return Custom formatted date string
     */
    public String getCreatedAt() {
        // TODO Definition should be refactored up
        String datePresentationFormat = "EEE MMM dd, yyyy GG";

        return new SimpleDateFormat(datePresentationFormat, new Locale("en_US")).
                format(railsTimestampToDate(createdAt));
    }

    /**
     * Ingests the rails formatted date into something parseable into a java Date object.
     *
     * @param railsDate the normal date timestamp returned by generic ActiveRecord
     * @return a Date object that can be manipulated in Java
     */
    // TODO Class should be refactored up
    private Date railsTimestampToDate(String railsDate) {
        String railsFormat = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(railsFormat, new Locale("en_US"));
        Date date = null;
        try {
            // TODO Enter terrible hacks.  I don't care about milliseconds, and that's where the
            // parse function is throwing a parse exception, so I will remove that bit:
            String hackyDate = railsDate.substring(0, 18);

            date = dateFormat.parse(hackyDate);
        } catch (ParseException e) {
            Log.d(TAG, "railsTimestampToDate: " + e.getMessage());
        }
        return date;
    }
}
