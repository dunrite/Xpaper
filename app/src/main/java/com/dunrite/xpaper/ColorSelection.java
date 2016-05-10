package com.dunrite.xpaper;

/**
 * Created by chadt on 2/2/2016.
 */
public class ColorSelection {

    private int color;
    private int textureResId;
    private int textureIconResId;
    private boolean selected;

    public ColorSelection(int color, int textureResId, int textureIconResId) {
        this.color = color;
        this.textureResId = textureResId;
        this.textureIconResId = textureIconResId;
        this.selected = false;
    }

    public int getColor() {
        return color;
    }

    public int getTextureResId() {
        return textureResId;
    }
    public int getTextureIconResId() {
        return textureIconResId;
    }

    public boolean getSelected() {
        return selected;
    }
    public void setSelected(boolean select) {this.selected = select;}
}

