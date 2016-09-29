package com.slashandhyphen.saplyn.Models.SaplynWebservice;

import com.slashandhyphen.saplyn.Models.Pojo.User;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Mike on 9/17/2016.
 */
public class SaplynService {
    private static final String authToken = "b1e6668141b3dd7f8b12c13ae38bb78c";
    private static final String BASE_URL = "https://saplyn-ookamijin.c9users.io/api/v1/";

    private SaplynInterface saplynInterface;

    public SaplynService() {
        Retrofit retrofit = getRetrofitBuild();
        saplynInterface = retrofit.create(SaplynInterface.class);

    }

    private Retrofit getRetrofitBuild () {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(getHeader(authToken))
                .build();
    }

    private OkHttpClient getHeader(final String authorizationValue ) {
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

    public rx.Observable<User> viewUser() {
        return saplynInterface.viewUser();
    }

    interface SaplynInterface {
        // Request method and URL specified in the annotation
        // Callback for the parsed response is the last parameter

        @POST("sign_in")
        Call<User> loginUser(@Body User user);

        @GET("users")
        Observable<User> viewUser();

    }
}