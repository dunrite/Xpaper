package com.dunrite.xpaper.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.dunrite.xpaper.R;
import com.dunrite.xpaper.adapters.ViewPagerAdapter;
import com.dunrite.xpaper.fragments.ColorsFragment;
import com.dunrite.xpaper.fragments.WallCatFragment;
import com.dunrite.xpaper.fragments.WallConfigFragment;
import com.dunrite.xpaper.utility.Utils;

public class EditorActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback {

    public static final String MyPrefs = "MyPrefs";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    ColorsFragment cfrag;

    private ImageView wallPreview;
    private Drawable background;
    private Drawable foreground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        SharedPreferences sp = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);

        //Starts IntroActivity if this is the first launch of app
        if (!sp.getBoolean("first", false)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("first", true);
            editor.apply();
            Intent intent = new Intent(this, IntroActivity.class); //call Intro class
            startActivity(intent);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.applyWallpaper(getApplicationContext(), wallPreview.getDrawable());
                Snackbar
                        .make(v, "Applying Wallpaper", Snackbar.LENGTH_SHORT)
                        .show();
            }
        });

        wallPreview = (ImageView) findViewById(R.id.wall_preview);

        //HARDCODED FOR TESTING PURPOSES
        background = ContextCompat.getDrawable(this, R.drawable.gray_xpbackground);
        foreground = ContextCompat.getDrawable(this, R.drawable.grey_x_xpforeground);
        int front = sp.getInt("front", ContextCompat.getColor(this,R.color.pure_lime));
        int back = sp.getInt("back", ContextCompat.getColor(this,R.color.pure_raspberry));
        //int accent = sharedPref.getInt("accent", 0);
        //HARDCODED FOR TESTING PURPOSES

        wallPreview.setImageDrawable(Utils.combineImages(background,foreground,front,back,this));
    }

    /**
     * Sets up the ViewPager to include certain fragments
     *
     * @param viewPager what to attach fragments to
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new WallCatFragment(), "Theme");
        adapter.addFragment(new WallConfigFragment(), "Configuration");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, int selectedColor) {
        cfrag.onColorSelection(dialog, selectedColor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.color_settings) {
            Intent intent = new Intent(this, ColorsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.pull_in_top, R.anim.stay_still);
        }

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
