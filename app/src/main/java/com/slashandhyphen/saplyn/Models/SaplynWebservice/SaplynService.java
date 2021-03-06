package com.slashandhyphen.saplyn.Models.SaplynWebservice;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.slashandhyphen.saplyn.Models.Pojo.User;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;


import org.xml.sax.ErrorHandler;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Mike on 9/17/2016.
 *
 * This class currently holds all actions done against the rails API.  It will at some point get
 * bloated and be divvied into separate classes.
 */
public class SaplynService {
    private static final String BASE_URL        = "https://damp-castle-87964.herokuapp.com/api/v1/";
    private static final String BASE_URL_PERS   = "https://damp-castle-87964.herokuapp.com/api/v1/";
    private static final String BASE_URL_TEST   = "https://saplyn-ookamijin.c9users.io/api/v1/";
    private static final String authTokenMissingException = "To use this endpoint you need to " +
            "create a SaplynService that populates the authToken argument in the constructor";

    private SaplynInterface saplynInterface;
    private String authToken;
    private String activeUrl = null;

    // This is a quick fix for adding debug options to the UI.
    // 0 = Production
    // 1 = Personal
    // 2 = Debug
    public static String debugLvl = "DebugLevel";

    public SaplynService(int debugLvl) {
        this.init(debugLvl, null);
    }

    /**
     * Creates an interface object.
     *
     * Currently, this is the only place that getRetrofitBuild is
     * called, so I'm tempted to squash the method into this, but the abstraction makes it clear
     * where data is coming from, and that functionality might be extended at some point, so I'll
     * leave it like this for now.
     */
    public SaplynService(int debugLvl, String authToken) {
        this.init(debugLvl, authToken);
    }

    private void init(int debugLvl, String authToken) {
        this.authToken = authToken;
        switch (debugLvl) {
            case 0:
                activeUrl = BASE_URL;
                break;
            case 1:
                activeUrl = BASE_URL_PERS;
                break;
            case 2:
                activeUrl = BASE_URL_TEST;
                break;
            default:
                break;
        }
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
                    .baseUrl(activeUrl)
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

    /**
     * Parses an http exception and returns the message as a string
     */
    public String getErrorMessage(HttpException httpException) {
        Gson gson = new Gson();
        JsonObject jsonObject = null;
        try {
            jsonObject = gson.fromJson(httpException.response().errorBody().string(), JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject.get("error").toString();
    }

    /*$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ Interface Getters/Setters $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$*/

    /**
     * Calls the SaplynService GET users endpoint and returns a user object
     *
     * @return a callback to obtain user data
     * @throws RuntimeException if auth token has not been set in the constructor
     * of this SaplynService
     */
    public Observable<User> viewUser() throws RuntimeException {
        if(authToken == null) {
            throw new RuntimeException(authTokenMissingException);
        }
        return saplynInterface.viewUser();
    }

    /**
     * Gets an auth token from the backend.
     *
     * @param user should have at least an email and password
     * @return a user object with an auth token
     */
    public Observable<User> loginUser(User user) {
        return saplynInterface.loginUser(user);
    }

    /**
     * Creates a new user in the backend and supplies an auth token that can be added to
     * shared preferences
     *
     * @param user needs an email address, password, and password confirmation
     * @return a user object with an auth token
     */
    public Observable<User> registerUser(User user) {
        return saplynInterface.createUser(user);
    }

    /**
     * Sends a deregister request to the backend for the given user id
     */
    public Observable<ResponseBody> destroyUser(int userId) {
        return saplynInterface.destroyUser(userId);
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

        @DELETE("users/{id}")
        Observable<ResponseBody> destroyUser(@Path("id") int userId);

    }
}