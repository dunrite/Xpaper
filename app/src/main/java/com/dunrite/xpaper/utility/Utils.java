package com.dunrite.xpaper.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.dunrite.xpaper.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Utility class for various common methods
 */
public class Utils {

    /**
     * Private empty constructor
     */
    private Utils() {
    }

    /**
     * Saves device configuration for later
     *
     * @param a     the activity doing the save
     * @param param integer that will be the value stored
     * @param type  determines what type of data is being saved
     */
    public static void saveDeviceConfig(Activity a, int param, String type) {
        SharedPreferences sharedPref = a.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(type, param);
        editor.apply();
    }

    /**
     * Checks if user has purchased a premium license or not
     *
     * @return premium status as a boolean
     */
    public static boolean getPremiumStatus(Activity a) {
        SharedPreferences sharedPref = a.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getBoolean("premium", false);
    }

    /**
     * fetchWallpaperIDs
     * gets all of the IDs for each drawable in the drawable folder
     * and inserts them into 'list'
     */
    public static void fetchWallpaperIDs(ArrayList<HashMap<String, Integer>> list) {
        Field[] ID_Fields = R.drawable.class.getFields();
        ArrayList<Integer> backgroundList = new ArrayList<>();
        ArrayList<Integer> foregroundList = new ArrayList<>();

        //create background and foreground list based on resource name
        for (int i = 0; i < ID_Fields.length; i++) {
            String curr = ID_Fields[i].toString();
            if (curr.contains("xpbackground")) {
                try {
                    backgroundList.add(ID_Fields[i].getInt(null));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if (curr.contains("xpforeground")) {
                try {
                    foregroundList.add(ID_Fields[i].getInt(null));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        //for each background go through and add a hash map pair for each background and foreground pair
        for (int i = 0; i < backgroundList.size(); i++) {
            for (int j = 0; j < foregroundList.size(); j++) {
                //variant 1
                HashMap<String, Integer> map1 = new HashMap<>();
                map1.put("background", backgroundList.get(i));
                map1.put("foreground", foregroundList.get(j));
                map1.put("variant", 1);

                //variant 2
                HashMap<String, Integer> map2 = new HashMap<>();
                map2.put("background", backgroundList.get(i));
                map2.put("foreground", foregroundList.get(j));
                map2.put("variant", 2);

                list.add(map1);
                list.add(map2);
            }
        }

    }
    // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
    public static Drawable combineImages (Drawable background, Drawable foreground, int color1, int color2, Context context) {
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

        //Filter for X
        Paint paint2 = new Paint();
        paint2.setFilterBitmap(false);
        paint2.setColorFilter(new PorterDuffColorFilter(color2, PorterDuff.Mode.SRC_ATOP));

        //Draw both images
        comboImage.drawBitmap(back, 0, 0, paint1);
        comboImage.drawBitmap(x, 0, 0, paint2);

        return new BitmapDrawable(context.getResources(), cs);
    }
}
