package com.dunrite.xpaper.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dunrite.xpaper.R;
import com.dunrite.xpaper.utility.Utils;

public class IntroSlide extends Fragment {

    private static final String ARG_LAYOUT_RES_ID = "layoutResId";

    public static IntroSlide newInstance(int layoutResId) {
        IntroSlide IntroSlide = new IntroSlide();

        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        IntroSlide.setArguments(args);

        return IntroSlide;
    }

    private int layoutResId;

    public IntroSlide() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID))
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(layoutResId, container, false);
        switch (layoutResId) {
            case R.layout.fragment_intro1:
                ImageView logo = (ImageView) rootView.findViewById(R.id.logo);
                ImageView dunrite = (ImageView) rootView.findViewById(R.id.dunrite);
                Utils.applyImageToView(getActivity(), logo, R.drawable.intro_x);
                Utils.applyImageToView(getActivity(), dunrite, R.drawable.dunriteappswhite);
                break;
            case R.layout.fragment_intro2:
                ImageView devices = (ImageView) rootView.findViewById(R.id.devices);
                Utils.applyImageToView(getActivity(), devices, R.drawable.intro_devices);
                break;
            case R.layout.fragment_intro3:
                ImageView res = (ImageView) rootView.findViewById(R.id.resolution);
                Utils.applyImageToView(getActivity(), res, R.drawable.intro_resolution);
                break;
            case R.layout.fragment_intro4:
                TextView desc = (TextView) rootView.findViewById(R.id.desc);
                ImageView colorWheel = (ImageView) rootView.findViewById(R.id.color_wheel);
                desc.setText(Utils.getDeviceNameString(getActivity()));
                Utils.applyImageToView(getActivity(), colorWheel, R.drawable.intro_colors);
                break;
        }
        return rootView;
    }

}
