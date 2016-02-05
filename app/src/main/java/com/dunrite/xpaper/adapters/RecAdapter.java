package com.dunrite.xpaper.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dunrite.xpaper.R;
import com.dunrite.xpaper.Theme;
import com.dunrite.xpaper.activities.EditorActivity;
import com.dunrite.xpaper.utility.Utils;

import java.util.ArrayList;

/**
 * Recycler View Adapter to display all of the wallpaper categories in a list of cards
 */
public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder> {
    private ArrayList<Theme> myThemes;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    // TODO: Change these to default to what they had previously selected
    private int selectedPos = 0;
    private int lastSelectedPos = 0;

    private EditorActivity mActivity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        public String currentItem;
        public ImageView mImageView;
        public ImageView mLockImage;
        public ImageView mSelectImage;
        public TextView mTextView;
        public CardView container;
        public Context context;

        public ViewHolder(Context c, View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.bg_image);
            mImageView.setOnClickListener(this);
            mSelectImage = (ImageView) v.findViewById(R.id.selected_img);
            mLockImage = (ImageView) v.findViewById(R.id.locked_img);
            mTextView = (TextView) v.findViewById(R.id.cat_name);
            container = (CardView) v.findViewById(R.id.card_view);
            context = c;
        }

        @Override
        public void onClick(View v) {

            if (mLockImage.getVisibility() != View.VISIBLE) {
                lastSelectedPos = selectedPos;
                selectedPos = getAdapterPosition();
                notifyItemChanged(selectedPos);
                notifyItemChanged(lastSelectedPos);

                Utils.saveDeviceConfig(mActivity, selectedPos, "theme", "WALL_CONFIG");
                mActivity.updatePreview();
            } else {
                Snackbar.make(v, "You need premium for this theme", Snackbar.LENGTH_SHORT)
                        .setAction("get premium", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
            }
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecAdapter(ArrayList<Theme> themes, EditorActivity activity) {
        myThemes = themes;
        mActivity = activity;
        selectedPos = Utils.getTheme(activity);
        lastSelectedPos = Utils.getTheme(activity);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(parent.getContext(), v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == selectedPos){
            holder.mSelectImage.setVisibility(View.VISIBLE);
        } else {
            holder.mSelectImage.setVisibility(View.INVISIBLE);
        }

        String catName = myThemes.get(position).getCategoryName();
        //TODO; Check if user has premium
        if (catName.startsWith("#")) {
            holder.mLockImage.setVisibility(View.VISIBLE);
            catName = catName.substring(1);
        } else {
            holder.mLockImage.setVisibility(View.INVISIBLE);
        }

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Glide.with(holder.context).load(myThemes.get(position).getThumbnail())
                .fitCenter()
                .into(holder.mImageView);

        holder.mTextView.setText(catName);
        holder.currentItem = catName;

        setAnimation(holder.container, position);
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myThemes.size();
    }
}