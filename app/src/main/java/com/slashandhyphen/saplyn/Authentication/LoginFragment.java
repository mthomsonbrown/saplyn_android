package com.slashandhyphen.saplyn.Authentication;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.slashandhyphen.saplyn.Models.Pojo.User;
import com.slashandhyphen.saplyn.Models.SaplynWebservice.SaplynService;
import com.slashandhyphen.saplyn.R;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    /**
     * Creates references to relevant views.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = (AuthenticationActivity) getActivity();
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_login, container, false);

        layout.findViewById(R.id.button_login).setOnClickListener(this);

        return layout;
    }

    /**
     * Maybe this should be put inline.  All I think it will ever do is call the login button.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                doLogin();
                break;
            default:
                break;
        }
    }

    /**
     * Calls the backend API with an email address and password.  After a User object is returned,
     * a call is made to activity.onAuthenticationSuccessful with the provisioned auth token.
     */
    private void doLogin() {
        SaplynService saplynService = new SaplynService();
        userListener = saplynService.loginUser(AuthenticationActivity.debugUser);
        userListener.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
                            activity.onAuthenticationSuccessful(user.getAuthToken());
                        },
                        throwable -> Log.e(TAG, "onErrorFromPopulateUser: "
                                + throwable.getMessage()));
    }
}
