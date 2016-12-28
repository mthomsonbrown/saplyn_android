package com.slashandhyphen.saplyn.Authentication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.slashandhyphen.saplyn.HomeRealm.HomeActivity;
import com.slashandhyphen.saplyn.R;

import static com.slashandhyphen.saplyn.Models.SaplynWebservice.SaplynService.debugLvl;


/**
 * Created by Mike on 9/30/2016.
 *
 * If authentication is required, an intent should be made to call this activity, which will
 * handle authentication and return to the calling function after adding an auth token to the
 * sharedPreferences object.
 */
public class AuthenticationActivity extends Activity {
    private SharedPreferences preferences;
    protected FragmentManager fm = getFragmentManager();
    protected Fragment fragment;
    private Toolbar toolbar;

    /**
     * This creates a WelcomeFragment which handles navigation between authentication options and
     * ultimately provides an auth token.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        preferences = getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);


        fragment = fm.findFragmentById(R.id.main_fragment_container_authentication);
        if (fragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.main_fragment_container_authentication, new WelcomeFragment());
            ft.commit();
        }
    }

    /**
     * Adds a menu xml resource to the toolbar
     * @param menu Not sure...
     * @return true for some reason.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                doSettings();
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * Debuggish thing right now to choose what endpoint
     * to hit, and possibly some canned credentials
     */
    private void doSettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Endpoint");
        CharSequence[] items = {"Production", "Personal", "Debug"};

        builder.setSingleChoiceItems(items,
                preferences.getInt(debugLvl, -1),
                (dialogInterface, item) -> {
                    SharedPreferences.Editor editor = preferences.edit();
                    switch (item) {
                        case 0:
                            editor.putInt(debugLvl, 0);
                            break;
                        case 1:
                            editor.putInt(debugLvl, 1);
                            break;
                        case 2:
                            editor.putInt(debugLvl, 2);
                            break;
                        default:
                            break;
                    }
                    editor.apply();
                    dialogInterface.dismiss();
                });

        builder.create().show();
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
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.auth_token), authToken);
        editor.apply();
    }
}