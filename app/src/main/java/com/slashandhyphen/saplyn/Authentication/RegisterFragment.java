package com.slashandhyphen.saplyn.Authentication;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.slashandhyphen.saplyn.Models.Pojo.User;
import com.slashandhyphen.saplyn.Models.SaplynWebservice.SaplynService;
import com.slashandhyphen.saplyn.R;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    SaplynService saplynService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = (AuthenticationActivity) getActivity();
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_register, container, false);

        layout.findViewById(R.id.register_button_register).setOnClickListener(this);

        saplynService = new SaplynService();

        return layout;
    }


    /**
     * Right now this only presents registration
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_button_register:
                doRegister();
                break;
            default:
                break;
        }
    }

    /**
     * Sends a POST request to the saplyn webservice in order to create a new user object
     */
    private void doRegister() {
        userListener = saplynService.registerUser(AuthenticationActivity.debugUser);
        userListener.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
                            activity.onAuthenticationSuccessful(user.getAuthToken());
                        },
                        throwable -> {
                            if(throwable instanceof HttpException) {
                                ResponseBody body = ((HttpException) throwable).response().errorBody();
                                try {
                                    // TODO Should definitely handle errors better than a string compare
                                    String error = body.string();
                                    if(error.contains("Email address already registered")) {
                                        // cannot pass body.string() here or will
                                        // get "Content-Length and stream length disagree" error
                                        Toast.makeText(getActivity(),
                                                "Email address already registered",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } catch (IOException e) {
                                    Log.d(TAG, "doRegister: There were an exception");
                                    e.printStackTrace();
                                }
                            }
                        }
                );
    }
}
