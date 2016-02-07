package com.dunrite.xpaper.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.dunrite.xpaper.R;
import com.dunrite.xpaper.activities.ColorsActivity;
import com.dunrite.xpaper.activities.EditorActivity;
import com.dunrite.xpaper.utility.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

/**
 * Fragment to house everything to do with displaying the list of wallpapers
 */
public class ColorsFragment extends Fragment {
    Button frontButton, backButton, accentButton, shuffleButton;
    FloatingActionButton introFAB;
    ImageView frontCirc, backCirc, accCirc, devicePrev;
    Spinner modelSpinner;
    SharedPreferences sp;
    ArrayList<Integer> bColors = new ArrayList<>();
    ArrayList<Integer> aColors = new ArrayList<>();
    int[] front = {Color.BLACK, Color.WHITE};
    int[] accent;
    int[] back;
    public static String lastPicked;
    int model;
    public ColorChooserDialog.Builder frontChooser;
    public ColorChooserDialog.Builder backChooser;
    public ColorChooserDialog.Builder accentChooser;

    public ColorsFragment() {
        //mandatory empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_colors, container, false);

        //instantiate buttons
        frontButton = (Button) rootView.findViewById(R.id.front_button);
        backButton = (Button) rootView.findViewById(R.id.back_button);
        accentButton = (Button) rootView.findViewById(R.id.accent_button);
        shuffleButton = (Button) rootView.findViewById(R.id.random_button);

        //FAB only for initial setup after intro activity
        introFAB = (FloatingActionButton) rootView.findViewById(R.id.introFAB);

        //instantiate color circles
        frontCirc = (ImageView) rootView.findViewById(R.id.front_circle);
        backCirc = (ImageView) rootView.findViewById(R.id.back_circle);
        accCirc = (ImageView) rootView.findViewById(R.id.accent_circle);

        devicePrev = (ImageView) rootView.findViewById(R.id.device_preview);

        modelSpinner = (Spinner) rootView.findViewById(R.id.model_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> modelAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.models_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        modelSpinner.setAdapter(modelAdapter);
        modelSpinner.setOnItemSelectedListener(mHandler);

        //set up onClickListeners
        frontButton.setOnClickListener(fHandler);
        backButton.setOnClickListener(bHandler);
        accentButton.setOnClickListener(aHandler);
        shuffleButton.setOnClickListener(sHandler);
        introFAB.setOnClickListener(fabHandler);

        model = Utils.getModel(getActivity());


        colorPreviews();
        //initialize circle previews
        resetColors();
        colorBackPreview();

        //first launch after intro
        if (Utils.isFirstLaunch(getActivity())) {
            randomizeConfig();
        } else {
            introFAB.setVisibility(View.GONE);
        }

        return rootView;
    }


    /*****************************************************************************
     * OnClickListeners
     *****************************************************************************/

    /**
     * fabHandler
     * OnClickListener for floating action button
     */
    View.OnClickListener fabHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Utils.appHasLaunched(getActivity());
            Intent intent = new Intent(getContext(), EditorActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.stay_still, R.anim.pull_out_top);
        }
    };

    /**
     * fHandler
     * OnClickListener for front button
     */
    View.OnClickListener fHandler = new View.OnClickListener() {
        public void onClick(View v) {
            frontChooser.preselect(Utils.getFrontColor(getActivity()));
            frontChooser.show();
            lastPicked = "front";

        }
    };

    /**
     * bHandler
     * OnClickListener for back button
     */
    View.OnClickListener bHandler = new View.OnClickListener() {
        public void onClick(View v) {
            backChooser.preselect(Utils.getBackColor(getActivity()));
            backChooser.show();
            lastPicked = "back";
        }
    };

    /**
     * aHandler
     * OnClickListener for accent button
     */
    View.OnClickListener aHandler = new View.OnClickListener() {
        public void onClick(View v) {
            accentChooser.preselect(Utils.getAccentColor(getActivity()));
            accentChooser.show();
            lastPicked = "accent";
        }
    };

    /**
     * sHandler
     * OnClickListener for shuffle button
     */
    View.OnClickListener sHandler = new View.OnClickListener() {
        public void onClick(View v) {
            randomizeConfig();
        }
    };

    /**
     * Listener for device spinner
     */
    AdapterView.OnItemSelectedListener mHandler = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //only do anything if we are selecting a different model than before
            if (position != model) {
                //save and set model number
                Utils.saveDeviceConfig(getActivity(), position, "model", "MODEL");
                model = Utils.getModel(getActivity());

                //reset the color lists based on current model selected
                resetColors();
                //randomize the configuration
                randomizeConfig();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


    /*****************************************************************************
     * Color management
     *****************************************************************************/

    /**
     * Resets the color options in the color pickers This is useful for when the user changes what
     * model they are using.
     */
    public void resetColors() {
        bColors = new ArrayList<>();
        aColors = new ArrayList<>();
        fetchBackColors(bColors, model);
        back = Utils.toIntArray(bColors, getContext());

        fetchAccentColors(aColors, model);
        accent = Utils.toIntArray(aColors, getContext());
        frontChooser = new ColorChooserDialog.Builder((ColorsActivity) getActivity(), R.string.front_color)
                .customColors(front, null)
                .preselect(Utils.getFrontColor(getActivity()))
                .allowUserColorInputAlpha(false)
                .allowUserColorInput(true);
        backChooser = new ColorChooserDialog.Builder((ColorsActivity) getActivity(), R.string.back_color)
                .customColors(back, null)
                .preselect(Utils.getBackColor(getActivity()))
                .allowUserColorInputAlpha(false)
                .allowUserColorInput(true);
        accentChooser = new ColorChooserDialog.Builder((ColorsActivity) getActivity(), R.string.accent_color)
                .customColors(accent, null)
                .preselect(Utils.getAccentColor(getActivity()))
                .allowUserColorInputAlpha(false)
                .allowUserColorInput(true);
    }

    /**
     * Randomizes the device configuration
     */
    private void randomizeConfig() {
        //use color lists to generate random selection
        Random rand = new Random();
        int randomAcc = rand.nextInt(accent.length);
        int randomBack = rand.nextInt(back.length);

        //set circles to random in the list and save to configuration
        frontCirc.setColorFilter(front[0]);
        backCirc.setColorFilter(back[randomBack]);
        accCirc.setColorFilter(accent[randomAcc]);
        Utils.saveDeviceConfig(getActivity(), front[0], "front", "COLORS");
        Utils.saveDeviceConfig(getActivity(), back[randomBack], "back", "COLORS");
        Utils.saveDeviceConfig(getActivity(), accent[randomAcc], "accent", "COLORS");
        colorBackPreview();
    }

    /**
     * fetchBackColors
     * gets all of the back color IDs for each color for specified 'model'
     * and inserts them into 'list'
     */
    public void fetchBackColors(ArrayList<Integer> list, int modelNumber) {
        String modelString = Utils.modelToString(modelNumber);

        Field[] ID_Fields = R.color.class.getFields();
        for (int i = 0; i < ID_Fields.length; i++) {
            String curr = ID_Fields[i].toString();
            if ("PURE".equals(modelString) && curr.contains("pure_") ||
                    "STYLE".equals(modelString) && curr.contains("pure_") ||
                    "2014".equals(modelString) && curr.contains("x14_") ||
                    "2013".equals(modelString) && curr.contains("x13_") ||
                    "FORCE".equals(modelString) && curr.contains("force_") ||
                    "PLAY".equals(modelString) && curr.contains("play_")) {
                try {
                    list.add(ID_Fields[i].getInt(null));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * fetchAccentColors
     * gets all of the accent color IDs for each color for specified 'model'
     * and inserts them into 'list'
     */
    public void fetchAccentColors(ArrayList<Integer> list, int modelNumber) {
        String modelString = Utils.modelToString(modelNumber);

        Field[] ID_Fields = R.color.class.getFields();
        for (int i = 0; i < ID_Fields.length; i++) {
            String curr = ID_Fields[i].toString();
            if ("PURE".equals(modelString) && curr.contains("purea_") ||
                    "STYLE".equals(modelString) && curr.contains("purea_") ||
                    "2014".equals(modelString) && curr.contains("x14a_") ||
                    "2013".equals(modelString) && curr.contains("x13a_") ||
                    "FORCE".equals(modelString) && curr.contains("forcea_") ||
                    "PLAY".equals(modelString) && curr.contains("playa_")) {
                try {
                    list.add(ID_Fields[i].getInt(null));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Handles what happens when user selected a color
     *
     * @param dialog        which dialog they were in
     * @param selectedColor the selected color
     */
    public void onColorSelection(ColorChooserDialog dialog, int selectedColor) {
        switch (lastPicked) {
            case "front":
                frontCirc.setColorFilter(selectedColor);
                Utils.saveDeviceConfig(getActivity(), selectedColor, "front", "COLORS");
                break;
            case "back":
                backCirc.setColorFilter(selectedColor);
                Utils.saveDeviceConfig(getActivity(), selectedColor, "back", "COLORS");
                break;
            case "accent":
                accCirc.setColorFilter(selectedColor);
                Utils.saveDeviceConfig(getActivity(), selectedColor, "accent", "COLORS");
                break;
            default:
        }
        colorBackPreview();
    }

    /**
     * Loads device configuration and colors the previews
     */
    public void colorPreviews() {
        //select correct model
        modelSpinner.setSelection(Utils.getModel(getActivity()));
        //fill circle colors
        frontCirc.setColorFilter(Utils.getFrontColor(getActivity()));
        backCirc.setColorFilter(Utils.getBackColor(getActivity()));
        accCirc.setColorFilter(Utils.getAccentColor(getActivity()));
    }

    /**
     * Colors the back preview of the device
     */
    public void colorBackPreview() {
        model = Utils.getModel(getActivity());
        Drawable back = ContextCompat.getDrawable(getContext(), R.drawable.pureback);
        Drawable accent = ContextCompat.getDrawable(getContext(), R.drawable.pureaccent);
        Drawable deviceMisc = ContextCompat.getDrawable(getContext(), R.drawable.purerim);
        if (model == 0 || model == 1) {
            back = ContextCompat.getDrawable(getContext(), R.drawable.pureback);
            accent = ContextCompat.getDrawable(getContext(), R.drawable.pureaccent);
            deviceMisc = ContextCompat.getDrawable(getContext(), R.drawable.purerim);
        } else if (model == 2) {
            back = ContextCompat.getDrawable(getContext(), R.drawable.playback);
            accent = ContextCompat.getDrawable(getContext(), R.drawable.playaccent);
            deviceMisc = ContextCompat.getDrawable(getContext(), R.drawable.playrim);
        } else if (model == 3) {
            back = ContextCompat.getDrawable(getContext(), R.drawable.forceback);
            accent = ContextCompat.getDrawable(getContext(), R.drawable.forceaccent);
            deviceMisc = ContextCompat.getDrawable(getContext(), R.drawable.forcerim);
        } else if (model == 4) {
            back = ContextCompat.getDrawable(getContext(), R.drawable.x14back);
            accent = ContextCompat.getDrawable(getContext(), R.drawable.x14accent);
            deviceMisc = ContextCompat.getDrawable(getContext(), R.drawable.x14rim);
        } else if (model == 5) {
            back = ContextCompat.getDrawable(getContext(), R.drawable.x13back);
            accent = ContextCompat.getDrawable(getContext(), R.drawable.x13accent);
            deviceMisc = ContextCompat.getDrawable(getContext(), R.drawable.x13rim);

        }
        Drawable combinedImg = Utils.combineImages(back, accent, deviceMisc,
                Utils.getBackColor(getActivity()), Utils.getAccentColor(getActivity()), "device", getContext());
        Utils.applyImageToView(getContext(), devicePrev, combinedImg);
    }

}
