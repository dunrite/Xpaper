package com.dunrite.xpaper.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

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
            } else if (curr.contains("xpforeground")){
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
                HashMap<String,Integer> map1 = new HashMap<>();
                map1.put("background", backgroundList.get(i));
                map1.put("foreground", foregroundList.get(j));
                map1.put("variant", 1);

                //variant 2
                HashMap<String,Integer> map2 = new HashMap<>();
                map2.put("background", backgroundList.get(i));
                map2.put("foreground", foregroundList.get(j));
                map2.put("variant", 2);

                list.add(map1);
                list.add(map2);
            }
        }
    }
}
