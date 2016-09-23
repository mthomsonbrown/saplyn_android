package com.slashandhyphen.saplyn.Models;

import android.util.Log;

import com.slashandhyphen.saplyn.Models.Pojo.GenericContainer;
import com.slashandhyphen.saplyn.Models.Pojo.User;
import com.slashandhyphen.saplyn.Models.SaplynWebservice.RetrofitHeader;
import com.slashandhyphen.saplyn.Models.SaplynWebservice.SaplynService;

import com.google.gson.JsonObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mike on 9/14/2016.
 */
public class FakeModel {
    private static final String TAG = "~FakeModel~";
    // Trailing slash is needed
    public static final String BASE_URL = "https://saplyn-ookamijin.c9users.io/api/v1/";
    public String token = "b1e6668141b3dd7f8b12c13ae38bb78c";

    public String getStuffFromModel () {
        Log.d(TAG, "getStuffFromModel: Getting Stuff");
        User user = new User();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(RetrofitHeader.getHeader(token))
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
