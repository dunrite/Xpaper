package com.dunrite.xpaper.utility;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

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

    public static int getTheme(Activity a) {
        SharedPreferences sharedPref = a.getSharedPreferences("WALL_CONFIG", Context.MODE_PRIVATE);
        return sharedPref.getInt("theme", 0); //defaults to basic
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
     * Creates drawable for the wallpaper
     * @param context current app context
     * @param foreground foreground drawable
     * @param bgColor color of background
     * @param fgColor color of foreground
     * @param isPreview if this is for the preview or not
     * @return the final, constructed wallpaper
     */
    public static Drawable constructWallpaper(Context context, Drawable foreground,
                                              int bgColor, int fgColor, boolean isPreview) {
        final int WIDTH = 2560;
        final int HEIGHT = 1440;
        Canvas comboImage;
        Bitmap cs, fg;
        Paint fgPaint = new Paint();

        //create bitmap from foreground drawable
        fg = ((BitmapDrawable) foreground).getBitmap();

        if (isPreview)
            cs = Bitmap.createBitmap(WIDTH / 2, HEIGHT / 2, Bitmap.Config.ARGB_8888);
        else
            cs = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
        comboImage = new Canvas(cs);

        fgPaint.setFilterBitmap(false);
        fgPaint.setColorFilter(new PorterDuffColorFilter(fgColor, PorterDuff.Mode.SRC_ATOP));

        comboImage.drawRGB(Color.red(bgColor), Color.green(bgColor), Color.blue(bgColor));
        if (isPreview)
            comboImage.drawBitmap(Bitmap.createScaledBitmap(fg, WIDTH / 2, HEIGHT / 2, true), 0, 0, fgPaint);
        else
            comboImage.drawBitmap(fg, 0, 0, fgPaint);

        return new BitmapDrawable(context.getResources(), cs);
    }

    /**
     * TODO: DEPRECATE
     * Combines two images into one while also coloring each separate image
     *
     * @param background the main background drawable
     * @param foreground the drawable in the front of the background]
     * @param context current context
     * @return colorized and combined drawable
     *
     * can add a 3rd parameter 'String loc' if you want to save the new image.
     * left some code to do that at the bottom
     */
    public static Drawable combineImages(Drawable background, Drawable foreground, Drawable deviceMisc,
                                         int color1, int color2, String type, Context context) {
        Bitmap cs;
        Bitmap device = null;
        int width;
        int height;

        //convert from drawable to bitmap
        Bitmap back = ((BitmapDrawable) background).getBitmap();
        back = back.copy(Bitmap.Config.ARGB_8888, true);

        Bitmap x = ((BitmapDrawable) foreground).getBitmap();
        x = x.copy(Bitmap.Config.ARGB_8888, true);

        if (type.equals("device")) {
            device = ((BitmapDrawable) deviceMisc).getBitmap();
            device = device.copy(Bitmap.Config.ARGB_8888, true);
        }
        //initialize Canvas
        if (type.equals("preview") || type.equals("device")) {
            width = back.getWidth() / 2;
            height = back.getHeight() / 2;
        } else {
            width = back.getWidth();
            height = back.getHeight();
        }
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
        if (type.equals("preview") || type.equals("device")) {
            if (type.equals("device"))
                comboImage.drawBitmap(Bitmap.createScaledBitmap(device, device.getWidth() / 2, device.getHeight() / 2, true), 0, 0, null);
            comboImage.drawBitmap(Bitmap.createScaledBitmap(back, back.getWidth() / 2, back.getHeight() / 2, true), 0, 0, paint1);
            comboImage.drawBitmap(Bitmap.createScaledBitmap(x, x.getWidth() / 2, x.getHeight() / 2, true), 0, 0, paint2);
        } else {
            comboImage.drawBitmap(back, 0, 0, paint1);
            comboImage.drawBitmap(x, 0, 0, paint2);
        }

        return new BitmapDrawable(context.getResources(), cs);
    }

    //TODO: DEPRECATE
    public static Drawable combineImages2(Drawable background, Drawable foreground, Drawable deviceMisc,
                                         int backgroundCol, int foregroundCol, String type, Context context) {
        Bitmap cs;
        Bitmap device = null;
        int width;
        int height;

        //TEXTURE TESTING
        String textureLocation = "";
        Bitmap foregroundTexture = null;
        //TODO: will need some type of way to know which location to put the texture (foreground/background/both)
//        String textureLocation = "foreground";
//        type = "";
        //TODO: will need some type of way to know which foreground texture drawable to pull from
//        Bitmap foregroundTexture = ((BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.texture_bamboo)).getBitmap();
//        foregroundTexture = foregroundTexture.copy(Bitmap.Config.ARGB_8888, true);

        //convert from drawable to bitmap
        Bitmap back = ((BitmapDrawable) background).getBitmap();
        back = back.copy(Bitmap.Config.ARGB_8888, true);

        Bitmap fore = ((BitmapDrawable) foreground).getBitmap();
        fore = fore.copy(Bitmap.Config.ARGB_8888, true);

        if (type.equals("device")) {
            device = ((BitmapDrawable) deviceMisc).getBitmap();
            device = device.copy(Bitmap.Config.ARGB_8888, true);
        }
        //initialize Canvas
        if (type.equals("preview") || type.equals("device")) {
            width = back.getWidth() / 2;
            height = back.getHeight() / 2;
        } else {
            width = back.getWidth();
            height = back.getHeight();
        }
        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);


        Paint paint1 = new Paint();
        paint1.setFilterBitmap(false);
        //Filter for Background
        if(textureLocation.equals("background")||textureLocation.equals("both")){
            paint1.setColorFilter(new PorterDuffColorFilter(backgroundCol, PorterDuff.Mode.DST_ATOP));
        }else{
            paint1.setColorFilter(new PorterDuffColorFilter(backgroundCol, PorterDuff.Mode.SRC_ATOP));
        }

        //Filter for Foreground
        Paint paint2 = new Paint();
        paint2.setFilterBitmap(false);
        if(textureLocation.equals("foreground")||textureLocation.equals("both")){
            //DIFFICULT CASE
            //create new canvas to combine
            Canvas foreCanvas = new Canvas(fore);

            //set up paint for texture
            Paint paintTexture = new Paint();
            paintTexture.setFilterBitmap(false);
            paintTexture.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            //draw our combination
            foreCanvas.drawBitmap(foregroundTexture, 0, 0, paintTexture);

            //set up theme for outer image
            paint2.setColorFilter(new PorterDuffColorFilter(foregroundCol, PorterDuff.Mode.DST_IN));
        } else {
            paint2.setColorFilter(new PorterDuffColorFilter(foregroundCol, PorterDuff.Mode.SRC_ATOP));
        }

        //Draw both images
        if (type.equals("preview") || type.equals("device")) {
            if (type.equals("device") && device != null) {
                comboImage.drawBitmap(Bitmap.createScaledBitmap(device, device.getWidth() / 2, device.getHeight() / 2, true), 0, 0, null);
                device.recycle();
            }
            comboImage.drawBitmap(Bitmap.createScaledBitmap(back, back.getWidth() / 2, back.getHeight() / 2, true), 0, 0, paint1);
            comboImage.drawBitmap(Bitmap.createScaledBitmap(fore, fore.getWidth() / 2, fore.getHeight() / 2, true), 0, 0, paint2);

        } else {
            comboImage.drawBitmap(back, 0, 0, paint1);
            comboImage.drawBitmap(fore, 0, 0, paint2);
        }
        back.recycle();
        fore.recycle();

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
            //Testing setStream
            //ByteArrayOutputStream out = new ByteArrayOutputStream();
            //bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            //InputStream inputStream = new ByteArrayInputStream(out.toByteArray());
            //Testing setStream
            if (bitmap != null) {
                wm.setBitmap(bitmap);
                //Testing setStream
                //wm.setStream(inputStream);
                bitmap.recycle();
                //Testing setStream
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*****************************************************************************
     * Misc
     *****************************************************************************/

    //TODO: get actual value, if possible
    public static int getDeviceResWidth(Context context) {
        int width = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        Display display = wm.getDefaultDisplay();
        int density = metrics.densityDpi;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(metrics);

            width = metrics.widthPixels;
        }
        Log.d("DEVICE WIDTH", "" + width);
        return width;
    }

    //TODO: get actual value, if possible
    public static int getDeviceResHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int density = metrics.densityDpi;
        Log.d("DEVICE HEIGHT", "" + (density / 160) * metrics.heightPixels);
        return (density / 160) * metrics.heightPixels;
    }

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
