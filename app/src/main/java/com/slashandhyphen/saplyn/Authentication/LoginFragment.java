package com.slashandhyphen.saplyn.Authentication;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.slashandhyphen.saplyn.R;

/**
 * Created by Mike on 10/5/2016.
 *
 * Gathers credentials from the user and sends those to the backend API in order to receive an
 * auth token.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    RelativeLayout layout;
    AuthenticationActivity activity;


    /**
     * Creates references to relevant views.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                activity.onAuthenticationSuccessful(AuthenticationActivity.debugAuthToken);
                break;
            default:
                break;
        }
    }
}
