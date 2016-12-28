package com.slashandhyphen.saplyn.HomeRealm;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.slashandhyphen.saplyn.R;

/**
 * Created by Mike on 12/27/2016.
 *
 * This is the view for entering entry snippets.
 */

public class EntryFragment extends Fragment implements View.OnClickListener {
    private static String TAG = "~EntryFragment~";
    RelativeLayout layout;
    HomeActivity activity;
    EditText textEntry;

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = (HomeActivity) getActivity();
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_entry, container, false);

        textEntry = (EditText) getActivity().findViewById(R.id.edit_entry);
        layout.findViewById(R.id.button_submit_entry).setOnClickListener(this);

        return layout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_submit_entry:
                doAddEntry();
                break;
            default:
                break;
        }
    }

    /**
     * Handles requests to add an entry text to a database.
     */
    private void doAddEntry() {
        Toast.makeText(getActivity(), "You tried to add an entry!", Toast.LENGTH_SHORT).show();
    }
}
