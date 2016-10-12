package com.slashandhyphen.saplyn.Authentication;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.slashandhyphen.saplyn.Models.Pojo.User;
import com.slashandhyphen.saplyn.R;

import rx.Observable;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = (AuthenticationActivity) getActivity();
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_register, container, false);

        layout.findViewById(R.id.button_register).setOnClickListener(this);

        return layout;
    }


    /**
     * Maybe this should be put inline.  All I think it will ever do is call the login button.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_register:
                doRegister();
                break;
            default:
                break;
        }
    }

    private void doRegister() {
        Toast.makeText(getActivity(), "Clicked register", Toast.LENGTH_SHORT).show();
    }
}
