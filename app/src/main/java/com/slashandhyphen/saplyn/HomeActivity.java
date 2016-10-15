package com.slashandhyphen.saplyn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.slashandhyphen.saplyn.Authentication.AuthenticationActivity;
import com.slashandhyphen.saplyn.Models.Pojo.User;
import com.slashandhyphen.saplyn.Models.SaplynWebservice.SaplynService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Mike on 9/30/2016.
 *
 * This is the landing page for user space.  If an auth token is present, this activity is
 * displayed.  If not, AuthenticationActivity is summoned.
 */
public class HomeActivity extends Activity implements View.OnClickListener {
    private static SharedPreferences preferences;
    private static String TAG = "~Home~";
    TextView fakeText;
    TextView fakeCreated;
    Button logoutButton;
    private Observable<User> userListener;
    Context context;
    String authToken;
    SaplynService saplynService;

    /**
     * Loads what the user sees when first entering user space.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = getApplicationContext();

        preferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        fakeText = (TextView) findViewById(R.id.hello);
        fakeCreated = (TextView) findViewById(R.id.created);
        logoutButton = (Button) findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(this);

        // I think I like this better than defining a button object...unless I need one
        findViewById(R.id.button_deregister).setOnClickListener(this);

        authToken = preferences.getString(getString(R.string.auth_token), "");
        saplynService = new SaplynService(authToken);
    }

    /**
     * Checks if an auth token is present.  If not, AuthenticationActivity is called to obtain one.
     */
    @Override
    public void onResume() {
        super.onResume();

        // Check if user already authenticated
        if (authToken == "") {
            Intent intent = new Intent(HomeActivity.this, AuthenticationActivity.class);
            startActivityForResult(intent, 0);
        }
        else {
            // After user is authenticated, grab their data from the net
            // TODO This will ping the net on every resume (which is probably frequent).
            // TODO Need to add in syncing logic
            userListener = saplynService.viewUser();
            populateUser();
        }
    }

    /**
     * Callback to fill user data fields.
     */
    private void populateUser() {
        userListener.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
                            fakeText.setText(user.getEmail());
                            fakeCreated.setText(user.getCreatedAt());
                        },
                        throwable -> {
                            if(throwable instanceof HttpException) {
                                ResponseBody body = ((HttpException) throwable).response().errorBody();
                                Log.e(TAG, "onErrorFromPopulateUser: "
                                        + body.toString());
                            }
                        }
                );
    }

    /**
     * @param view handles lotout and deregister actions
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_logout:
                logout();
                break;
            case R.id.button_deregister:
                doDeregister();
                break;
            default:
                break;
        }
    }

    /**
     * Deletes the shared preference auth_token, then spawns a new instance of
     * the AuthenticationActivity
     */
    private void logout() {
        preferences.edit().remove(getString(R.string.auth_token)).commit();
        Intent intent = new Intent(HomeActivity.this, AuthenticationActivity.class);
        startActivityForResult(intent, 0);
    }

    /**
     * This will be dissected later, but right now it grabs a user id using the current auth token
     * and then uses that userId to destroy itself
     */
    private void doDeregister() {
        userListener = saplynService.viewUser();
        userListener.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
                            Log.e(TAG, "doDeregister: Maybe destroying?");
                            Observable<ResponseBody> response = saplynService.destroyUser(user.getId());
                            response.subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            theResponse -> {
                                                // TODO sanity check:
                                                /*
                                                should do a sanity check here, but if we get
                                                here, then there's good confidence the
                                                operation passed.  I'd like to grab the status code
                                                from the theResponse object, but that's more trouble
                                                than it's worth to me right now.
                                                 */

                                                // user's been deleted from web service, time to
                                                // log out from the client:
                                                logout();
                                            }
                                    );
                        },
                        throwable -> {
                            Log.e(TAG, "doDeregister: " + throwable.getMessage());
                        }
                );
    }
}
