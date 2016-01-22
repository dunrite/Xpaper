package com.dunrite.xpaper.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
import com.dunrite.xpaper.activities.EditorActivity;
import com.dunrite.xpaper.utility.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to house everything to do with displaying the list of wallpapers
 */
public class ColorsFragment extends Fragment {
    Button frontButton, backButton, accentButton;
    ImageView frontCirc, backCirc, accCirc;
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
        View rootView = inflater.inflate(R.layout.content_colors, container, false);

        //instantiate buttons
        frontButton = (Button) rootView.findViewById(R.id.front_button);
        backButton = (Button) rootView.findViewById(R.id.back_button);
        accentButton = (Button) rootView.findViewById(R.id.accent_button);

        //instantiate color circles
        frontCirc = (ImageView) rootView.findViewById(R.id.front_circle);
        backCirc = (ImageView) rootView.findViewById(R.id.back_circle);
        accCirc = (ImageView) rootView.findViewById(R.id.accent_circle);

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
        loadDeviceConfig();
        resetColors();

        return rootView;
    }

    /**
     * Resets the color options in the color pickers This is useful for when the user changes what
     * model they are using.
     */
    public void resetColors() {
        Log.d("Model", model);
        bColors = new ArrayList<>();
        aColors = new ArrayList<>();
        fetchColors(bColors, model, "back");
        int[] back = toIntArray(bColors, getContext());

        fetchColors(aColors, model, "accent");
        int[] accent = toIntArray(aColors, getContext());

        frontChooser = new ColorChooserDialog.Builder((EditorActivity) getActivity(), R.string.front_color)
                .customColors(front, null)
                .allowUserColorInput(false);
        backChooser = new ColorChooserDialog.Builder((EditorActivity) getActivity(), R.string.back_color)
                .customColors(back, null)
                .allowUserColorInput(false);
        accentChooser = new ColorChooserDialog.Builder((EditorActivity) getActivity(), R.string.accent_color)
                .customColors(accent, null)
                .allowUserColorInput(false);
    }

    AdapterView.OnItemSelectedListener mHandler = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    model = "PURE";
                    resetColors();
                    Utils.saveDeviceConfig(getActivity(), position, "model");
                    return;
                case 1:
                    model = "PURE"; //The Style is essentially the same as the Pure
                    resetColors();
                    Utils.saveDeviceConfig(getActivity(), position, "model");
                    return;
                case 2:
                    model = "2013";
                    resetColors();
                    Utils.saveDeviceConfig(getActivity(), position, "model");
                    return;
                case 3:
                    model = "2014";
                    resetColors();
                    Utils.saveDeviceConfig(getActivity(), position, "model");
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
     * fetchColors
     * gets all of the IDs for each color for specified 'model'
     * and inserts them into 'list'
     */
    public void fetchColors(ArrayList<Integer> list, String model, String type) {
        Field[] ID_Fields = R.color.class.getFields();
        for (int i = 0; i < ID_Fields.length; i++) {
            String curr = ID_Fields[i].toString();
            if ("PURE".equals(model) && curr.contains("pure") ||
                    "2014".equals(model) && curr.contains("x14") ||
                    "2013".equals(model) && curr.contains("x13")) {
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
                Utils.saveDeviceConfig(getActivity(), selectedColor, "front");
                return;
            case "back":
                backCirc.setColorFilter(selectedColor);
                Utils.saveDeviceConfig(getActivity(), selectedColor, "back");
                return;
            case "accent":
                accCirc.setColorFilter(selectedColor);
                Utils.saveDeviceConfig(getActivity(), selectedColor, "accent");
                return;
            default:
        }
    }

    /**
     * Converts an Integer ArrayList into an int array
     *
     * @param list    the ArrayList needing conversion
     * @param context the application context
     * @return the final array
     */
    public static int[] toIntArray(List<Integer> list, Context context) {
        int[] intArray = new int[list.size()];
        int i = 0;

        for (Integer integer : list)
            intArray[i++] = ContextCompat.getColor(context, integer);

        return intArray;
    }

    /**
     * Loads device configuration from previous session
     */
    public void loadDeviceConfig() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int model = sharedPref.getInt("model", 0);
        int front = sharedPref.getInt("front", 0);
        int back = sharedPref.getInt("back", 0);
        int accent = sharedPref.getInt("accent", 0);
        //select correct model
        modelSpinner.setSelection(model);
        //fill circle colors
        frontCirc.setColorFilter(front);
        backCirc.setColorFilter(back);
        accCirc.setColorFilter(accent);
    }
}
