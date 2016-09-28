package com.slashandhyphen.saplyn.Models.SaplynWebservice;

import com.slashandhyphen.saplyn.Models.Pojo.User;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Mike on 9/17/2016.
 */
public interface SaplynService {
    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @POST("sign_in")
    Call<User> loginUser(@Body User user);

    @GET("users")
    Observable<User> viewUser();

}
