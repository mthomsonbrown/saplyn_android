package com.slashandhyphen.saplyn.Models.SaplynWebservice;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slashandhyphen.saplyn.Models.Pojo.User;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Mike on 9/17/2016.
 *
 * This class currently holds all actions done against the rails API.  It will at some point get
 * bloated and be divvied into separate classes.
 */
public class SaplynService {
    private static final String BASE_URL = "https://saplyn-ookamijin.c9users.io/api/v1/";
    private static final String authTokenMissingException = "To use this endpoint you need to " +
            "create a SaplynService that populates the authToken argument in the constructor";

    private SaplynInterface saplynInterface;
    private String authToken;

    public SaplynService() {
        saplynInterface = getRetrofitBuild().create(SaplynInterface.class);
    }

    /**
     * Creates an interface object.
     *
     * Currently, this is the only place that getRetrofitBuild is
     * called, so I'm tempted to squash the method into this, but the abstraction makes it clear
     * where data is coming from, and that functionality might be extended at some point, so I'll
     * leave it like this for now.
     */
    public SaplynService(String authToken) {
        this.authToken = authToken;
        saplynInterface = getRetrofitBuild().create(SaplynInterface.class);
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
     * Creates a retrofit object that can be used for interaction with the rails API.
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
     * This populates header data for requests to the Saplyn service.  It will create both
     * authenticated and unauthenticated requests.  The endpoint wrappers will throw an exception
     * if valid data is not supplied.
     *
     * @param authToken the AuthToken
     * @return an OkHttpClient object used by a Retrofit object to create a SaplynService interface
     * to talk to the backend.
     */
    private OkHttpClient getHeader(String authToken ) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(
                chain -> {
                    Request request = null;

                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .addHeader("Content-Type", "application/json");

                    if(authToken != null) {
                        requestBuilder.addHeader("Authorization", "Token token=" + authToken);
                    }

                        request = requestBuilder.build();

                    return chain.proceed(request);
                });

        return builder.build();
    }

    /*$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ Interface Getters/Setters $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$*/

    /**
     * Calls the SaplynService GET users endpoint and returns an Observable
     *
     * @return a callback to obtain user data
     * @throws RuntimeException if auth token has not been set in the constructor
     * of this SaplynService
     */
    public rx.Observable<User> viewUser() throws RuntimeException {
        if(authToken == null) {
            throw new RuntimeException(authTokenMissingException);
        }
        return saplynInterface.viewUser();
    }

    /**
     * Gets an auth token from the backend.
     *
     * @param user should have at least an email and password.
     * @return a user object with (hopefully) an auth token
     */
    public Observable<User> loginUser(User user) {
        return saplynInterface.loginUser(user);
    }

    public Observable<User> registerUser(User user) {
        return saplynInterface.createUser(user);
    }

    /**
     * This is the interface to the Saplyn webservice.  All endpoints should be described here.
     */
    interface SaplynInterface {

        @POST("sign_in")
        Observable<User> loginUser(@Body User user);

        @POST("users")
        Observable<User> createUser(@Body User user);

        @GET("users")
        Observable<User> viewUser();

    }
}