package com.slashandhyphen.saplyn.Models;

import android.util.Log;

import com.slashandhyphen.saplyn.Models.Pojo.User;
import com.slashandhyphen.saplyn.Models.SaplynWebservice.RetrofitHeader;
import com.slashandhyphen.saplyn.Models.SaplynWebservice.SaplynService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mike on 9/14/2016.
 */
public class FakeModel {
    public static final String TAG = "~FakeModel~";
    // Trailing slash is needed
    public static final String BASE_URL = "https://saplyn-ookamijin.c9users.io/api/v1/";
    public String token = "MyToken";

    public String getStuffFromModel () {
        Log.d(TAG, "getStuffFromModel: Getting Stuff");
        User user = new User();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(RetrofitHeader.getHeader(token))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SaplynService service = retrofit.create(SaplynService.class);

        Call<User> call = service.loginUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "onResponse: Got response from callback");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "onFailure: Got failure from callback");
            }
        });

        return "Some basic stuff";
    }
}
