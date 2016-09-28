package com.slashandhyphen.saplyn.Models.SaplynWebservice;


import android.util.Log;


import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mike on 9/19/2016.
 */
public class RetrofitUtilities {
    private static final String authToken = "b1e6668141b3dd7f8b12c13ae38bb78c";
    private static final String BASE_URL = "https://saplyn-ookamijin.c9users.io/api/v1/";

    private static OkHttpClient getHeader(final String authorizationValue ) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(
                chain -> {
                    Request request = null;
                    if (authorizationValue != null) {

                        Request original = chain.request();
                        // Request customization: add request headers
                        Request.Builder requestBuilder = original.newBuilder()
                                .addHeader("Authorization", "Token token=" + authorizationValue)
                                .addHeader("Content-Type", "application/json");

                        request = requestBuilder.build();
                    }
                    return chain.proceed(request);
                });
        return builder.build();
    }

    public static Retrofit getRetrofitBuild () {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(RetrofitUtilities.getHeader(authToken))
                .build();
    }

}
