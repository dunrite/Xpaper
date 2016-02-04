package com.dunrite.xpaper;

/**
 * Created by chadt on 2/2/2016.
 */
public class ColorSelection {

    private int color;
    private int textureResId;

    public ColorSelection(int color, int textureResId) {
        this.color = color;
        this.textureResId = textureResId;
    }

    public int getColor() {
        return color;
    }

    public int getTextureResId() {
        return textureResId;
    }

}
