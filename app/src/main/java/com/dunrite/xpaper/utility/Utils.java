package com.dunrite.xpaper.utility;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.io.IOException;
import java.util.List;

/**
 * Utility class for various common methods
 */
public class Utils {

    /**
     * Private empty constructor
     */
    private Utils() {
    }


    /*****************************************************************************
     * Getters
     *****************************************************************************/

    /**
     * Returns stored model value for the spinner in the ColorsFragment
     *
     * @param a activity calling
     * @return integer value of model
     */
    public static int getModel(Activity a) {
        SharedPreferences sharedPref = a.getSharedPreferences("MODEL", Context.MODE_PRIVATE);
        return sharedPref.getInt("model", 0);
    }

    /**
     * Returns stored front color value
     *
     * @param a activity calling
     * @return integer value of front
     */
    public static int getFrontColor(Activity a) {
        SharedPreferences sharedPref = a.getSharedPreferences("COLORS", Context.MODE_PRIVATE);
        return sharedPref.getInt("front", 0);
    }

    /**
     * Returns stored accent color value
     *
     * @param a activity calling
     * @return integer value of accent
     */
    public static int getAccentColor(Activity a) {
        SharedPreferences sharedPref = a.getSharedPreferences("COLORS", Context.MODE_PRIVATE);
        return sharedPref.getInt("accent", 0);
    }

    /**
     * Returns stored back color value
     *
     * @param a activity calling
     * @return integer value of back
     */
    public static int getBackColor(Activity a) {
        SharedPreferences sharedPref = a.getSharedPreferences("COLORS", Context.MODE_PRIVATE);
        return sharedPref.getInt("back", 0);
    }

    /**
     * Returns stored value set for bgColor (0=front, 1=back, 2=accent)
     *
     * @param a the current activity
     * @return background color value
     */
    public static int getBackgroundColor(Activity a) {
        SharedPreferences sharedPref = a.getSharedPreferences("WALL_CONFIG", Context.MODE_PRIVATE);
        return sharedPref.getInt("bgColor", 0);
    }

    /**
     * Returns stored value set for fgColor (0=front, 1=back, 2=accent)
     *
     * @param a the current activity
     * @return background color value
     */
    public static int getForegroundColor(Activity a) {
        SharedPreferences sharedPref = a.getSharedPreferences("WALL_CONFIG", Context.MODE_PRIVATE);
        return sharedPref.getInt("fgColor", 1); //defaults to back color
    }

    /**
     * Checks if user has purchased a premium license or not
     *
     * @param a the current activity
     * @return premium status as a boolean
     */
    public static boolean getPremiumStatus(Activity a) {
        SharedPreferences sharedPref = a.getSharedPreferences("APP_SETTINGS", Context.MODE_PRIVATE);
        return sharedPref.getBoolean("premium", false);
    }


    /*****************************************************************************
     * Saving
     *****************************************************************************/

    /**
     * Saves device configuration for later
     *
     * @param a     the activity doing the save
     * @param param integer that will be the value stored
     * @param type  determines what type of data is being saved
     * @param pref  the name of the preference
     */
    public static void saveDeviceConfig(Activity a, int param, String type, String pref) {
        SharedPreferences sharedPref = a.getSharedPreferences(pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(type, param);
        editor.apply();
    }


    /*****************************************************************************
     * Image Processing
     *****************************************************************************/

    /**
     * Combines two images into one while also coloring each separate image
     *
     * @param background the main background drawable
     * @param foreground the drawable in the front of the background
     * @param color1 color to change background to
     * @param color2 color to change foreground to
     * @param context current context
     * @return colorized and combined drawable
     *
     * can add a 3rd parameter 'String loc' if you want to save the new image.
     * left some code to do that at the bottom
     */
    public static Drawable combineImages(Drawable background, Drawable foreground,
                                         int color1, int color2, Context context) {
        Bitmap cs = null;

        //convert from drawable to bitmap
        Bitmap back = ((BitmapDrawable) background).getBitmap();
        back = back.copy(Bitmap.Config.ARGB_8888, true);

        Bitmap x = ((BitmapDrawable) foreground).getBitmap();
        x = x.copy(Bitmap.Config.ARGB_8888, true);

        //initialize Canvas
        int width = back.getWidth();
        int height = back.getHeight();
        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);

        //Filter for Background
        Paint paint1 = new Paint();
        paint1.setFilterBitmap(false);
        paint1.setColorFilter(new PorterDuffColorFilter(color1, PorterDuff.Mode.SRC_ATOP));

        //Filter for Foreground
        Paint paint2 = new Paint();
        paint2.setFilterBitmap(false);
        paint2.setColorFilter(new PorterDuffColorFilter(color2, PorterDuff.Mode.SRC_ATOP));

        //Draw both images
        comboImage.drawBitmap(back, 0, 0, paint1);
        comboImage.drawBitmap(x, 0, 0, paint2);

        return new BitmapDrawable(context.getResources(), cs);
    }

    /**
     * Applies wallpaper to device's home screen
     *
     * @param c application's conext
     * @param d drawable to apply to background
     */
    public static void applyWallpaper(Context c, Drawable d) {
        WallpaperManager wm = WallpaperManager.getInstance(c);
        try {
            Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
            if (bitmap != null)
                wm.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*****************************************************************************
     * Misc
     *****************************************************************************/

    /**
     * Converts an Integer ArrayList into an int array
     *
     * @param list    the ArrayList needing conversion
     * @param context the application context
     * @return the final array
     */
    public static int[] toIntArray(List<Integer> list, Context context) {
        int[] intArray = new int[list.size()];
        int i = 0;

        for (Integer integer : list)
            intArray[i++] = ContextCompat.getColor(context, integer);

        return intArray;
    }
}
