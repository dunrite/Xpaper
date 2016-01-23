package com.dunrite.xpaper.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.dunrite.xpaper.R;
import com.dunrite.xpaper.fragments.ColorsFragment;

public class ColorsActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback {
    private ColorsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors);

        getSupportActionBar().setElevation(0); //removes shadow under actionbar

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new ColorsFragment();
        fragmentTransaction.add(R.id.FragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onColorSelection(ColorChooserDialog dialog, int selectedColor) {
        fragment.onColorSelection(dialog, selectedColor);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
