package com.dunrite.xpaper.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dunrite.xpaper.R;

/**
 * Fragment for wallpaper configuration options
 */
public class WallConfigFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView;
        rootView = inflater.inflate(R.layout.fragment_wall_config, container, false);


        // Inflate the layout for this fragment
        return rootView;
    }
}
