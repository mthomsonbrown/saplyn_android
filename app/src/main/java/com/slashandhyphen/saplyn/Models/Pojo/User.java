package com.slashandhyphen.saplyn.Models.Pojo;

/**
 * Created by Mike on 9/17/2016.
 */
public class User {
    public Integer id;
    public String email;
    public String createdAt;
    public String updatedAt;
    public String authToken;
    public String username;

    public String getUsername() {
        return this.username;
    }
}
