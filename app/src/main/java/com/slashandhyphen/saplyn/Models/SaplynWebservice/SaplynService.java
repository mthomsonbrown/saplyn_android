package com.slashandhyphen.saplyn.Models.SaplynWebservice;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slashandhyphen.saplyn.Models.Pojo.User;
import com.slashandhyphen.saplyn.R;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Mike on 9/17/2016.
 *
 * This class currently holds all actions done against the rails API.  It will at some point get
 * bloated and be divvied into separate classes.
 */
public class SaplynService {
    private static final String BASE_URL = "https://saplyn-ookamijin.c9users.io/api/v1/";

    private SaplynInterface saplynInterface;
    private String authToken;
    private SharedPreferences preferences;

    /**
     * Creates an interface object.
     *
     * Currently, this is the only place that getRetrofitBuild is
     * called, so I'm tempted to squash the method into this, but the abstraction makes it clear
     * where data is coming from, and that functionality might be extended at some point, so I'll
     * leave it like this for now.
     */
    public SaplynService(Context context) {
        saplynInterface = getRetrofitBuild().create(SaplynInterface.class);
//       preferences.getString(getString(R.id.auth_token), authToken);
        preferences = context.getSharedPreferences("CurrentUser", MODE_PRIVATE);
        authToken = preferences.getString(context.getString(R.string.auth_token), "");
    }

    /**
     * This is only used by the getRetrofitBuild function, and it may get squashed, but it adds
     * clarity for now so it's staying for the nonce.
     *
     * @return a Gson object that converts snake_case input to camelCase output
     */
    private Gson withCamelCaseConversion() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    /**
     * Creates a retrofit object that can be used for interaction with the rails API.  In the
     * future, there may be endpoints that need different information, so I can add new methods of
     * this type, but I don't currently see that as being necessary.
     *
     * @return a Retrofit object to obtain an interface from
     */
    private Retrofit getRetrofitBuild () {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(withCamelCaseConversion()))
                .baseUrl(BASE_URL)
                .client(getHeader(authToken))
                .build();
    }

    /**
     * This populates header data for requests to the Saplyn service.  Right now it deals with GETs
     * at least, requiring an auth token.  At some point, there will be GETs and POSTs that don't
     * have an auth token, so this will probably be the source of change in this class.
     *
     * @param authorizationValue the AuthToken
     * @return an OkHttpClient object used by a Retrofit object to create a SaplynService interface
     * to talk to the backend.
     */
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


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Interface Getters/(probably)Setters

    /**
     * Calls the SaplynService GET users endpoint and returns an Observable
     *
     * @return a callback to obtain user data
     */
    public rx.Observable<User> viewUser() {
        return saplynInterface.viewUser();
    }

    /**
     * This is the interface to the Saplyn webservice.  All endpoints should be described here.
     */
    interface SaplynInterface {

        @POST("sign_in")
        Call<User> loginUser(@Body User user);

        @GET("users")
        Observable<User> viewUser();

    }
}