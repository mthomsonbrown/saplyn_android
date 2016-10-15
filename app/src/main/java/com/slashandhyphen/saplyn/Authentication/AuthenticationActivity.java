package com.slashandhyphen.saplyn.Authentication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.slashandhyphen.saplyn.HomeActivity;
import com.slashandhyphen.saplyn.Models.Pojo.User;
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
    protected FragmentManager fm = getFragmentManager();
    protected Fragment fragment;

    // Debug variables
    public static final String debugAuthToken = "b1e6668141b3dd7f8b12c13ae38bb78c";
    public static final User debugUser = new User("usernamet@test2.com", "qwertyuiop", "qwertyuiop");

    /**
     * This creates a WelcomeFragment which handles navigation between authentication options and
     * ultimately provides an auth token.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_container, new WelcomeFragment());
            ft.commit();
        }
    }

    /**
     * This is called after an auth token is provisioned by whatever fragment was deployed.  This
     * stores the auth token and then returns to the home activity.
     *
     * @param authToken the auth token provisioned by one of the fragments
     */
    protected void onAuthenticationSuccessful(String authToken) {
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