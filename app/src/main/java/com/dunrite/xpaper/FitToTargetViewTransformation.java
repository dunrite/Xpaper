package com.dunrite.xpaper;

import android.graphics.Bitmap;
import android.view.View;

import com.squareup.picasso.Transformation;

/**
 * Picasso Transformation class to fit image to target View size
 */
public class FitToTargetViewTransformation implements Transformation {
    private View view;

    public FitToTargetViewTransformation(View view) {
        this.view = view;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int targetWidth = view.getWidth();

        double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
        int targetHeight = (int) (targetWidth * aspectRatio);
        if (source.getHeight() >= source.getWidth()) {
            return source;
        }
        Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
        if (result != source) {
            // Same bitmap is returned if sizes are the same
            source.recycle();
        }
        return result;
    }

    @Override
    public String key() {
        return "transformation" + " desiredWidth";
    }
}