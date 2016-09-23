package com.slashandhyphen.saplyn.Models.Pojo;

import android.util.Log;

import com.google.gson.JsonObject;
import com.slashandhyphen.saplyn.Models.SaplynWebservice.RetrofitHeader;
import com.slashandhyphen.saplyn.Models.SaplynWebservice.SaplynService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mike on 9/17/2016.
 */
public class User {
    private static final String TAG = "~User~";
    // Trailing slash is needed
    private static final String BASE_URL = "https://saplyn-ookamijin.c9users.io/api/v1/";

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(RetrofitHeader.getHeader(authToken))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SaplynService service = retrofit.create(SaplynService.class);

        Call<JsonObject> call = service.viewUser();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Got response from callback " + response.code());
                    Log.d(TAG, "onResponse: " + response.body());
                }
                else {
                    Log.d(TAG, "onResponse: Got an error code: " + response.code());
                    try {
                        Log.d(TAG, "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.d(TAG, "onResponse: " + "...And it threw an IO Exception...");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onFailure: Got failure from callback");
                Log.d(TAG, "onFailure: t is: ", t);
            }
        });


        successString = "Some basic string of success";
    }

    public String getUsername() {
        return this.username;
    }

    public String getStuffFromModel () {
        Log.d(TAG, "getStuffFromModel: Getting Stuff");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(RetrofitHeader.getHeader(authToken))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SaplynService service = retrofit.create(SaplynService.class);

        Call<JsonObject> call = service.viewUser();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String stop = "string";
                if(response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Got response from callback " + response.code());
                    JsonObject me = response.body();
                    Log.d(TAG, "onResponse: " + response.body());
                }
                else {
                    Log.d(TAG, "onResponse: Got an error code: " + response.code());
                    try {
                        Log.d(TAG, "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.d(TAG, "onResponse: " + "...And it threw an IO Exception...");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onFailure: Got failure from callback");
                Log.d(TAG, "onFailure: t is: ", t);
            }
        });


        return "Some basic stuff";
    }
}
