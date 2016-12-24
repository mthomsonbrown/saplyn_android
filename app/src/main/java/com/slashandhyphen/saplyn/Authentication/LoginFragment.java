package com.slashandhyphen.saplyn.Authentication;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.slashandhyphen.saplyn.Models.Pojo.User;
import com.slashandhyphen.saplyn.Models.SaplynWebservice.SaplynService;
import com.slashandhyphen.saplyn.R;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.CompositeException;
import rx.schedulers.Schedulers;

import static com.slashandhyphen.saplyn.Authentication.AuthenticationActivity.debugUser;

/**
 * Created by Mike on 10/5/2016.
 *
 * Gathers credentials from the user and sends those to the backend API in order to receive an
 * auth token.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private static String TAG = "~LoginFragment~";
    RelativeLayout layout;
    AuthenticationActivity activity;
    private Observable<User> userListener;
    EditText    emailEditText,
                password;
    TextView errorText;
    User user;

    /**
     * Creates references to relevant views.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = (AuthenticationActivity) getActivity();
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_login, container, false);

        emailEditText = (EditText) layout.findViewById(R.id.email_edit_text_login);
        password = (EditText) layout.findViewById(R.id.password_edit_text_login);
        errorText = (TextView) layout.findViewById(R.id.error_text_login);

        // Debug junk
        if(debugUser != null) {
            emailEditText.setText(debugUser.getEmail());
            password.setText(debugUser.getPassword());
        }

        layout.findViewById(R.id.login_button_login).setOnClickListener(this);

        return layout;
    }

    /**
     * Maybe this should be put inline.  All I think it will ever do is call the login button.
     */
    @Override
    public void onClick(View view) {
        user = buildUser();
        switch (view.getId()) {
            case R.id.login_button_login:
                doLogin();
                break;
            default:
                break;
        }
    }

    private User buildUser() {
        user = new User(
                emailEditText.getText().toString(),
                password.getText().toString()
        );
        return user;
    }

    /**
     * Calls the backend API with an email address and password.  After a User object is returned,
     * a call is made to activity.onAuthenticationSuccessful with the provisioned auth token.
     */
    private void doLogin() {
        SaplynService saplynService = new SaplynService();
        userListener = saplynService.loginUser(user);
        userListener.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn( throwable -> {
                    if(throwable instanceof HttpException) {
                        HttpException httpException = ((HttpException) throwable);
                        String error = null;

                        // Catch errors that the web host handles and output simple message
                        // rather than error body
                        if(httpException.code() == 503) {
                            error = httpException.getMessage();
                        }
                        else {
                            error = saplynService.getErrorMessage(httpException);
                        }

                        errorText.setText(error);
                        errorText.setVisibility(View.VISIBLE);
                    }
                    return null;
                })
                .subscribe(
                        user -> {
                            activity.onAuthenticationSuccessful(user.getAuthToken());
                        },
                        throwable -> {
                            Log.d(TAG, "I don't /think/ we should have ended up here.");
                        }
                );
    }
}
