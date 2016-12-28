package com.slashandhyphen.saplyn.HomeRealm;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.slashandhyphen.saplyn.R;

/**
 * Created by Mike on 12/27/2016.
 *
 * This is the view for entering entry snippets.
 */

public class EntryFragment extends Fragment {
    private static String TAG = "~EntryFragment~";
    RelativeLayout layout;
    HomeActivity activity;

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = (HomeActivity) getActivity();
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_entry, container, false);

        return layout;
    }

}
