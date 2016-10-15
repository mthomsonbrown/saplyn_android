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
    }

    /**
     * Checks if an auth token is present.  If not, AuthenticationActivity is called to obtain one.
     */
    @Override
    public void onResume() {
        super.onResume();

        authToken = preferences.getString(getString(R.string.auth_token), "");

        // Check if user already authenticated
        if (authToken == "") {
            Intent intent = new Intent(HomeActivity.this, AuthenticationActivity.class);
            startActivityForResult(intent, 0);
        }
        else {
            // After user is authenticated, grab their data from the net
            // TODO This will ping the net on every resume (which is probably frequent).
            // TODO Need to add in syncing logic
            userListener = new SaplynService(authToken).viewUser();
            populateUser();
        }
    }

    /**
     * Callback to fill user data fields.
     */
    private void populateUser() {
        userListener.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                            fakeText.setText(user.getEmail());
                            fakeCreated.setText(user.getCreatedAt());
                        },
                        throwable -> Log.e(TAG, "onErrorFromPopulateUser: "
                                + throwable.getMessage()));
    }

    /**
     * @param view currently only responds to a logout button
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_logout:
                logout();
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
}
