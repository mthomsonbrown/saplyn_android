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

        layout.findViewById(R.id.button_login_welcome).setOnClickListener(this);
        layout.findViewById(R.id.button_register_welcome).setOnClickListener(this);

        return layout;
    }

    /**
     * Navigates between various authentication options.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login_welcome:
                login();
                break;
            case R.id.button_register_welcome:
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
        ft.replace(R.id.main_fragment_container_authentication, new LoginFragment());
        ft.commit();
    }

    /**
     * Calls the register fragment and then returns an auth token back to AuthenticationActivity.
     */
    private void register() {
        FragmentTransaction ft = activity.fm.beginTransaction();
        ft.replace(R.id.main_fragment_container_authentication, new RegisterFragment());
        ft.commit();
    }
}
