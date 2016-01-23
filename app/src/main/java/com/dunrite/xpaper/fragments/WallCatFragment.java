package com.dunrite.xpaper.fragments;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dunrite.xpaper.R;
import com.dunrite.xpaper.adapters.RecAdapter;

/**
 * Fragment that displays grid of wallpaper categories / themes
 */
public class WallCatFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public WallCatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView;
        rootView = inflater.inflate(R.layout.fragment_wall_cat, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(rootView.getContext(), 2);

        mRecyclerView.setLayoutManager(mLayoutManager);

        String[] myCategories = getResources().getStringArray(R.array.categories);
        int[] myThumbs = getThumbs();

        // specify an adapter
        mAdapter = new RecAdapter(myCategories, myThumbs);
        mRecyclerView.setAdapter(mAdapter);
        // Inflate the layout for this fragment
        return rootView;
    }

    /**
     * Gets integer values for category thumbnail images
     *
     * @return integer array of resource values
     */
    private int[] getThumbs() {
        TypedArray tArray = getResources().obtainTypedArray(
                R.array.cat_thumbs);
        int count = tArray.length();
        int[] ids = new int[count];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = tArray.getResourceId(i, 0);
        }
        tArray.recycle();
        return ids;
    }
}
