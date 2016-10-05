package com.slashandhyphen.saplyn.Authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.slashandhyphen.saplyn.HomeActivity;
import com.slashandhyphen.saplyn.R;

/**
 * Created by Mike on 9/30/2016.
 *
 * If authentication is required, an intent should be made to call this activity, which will
 * handle authentication and return to the calling function after adding an auth token to the
 * sharedPreferences object.
 */
public class AuthenticationActivity extends FragmentActivity {
    private SharedPreferences preferences;

    // Debug variables
    public static final String authToken = "b1e6668141b3dd7f8b12c13ae38bb78c";

    /**
     * This calls register and login fragments to handle authentication.  It then adds an auth token
     * to the sharedPreferences object and returns to the home activity.
     *
     * @param savedInstanceState used to reload state information during lifecycle transitions
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        // For now, just supply the debug auth token and return to HomeActivity.
        storeAuthToken(authToken);
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                            finish();
    }

    /**
     * Sets an auth token string into shared preferences
     *
     * @param authToken the authentication token provisioned from the server
     */
    public void storeAuthToken(String authToken) {
        preferences = getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.auth_token), authToken);
        editor.apply();
    }
}