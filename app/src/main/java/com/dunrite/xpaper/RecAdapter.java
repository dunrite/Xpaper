package com.dunrite.xpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Recycler View Adapter to display all of the wallpapers in a list of cards
 */
public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder> {
    private static ArrayList<Integer> mDataset;
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        // each data item is just a string in this case
        public String currentItem;
        public ImageView mImageView;
        public CardView container;
        public Context context;

        public ViewHolder(Context c, View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.bg_image);
            mImageView.setOnClickListener(this);
            container = (CardView) v.findViewById(R.id.card_view);
            context = c;
        }

        @Override
        public void onClick(View v) {
            new SetWallpaperAsyncTask(v.getContext()).execute("");
            Snackbar
                    .make(v, "Setting Wallpaper", Snackbar.LENGTH_LONG)
                    .show();
        }

        private class SetWallpaperAsyncTask extends
                AsyncTask<String, Void, String> {
            Context context;

            public SetWallpaperAsyncTask(Context context) {
                this.context = context;
            }

            @Override
            protected String doInBackground(String... params) {
                setWallpaper(currentItem);
                return "Executed";
            }

            @Override
            protected void onPostExecute(String result) {
            }

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected void onProgressUpdate(Void... values) {
            }

            private void setWallpaper(String url) {
                try {
                    WallpaperManager wpm = WallpaperManager
                            .getInstance(context);
                    InputStream ins = new URL(url).openStream();
                    wpm.setStream(ins);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecAdapter(ArrayList<Integer> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(parent.getContext(), v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Picasso.with(holder.context).load(mDataset.get(position))
                .transform(new FitToTargetViewTransformation(holder.container))
                .into(holder.mImageView);
        holder.currentItem = mDataset.get(position).toString();
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
        return mDataset.size();
    }
}