package com.slashandhyphen.saplyn.HomeRealm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.slashandhyphen.saplyn.Authentication.AuthenticationActivity;
import com.slashandhyphen.saplyn.Authentication.WelcomeFragment;
import com.slashandhyphen.saplyn.Models.Pojo.User;
import com.slashandhyphen.saplyn.Models.SaplynWebservice.SaplynService;
import com.slashandhyphen.saplyn.R;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.slashandhyphen.saplyn.Models.SaplynWebservice.SaplynService.debugLvl;

/**
 * Created by Mike on 9/30/2016.
 *
 * This is the entry point for user space.  If a user is not yet authenticated on the device, the
 * authentication activity will be spawned to register them or sign them in.
 *
 * If a user has a valid auth token, they will be taken to a dashboard fragment, which will have a
 * list of relevant info (probably a list of previous posts), and an option to add entries.
 *
 * If the user chooses to add an entry, a new fragment will be added to the stack which will present
 * an entry interface.
 */
public class HomeActivity extends Activity {
    private static SharedPreferences preferences;
    private static String TAG = "~Home~";

    protected User user;
    protected FragmentManager fm = getFragmentManager();
    private Observable<User> userListener;
    private String authToken;
    private SaplynService saplynService;

    /**
     * Loads what the user sees when first entering user space.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        preferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
    }

    /**
     * Checks if an auth token is present.  If not, AuthenticationActivity is called to obtain one.
     */
    @Override
    public void onResume() {
        super.onResume();

        validateToken();
    }

    /**
     * Removes the token from local memory and then runs the token validation flow
     * TODO token stuff should probably be handled by the authentication activity
     */
    @SuppressLint("CommitPrefEdits") // Preferences change needs to be synchronous
    public void invalidateToken() {
        preferences.edit().remove(getString(R.string.auth_token)).commit();
        validateToken();
    }

    /**
     * Pull the token from shared preferences and make sure it is valid (exists)
     */
    protected void validateToken() {

        authToken = preferences.getString(getString(R.string.auth_token), "");
        saplynService = new SaplynService(preferences.getInt(debugLvl, -1), authToken);

        // Check if user already authenticated
        if (authToken.equals("")) {
            Intent intent = new Intent(HomeActivity.this, AuthenticationActivity.class);
            startActivityForResult(intent, 0);
        }
        else {
            populateUser();
        }
    }

    /**
     * Callback to fill user data fields.
     */
    private void populateUser() {
        // After user is authenticated, grab their data from the net
        // TODO This will ping the net on every resume (which is probably frequent).
        userListener = saplynService.viewUser();

        userListener.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(

                        // We got a valid user from the web.  Populate our local object and spawn
                        // the dashboard.
                        user -> {
                            this.user = user;
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.add(R.id.main_fragment_container_home, new DashboardFragment());
                            ft.commit();
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

    protected void deregisterUser() {
        Observable<ResponseBody> response = saplynService.destroyUser(user.getId());
        response.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        theResponse -> {
                            invalidateToken();
                        }
                );
    }

    protected void addEntry(String entry) {
//        userListener.sul
    }
}
