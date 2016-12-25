package com.slashandhyphen.saplyn.Authentication;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
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

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.slashandhyphen.saplyn.Models.SaplynWebservice.SaplynService.debugLvl;

/**
 * Created by Mike on 10/11/2016.
 *
 * Gathers credentials from the user and sends those to the backend API in order to receive an
 * auth token.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    private static String TAG = "~RegisterFragment~";
    RelativeLayout layout;
    AuthenticationActivity activity;
    private Observable<User> userListener;

    EditText    emailEditText,
                password,
                passwordConfirmation;
    TextView errorText;
    User user;

    SharedPreferences preferences;

    /**
     * Creates references to relevant views.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = (AuthenticationActivity) getActivity();
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_register, container, false);

        emailEditText = (EditText) layout.findViewById(R.id.email_edit_text_register);
        password = (EditText) layout.findViewById(R.id.password_edit_text_register);
        passwordConfirmation = (EditText) layout.findViewById(R.id.password_confirmation_edit_text_register);
        errorText = (TextView) layout.findViewById(R.id.error_text_register);

        layout.findViewById(R.id.register_button_register).setOnClickListener(this);

        preferences = this.getActivity().getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);

        return layout;
    }


    /**
     * Right now this only presents registration
     */
    @Override
    public void onClick(View view) {
        user = buildUser();
        switch (view.getId()) {
            case R.id.register_button_register:
                doRegister();
                break;
            default:
                break;
        }
    }

    private User buildUser() {
        user = new User(
                emailEditText.getText().toString(),
                password.getText().toString(),
                passwordConfirmation.getText().toString()
        );
        return user;
    }

    /**
     * Sends a POST request to the saplyn webservice in order to create a new user object
     */
    private void doRegister() {
        SaplynService saplynService = new SaplynService(preferences.getInt(debugLvl, -1));
        userListener = saplynService.registerUser(user);
        userListener.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                .observeOn(AndroidSchedulers.mainThread())

                // For some reason the registration response doesn't hit the throwable in the
                // subscribe function, but will call into this method.
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
                } )
                .subscribe (
                        user -> {
                            activity.onAuthenticationSuccessful(user.getAuthToken());
                        },
                        throwable -> {
                            Log.d(TAG, "I don't /think/ we should have ended up here.");
                        }
                );
    }
}
