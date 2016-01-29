package com.dunrite.xpaper.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dunrite.xpaper.R;
import com.dunrite.xpaper.utility.Utils;

/**
 * A Custom Spinner Adapter to show color previews next to each option
 */
public class CustomSpinnerAdapter extends ArrayAdapter<String>{

    private Context context;
    private Activity activity;
    private String[] objects;

    /**
     * Constructor
     *
     * @param context            current app context
     * @param textViewResourceId resource ID for the current textview
     * @param objects
     * @param activity           current activity
     */
    public CustomSpinnerAdapter(Context context, int textViewResourceId, String[] objects, Activity activity){
        super(context, textViewResourceId, objects);
        this.context = context;
        this.activity = activity;
        this.objects = objects;
    }

    /**
     * Gets custom view and places color reviews in each spot
     * @param position spot in the spinner
     * @param convertView
     * @param parent
     * @return the custom view
     */
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.color_preview_spinner_item, parent, false);

        TextView label = (TextView) layout.findViewById(R.id.label);

        label.setText(objects[position]);

        ImageView img = (ImageView) layout.findViewById(R.id.color_preview);

        switch (position) {
            case 0: //front
                img.setColorFilter(Utils.getFrontColor(activity));
                break;
            case 1: //back
                img.setColorFilter(Utils.getBackColor(activity));
                break;
            case 2: //accent
                img.setColorFilter(Utils.getAccentColor(activity));
                break;
        }

        return layout;
    }

    // It gets a View that displays in the drop down popup the data at the specified position
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // It gets a View that displays the data at the specified position
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}
