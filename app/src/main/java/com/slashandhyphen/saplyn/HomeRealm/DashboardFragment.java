package com.slashandhyphen.saplyn.HomeRealm;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.slashandhyphen.saplyn.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Mike on 12/27/2016.
 *
 * Main view for the user space.  Shows various views depending on settings chosen.
 */

public class DashboardFragment extends Fragment implements View.OnClickListener {
    private static String TAG = "~DashboardFragment~";
    RelativeLayout layout;
    HomeActivity activity;
    private static SharedPreferences preferences;

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = (HomeActivity) getActivity();
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_dashboard, container, false);

        layout.findViewById(R.id.button_logout).setOnClickListener(this);
        layout.findViewById(R.id.button_deregister).setOnClickListener(this);
        layout.findViewById(R.id.button_entry).setOnClickListener(this);

        preferences = getActivity().getSharedPreferences("CurrentUser", MODE_PRIVATE);

        return layout;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_logout:
                doLogout();
                break;
            case R.id.button_deregister:
                doDeregister();
                break;
            case R.id.button_entry:
                doEntry();
                break;
            default:
                break;
        }
    }

    /**
     * Removes the auth_token from shared preferences and then returns to the home activity
     */
    private void doLogout() {
        ((HomeActivity) getActivity()).invalidateToken();
    }

    private void doDeregister() {
        ((HomeActivity) getActivity()).deregisterUser();
    }

    private void doEntry() {
        FragmentTransaction ft = activity.fm.beginTransaction();
        ft.replace(R.id.main_fragment_container_home, new EntryFragment());
        ft.commit();
    }
}
