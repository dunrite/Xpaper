package com.dunrite.xpaper;

/**
 * Created by chadt on 1/25/2016.
 */
public class Theme {

    private int thumbnail;
    private String categoryName;

    public Theme (int thumbnail, String categoryName){
        this.thumbnail = thumbnail;
        this.categoryName = categoryName;
    }

    public int getThumbnail(){
        return thumbnail;
    }

    public String getCategoryName(){
        return categoryName;
    }

    public void setThumbnail(int thumbnail){
        this.thumbnail = thumbnail;
    }

    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }
}
