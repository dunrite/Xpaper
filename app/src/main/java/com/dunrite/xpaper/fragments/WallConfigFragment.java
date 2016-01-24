package com.dunrite.xpaper.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.dunrite.xpaper.R;
import com.dunrite.xpaper.activities.EditorActivity;
import com.dunrite.xpaper.utility.Utils;

/**
 * Fragment for wallpaper configuration options
 */
public class WallConfigFragment extends Fragment {
    private AppCompatSpinner bgSpinner, fgSpinner;
    private EditorActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView;
        rootView = inflater.inflate(R.layout.fragment_wall_config, container, false);

        activity = (EditorActivity) getActivity();

        setupSpinners(rootView);

        // Inflate the layout for this fragment
        return rootView;
    }

    /**
     * populates spinners with correct content and listeners
     *
     * @param v the current view
     */
    private void setupSpinners(View v) {

        //Setup background spinner
        bgSpinner = (AppCompatSpinner) v.findViewById(R.id.background_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        SpinnerAdapter bgAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.color_option_array, android.R.layout.simple_spinner_dropdown_item);

        bgSpinner.setAdapter(bgAdapter);
        bgSpinner.setOnItemSelectedListener(bgHandler);


        //Setup foreground spinner
        fgSpinner = (AppCompatSpinner) v.findViewById(R.id.foreground_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        SpinnerAdapter fgAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.color_option_array, android.R.layout.simple_spinner_dropdown_item);

        fgSpinner.setAdapter(fgAdapter);
        fgSpinner.setOnItemSelectedListener(fgHandler);

        loadSpinnerValues();
    }

    /**
     * Gets stored spinner selections from sharedPrefs
     */
    private void loadSpinnerValues() {
        bgSpinner.setSelection(Utils.getBackgroundColor(getActivity()));
        fgSpinner.setSelection(Utils.getForegroundColor(getActivity()));
    }

    /**
     * Handler for the background spinner
     */
    AdapterView.OnItemSelectedListener bgHandler = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Utils.saveDeviceConfig(getActivity(), position, "bgColor", "WALL_CONFIG");
            activity.updatePreview();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    /**
     * Handler for the foreground spinner
     */
    AdapterView.OnItemSelectedListener fgHandler = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Utils.saveDeviceConfig(getActivity(), position, "fgColor", "WALL_CONFIG");
            activity.updatePreview();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}
