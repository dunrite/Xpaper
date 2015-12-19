package com.dunrite.xpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Recycler View Adapter to display all of the wallpapers in a list of cards
 */
public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder> {
    private static ArrayList<HashMap<String,Integer>> mDataset;
    private Context context;
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
    public RecAdapter( ArrayList<HashMap<String,Integer>> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
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

//        Picasso.with(holder.context).load(mDataset.get(position))
//                .transform(new FitToTargetViewTransformation(holder.container))
//                .into(holder.mImageView);
        HashMap<String,Integer> current = mDataset.get(position);

        if(current.get("variant") == 1) {
            holder.mImageView.setImageDrawable(combineImages(ContextCompat.getDrawable(context, current.get("background")),
                    ContextCompat.getDrawable(context, current.get("foreground")),
                    ContextCompat.getColor(context, R.color.pure_lime),
                    ContextCompat.getColor(context, R.color.pure_raspberry)));
        } else if (current.get("variant") == 2){
            holder.mImageView.setImageDrawable(combineImages(ContextCompat.getDrawable(context, current.get("background")),
                    ContextCompat.getDrawable(context, current.get("foreground")),
                    ContextCompat.getColor(context, R.color.pure_raspberry),
                    ContextCompat.getColor(context, R.color.pure_lime)));
        }
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

    public Drawable combineImages(Drawable backgroundParam, Drawable xParam, int color1, int color2) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
        Bitmap cs = null;

        //convert from drawable to bitmap
        Bitmap background = ((BitmapDrawable) backgroundParam).getBitmap();
        background = background.copy(Bitmap.Config.ARGB_8888, true);

        Bitmap x = ((BitmapDrawable) xParam).getBitmap();
        x  = x.copy(Bitmap.Config.ARGB_8888, true);

        //initialize Canvas
        int width = background.getWidth();
        int height = background.getHeight();
        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);

        //Filter for Background
        Paint paint1 = new Paint();
        paint1.setFilterBitmap(false);
        paint1.setColorFilter(new PorterDuffColorFilter(color1, PorterDuff.Mode.OVERLAY));

        //Filter for X
        Paint paint2 = new Paint();
        paint2.setFilterBitmap(false);
        paint2.setColorFilter(new PorterDuffColorFilter(color2, PorterDuff.Mode.MULTIPLY));

        //Draw both images
        comboImage.drawBitmap(background, 0, 0, paint1);
        comboImage.drawBitmap(x, 0, 0, paint2);

        return new BitmapDrawable(context.getResources(), cs);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}