package com.dunrite.xpaper.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.dunrite.xpaper.R;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Activity that is used the first time a user opens the app
 */
public class IntroActivity extends AppIntro2 {
    // Please DO NOT override onCreate. Use init
    @Override
    public void init(Bundle savedInstanceState) {

        // Add your slide's fragments here
        // AppIntro will automatically generate the dots indicator and buttons.
        //addSlide(first_fragment);
        //addSlide(second_fragment);
        //addSlide(third_fragment);
        //addSlide(fourth_fragment);

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest
        addSlide(AppIntroFragment.newInstance("Welcome to Xpaper", "Easily match your Moto X's Style", R.mipmap.ic_launcher, Color.parseColor("#35c1d3")));
        addSlide(AppIntroFragment.newInstance("Crispy", "Each wallpaper is in crisp 4K resolution", R.mipmap.ic_launcher, Color.parseColor("#35c1d3")));
        // OPTIONAL METHODS
        // Override bar/separator color
        //setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button
        //showSkipButton(false);
        showDoneButton(true);

        // Turn vibration on and set intensity
        // NOTE: you will probably need to ask VIBRATE permesssion in Manifest
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onDonePressed() {
        Intent intent = new Intent(this, EditorActivity.class); //call Intro class
        startActivity(intent);
    }
}
