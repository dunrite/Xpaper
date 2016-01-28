package com.dunrite.xpaper.fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.dunrite.xpaper.utility.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Fragment to house everything to do with displaying the list of wallpapers
 */
public class ColorsFragment extends Fragment {
    Button frontButton, backButton, accentButton;
    ImageView frontCirc, backCirc, accCirc, devicePrev;
    Spinner modelSpinner;
    ArrayList<Integer> bColors = new ArrayList<>();
    ArrayList<Integer> aColors = new ArrayList<>();
    int[] front = {Color.BLACK, Color.WHITE};
    String model = "PURE";
    public static String lastPicked;
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
        colorPreviews();
        resetColors();

        return rootView;
    }

    /**
     * Resets the color options in the color pickers This is useful for when the user changes what
     * model they are using.
     */
    public void resetColors() {
        bColors = new ArrayList<>();
        aColors = new ArrayList<>();
        fetchBackColors(bColors, model);
        int[] back = Utils.toIntArray(bColors, getContext());

        fetchAccentColors(aColors, model);
        int[] accent = Utils.toIntArray(aColors, getContext());
        frontChooser = new ColorChooserDialog.Builder((ColorsActivity) getActivity(), R.string.front_color)
                .customColors(front, null)
                .allowUserColorInput(false);
        backChooser = new ColorChooserDialog.Builder((ColorsActivity) getActivity(), R.string.back_color)
                .customColors(back, null)
                .allowUserColorInput(false);
        accentChooser = new ColorChooserDialog.Builder((ColorsActivity) getActivity(), R.string.accent_color)
                .customColors(accent, null)
                .allowUserColorInput(false);
        colorBackPreview();
    }

    AdapterView.OnItemSelectedListener mHandler = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    model = "PURE";
                    Utils.saveDeviceConfig(getActivity(), position, "model", "MODEL");
                    resetColors();
                    return;
                case 1:
                    model = "PURE"; //The Style is essentially the same as the Pure
                    Utils.saveDeviceConfig(getActivity(), position, "model", "MODEL");
                    resetColors();
                    return;
                case 2:
                    model = "2013";
                    Utils.saveDeviceConfig(getActivity(), position, "model", "MODEL");
                    resetColors();
                    return;
                case 3:
                    model = "2014";
                    Utils.saveDeviceConfig(getActivity(), position, "model", "MODEL");
                    resetColors();
                    return;
                default:
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    /**
     * fHandler
     * OnClickListener for front button
     */
    View.OnClickListener fHandler = new View.OnClickListener() {
        public void onClick(View v) {
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
            accentChooser.show();
            lastPicked = "accent";
        }
    };

    /**
     * fetchBackColors
     * gets all of the back color IDs for each color for specified 'model'
     * and inserts them into 'list'
     */
    public void fetchBackColors(ArrayList<Integer> list, String model) {
        Field[] ID_Fields = R.color.class.getFields();
        for (int i = 0; i < ID_Fields.length; i++) {
            String curr = ID_Fields[i].toString();
            if ("PURE".equals(model) && curr.contains("pure_") ||
                    "2014".equals(model) && curr.contains("x14_") ||
                    "2013".equals(model) && curr.contains("x13_")) {
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
    public void fetchAccentColors(ArrayList<Integer> list, String model) {
        Field[] ID_Fields = R.color.class.getFields();
        for (int i = 0; i < ID_Fields.length; i++) {
            String curr = ID_Fields[i].toString();
            if ("PURE".equals(model) && curr.contains("purea_") ||
                    "2014".equals(model) && curr.contains("x14a_") ||
                    "2013".equals(model) && curr.contains("x13a_")) {
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
        colorBackPreview();
    }

    public void colorBackPreview() {
        int model = Utils.getModel(getActivity());
        Drawable back = ContextCompat.getDrawable(getContext(), R.drawable.pureback);
        Drawable accent = ContextCompat.getDrawable(getContext(), R.drawable.pureaccent);
        if (model == 0 || model == 1) {
            back = ContextCompat.getDrawable(getContext(), R.drawable.pureback);
            accent = ContextCompat.getDrawable(getContext(), R.drawable.pureaccent);
        } else if (model == 2) {
            back = ContextCompat.getDrawable(getContext(), R.drawable.x13back);
            accent = ContextCompat.getDrawable(getContext(), R.drawable.x13accent);
        } /*else if (mdoel == 3) {
            back = ContextCompat.getDrawable(getContext(), R.drawable.x14back);
            accent = ContextCompat.getDrawable(getContext(), R.drawable.x14accent);
        }*/
        Drawable combinedImg = Utils.combineImages(back, accent, Utils.getBackColor(getActivity()), Utils.getAccentColor(getActivity()), "preview", getContext());
        devicePrev.setImageDrawable(combinedImg);
    }
}
