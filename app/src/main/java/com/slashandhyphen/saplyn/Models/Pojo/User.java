package com.slashandhyphen.saplyn.Models.Pojo;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mike on 9/17/2016.
 */
public class User {
    private static final String TAG = "~User~";

    public String successString;

    public Integer id;
    public String email;
    private String createdAt;
    public String updatedAt;
    private String authToken;
    private String username;

    public User(String authToken) {
        this.authToken = authToken;

        Log.d(TAG, "getStuffFromModel: Getting Stuff");

    }

    public String getUsername() {
        return this.username;
    }

    public String getCreatedAt() {
        // TODO Definition should be refactored up
        String datePresentationFormat = "EEE MMM dd, yyyy GG";

        return new SimpleDateFormat(datePresentationFormat, new Locale("en_US")).
                format(railsTimestampToDate(createdAt));
    }

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
