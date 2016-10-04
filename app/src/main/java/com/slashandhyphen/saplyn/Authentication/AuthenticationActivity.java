package com.slashandhyphen.saplyn.Authentication;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Mike on 9/30/2016.
 *
 * If authentication is required, an intent should be made to call this activity, which will
 * handle authentication and return to the calling function after adding an auth token to the
 * sharedPreferences object.
 */
public class AuthenticationActivity extends Activity {
    private SharedPreferences preferences;
}