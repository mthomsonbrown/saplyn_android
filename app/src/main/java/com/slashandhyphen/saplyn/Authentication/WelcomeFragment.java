package com.slashandhyphen.saplyn.Authentication;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.slashandhyphen.saplyn.R;

/**
 * Created by Mike on 10/5/2016.
 *
 * This fragment presents the user with UI to choose authentication options.
 */
public class WelcomeFragment extends Fragment implements View.OnClickListener {

    RelativeLayout layout;
    AuthenticationActivity activity;

    /**
     * Creates references to relevant views.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (AuthenticationActivity) getActivity();
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_welcome, container, false);

        layout.findViewById(R.id.button_welcome_login).setOnClickListener(this);
        layout.findViewById(R.id.button_welcome_register).setOnClickListener(this);

        return layout;
    }

    /**
     * Navigates between various authentication options.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_welcome_login:
                login();
                break;
            case R.id.button_welcome_register:
                register();
                break;
            default:
                break;
        }
    }

    /**
     * Calls the login fragment and then returns an auth token back to AuthenticationActivity.
     */
    private void login() {
        FragmentTransaction ft = activity.fm.beginTransaction();
        ft.replace(R.id.fragment_container, new LoginFragment());
        ft.commit();
    }

    /**
     * Calls the register fragment and then returns an auth token back to AuthenticationActivity.
     */
    private void register() {
        FragmentTransaction ft = activity.fm.beginTransaction();
        ft.replace(R.id.fragment_container, new RegisterFragment());
        ft.commit();
    }
}
