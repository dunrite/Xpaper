package com.dunrite.xpaper.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dunrite.xpaper.ColorSelection;
import com.dunrite.xpaper.R;
import com.dunrite.xpaper.activities.ColorsActivity;

import java.util.ArrayList;

/**
 * Created by chadt on 2/2/2016.
 */
public class CustomColorChooserAdapter extends BaseAdapter {

    private ArrayList<ColorSelection> myDataset;
    private ColorsActivity activity;
    private Context context;
    private LayoutInflater inflater;

    public CustomColorChooserAdapter(Context context, ArrayList<ColorSelection> colorSelections, ColorsActivity activity) {
        myDataset = colorSelections;
        this.context = context;
        this.activity = activity;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return myDataset.size();
    }

    public ColorSelection getItem(int position) {
        return myDataset.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v;
        if (convertView == null) {
            v = inflater.inflate(R.layout.color_selection, null);
        } else {
            v = convertView;
        }

        ImageView img = (ImageView) v.findViewById(R.id.bg_image);

        if (myDataset.get(position).getColor() != 0) {
            img.setBackgroundColor(myDataset.get(position).getColor());
        } else {
            img.setImageDrawable(ContextCompat.getDrawable(context, myDataset.get(position).getTextureResId()));
        }

        return v;
    }
}
