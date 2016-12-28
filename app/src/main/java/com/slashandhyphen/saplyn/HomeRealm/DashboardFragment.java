package com.slashandhyphen.saplyn.HomeRealm;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.slashandhyphen.saplyn.R;

/**
 * Created by Mike on 12/27/2016.
 *
 * Main view for the user space.  Shows various views depending on settings chosen.
 */

public class DashboardFragment extends Fragment implements View.OnClickListener {
    private static String TAG = "~DashboardFragment~";
    RelativeLayout layout;
    HomeActivity activity;

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

    private void doLogout() {
        Toast.makeText(getActivity(), "Got to logout", Toast.LENGTH_SHORT).show();
    }

    private void doDeregister() {
        Toast.makeText(getActivity(), "Got to degreister", Toast.LENGTH_SHORT).show();
    }

    private void doEntry() {
        FragmentTransaction ft = activity.fm.beginTransaction();
        ft.replace(R.id.main_fragment_container_home, new EntryFragment());
        ft.commit();
    }
}
