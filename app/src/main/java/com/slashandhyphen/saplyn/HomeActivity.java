package com.slashandhyphen.saplyn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
public class HomeActivity extends Activity {
    private static SharedPreferences preferences;
    private static String TAG = "~Home~";
    TextView fakeText;
    TextView fakeCreated;
    private Observable<User> userListener;
    Context context;
    private static final String authToken = "b1e6668141b3dd7f8b12c13ae38bb78c";

    /**
     * Loads what the user sees when first entering user space.
     *
     * @param savedInstanceState used to reload state information during lifecycle transitions
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = getApplicationContext();
        preferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        SaplynService saplynService = new SaplynService(context);
        preferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        fakeText = (TextView) findViewById(R.id.hello);
        fakeCreated = (TextView) findViewById(R.id.created);

        userListener = saplynService.viewUser();
        populateUser();
    }

    /**
     * Checks if an auth token is present.  If not, AuthenticationActivity is called to obtain one.
     */
    @Override
    public void onResume() {
        super.onResume();
        // Check if user already authenticated
        // TODO authentication not yet implemented
        preferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.auth_token), authToken);
        editor.apply();
        if (!preferences.contains(getString(R.string.auth_token))) {
            Intent intent = new Intent(HomeActivity.this, AuthenticationActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    /**
     * Callback to fill user data fields.
     */
    private void populateUser() {
        userListener.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                            fakeText.setText(user.getUsername());
                            fakeCreated.setText(user.getCreatedAt());
                        },
                        throwable -> Log.e(TAG, "onErrorFromPopulateUser: "
                                + throwable.getMessage()));
    }

}
