package com.dunrite.xpaper.activities;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
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
    private Drawable foreground;
    private SharedPreferences sp;
    private int foregroundCol = 0;
    private int backgroundCol = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        setRecentsStyle(); //sets style of tab in recents menu
        sp = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);

        //Starts IntroActivity if this is the first launch of app
        if (Utils.isFirstLaunch(this)) {
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
                //Handle on a background thread
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Drawable bm = Utils.constructWallpaper(getApplicationContext(), foreground,
                                backgroundCol, foregroundCol, false);
                        Utils.applyWallpaper(getApplicationContext(), bm);
                    }
                });
                Snackbar
                        .make(v, "Applying Wallpaper", Snackbar.LENGTH_SHORT)
                        .show();
            }
        });

        updatePreview();
    }

    /**
     * Sets custom logo on recent app menu card
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setRecentsStyle() {
        Bitmap recentsIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_recents);
        String title = "Xpaper";
        int color = ContextCompat.getColor(this, R.color.colorPrimary);

        ActivityManager.TaskDescription description =
                new ActivityManager.TaskDescription(title, recentsIcon, color);
        this.setTaskDescription(description);
    }

    @Override
    protected void onResume() {
        updatePreview();
        super.onResume();
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
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Updates the wallpaper preview with the correct colors and theme
     */
    public void updatePreview() {
        wallPreview = (ImageView) findViewById(R.id.wall_preview);
        //Determine which color is selected for background
        switch (Utils.getBackgroundColor(this)) {
            case 0: //front
                backgroundCol = Utils.getFrontColor(this);
                break;
            case 1: //back
                backgroundCol = Utils.getBackColor(this);
                break;
            case 2: //accent
                backgroundCol = Utils.getAccentColor(this);
                break;
        }
        //Determine which color is selected for foreground
        switch (Utils.getForegroundColor(this)) {
            case 0: //front
                foregroundCol = Utils.getFrontColor(this);
                break;
            case 1: //back
                foregroundCol = Utils.getBackColor(this);
                break;
            case 2: //accent
                foregroundCol = Utils.getAccentColor(this);
                break;
        }

        //get foreground from the list stored in resources
        int []foregrounds = getForegrounds();
        foreground = ContextCompat.getDrawable(this, foregrounds[Utils.getTheme(this)]);
        Drawable combined = Utils.constructWallpaper(this, foreground,
                backgroundCol, foregroundCol, true);
        Utils.applyImageToView(this, wallPreview, combined);
    }

    private int[] getForegrounds() {
        TypedArray tArray = getResources().obtainTypedArray(
                R.array.cat_foregrounds);
        int count = tArray.length();
        int[] ids = new int[count];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = tArray.getResourceId(i, 0);
        }
        tArray.recycle();
        return ids;
    }



}
