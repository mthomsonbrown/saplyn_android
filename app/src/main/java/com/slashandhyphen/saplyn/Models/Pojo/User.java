package com.slashandhyphen.saplyn.Models.Pojo;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mike on 10/12/2016.
 *
 * Deserialization container for json User objects provisioned from the backend.  This also holds
 * local functions to act on that data.
 */

public class User {
    private UserData user;

    /**
     * constructor
     *
     * @param email email address of the user
     * @param password the password for the user account
     */
    public User(String email, String password) {
        user = new UserData(email, password);
    }

    /**
     * constructor
     *
     * @param email email address of the user
     * @param password the password for the user account
     * @param passwordConfirmation the password for the user account
     */
    public User(String email, String password, String passwordConfirmation) {
        this.user = new UserData(email, password, passwordConfirmation);
    }

    /**
     * @return the auth token provisioned from the server
     */
    public String getAuthToken() {
        return user.getAuthToken();
    }

    /**
     * @return the username of the user
     */
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * @return the time the entry was created
     */
    public String getCreatedAt() {
        return user.getCreatedAt();
    }

    /**
     * @return email address of the user
     */
    public String getEmail() {
        return user.getEmail();
    }

    public int getId() {
        return user.getId();
    }

    /*$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ Internal User Class Stuff $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$*/
    /**
     * The actual user data is stored in this internal class in order to conform to Rails' JSON
     * structure.
     */
    private class UserData {
        private static final String TAG = "~User~";

        private Integer id;
        private String email;
        private String createdAt;
        private String updatedAt;
        private String authToken;
        private String username;
        private String password;
        private String passwordConfirmation;

        /**
         * Creates a user object without an authToken to be used to obtain one.
         *
         * @param email your email
         * @param password your password
         */
        UserData(String email, String password) {
            this.email = email;
            this.password = password;
        }

        /**
         * Creates a user object without an authToken to be used to obtain one.
         *
         * @param email your email
         * @param password your password
         */
        UserData(String email, String password, String passwordConfirmation) {
            this.email = email;
            this.password = password;
            this.passwordConfirmation = passwordConfirmation;
        }

        /**
         * @return the username
         */
        String getUsername() {
            return this.username;
        }

        /**
         * @return the auth token
         */
        String getAuthToken() {
            return authToken;
        }

        /**
         * @return the emails
         */
        private String getEmail() {
            return email;
        }

        /**
         * Created at date from the server.
         *
         * @return Custom formatted date string
         */
        String getCreatedAt() {
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

        public Integer getId() {
            return id;
        }
    }
}
