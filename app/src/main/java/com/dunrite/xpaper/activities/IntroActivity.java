package com.dunrite.xpaper.activities;

import android.content.Intent;
import android.os.Bundle;

import com.dunrite.xpaper.R;
import com.dunrite.xpaper.fragments.IntroSlide;
import com.github.paolorotolo.appintro.AppIntro2;

/**
 * Activity that is used the first time a user opens the app
 */
public class IntroActivity extends AppIntro2 {
    // Please DO NOT override onCreate. Use init
    @Override
    public void init(Bundle savedInstanceState) {

        addSlide(IntroSlide.newInstance(R.layout.fragment_intro1));
        addSlide(IntroSlide.newInstance(R.layout.fragment_intro2));
        addSlide(IntroSlide.newInstance(R.layout.fragment_intro3));
        addSlide(IntroSlide.newInstance(R.layout.fragment_intro4));

        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onDonePressed() {
        Intent intent = new Intent(this, ColorsActivity.class); //call Intro class
        startActivity(intent);
    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onSlideChanged() {

    }
}
