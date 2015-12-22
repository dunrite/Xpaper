package com.dunrite.xpaper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.afollestad.materialdialogs.color.ColorChooserDialog;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Fragment to house everything to do with displaying the list of wallpapers
 */
public class ColorsFragment extends Fragment {
    Button frontButton, backButton, accentButton;
    ImageView frontCirc, backCirc, accCirc;
    Spinner modelSpinner;
    ArrayList<Integer> bColors = new ArrayList<>();
    ArrayList<Integer> aColors = new ArrayList<>();
    int[][] empty = {};
    String model = "PURE";
    int test = 0;
    public ColorChooserDialog.Builder frontChooser;
    public ColorChooserDialog.Builder backChooser;
    public ColorChooserDialog.Builder accentChooser;

    public ColorsFragment() {

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

        //set up onClickListeners
        frontButton.setOnClickListener(fHandler);
        backButton.setOnClickListener(bHandler);
        accentButton.setOnClickListener(aHandler);

        fetchColors(bColors, model, "back");

        frontChooser = new ColorChooserDialog.Builder((MainActivity) getActivity(), R.string.app_name);
        backChooser = new ColorChooserDialog.Builder((MainActivity) getActivity(), R.string.app_name);
        accentChooser = new ColorChooserDialog.Builder((MainActivity) getActivity(), R.string.app_name);

        return rootView;
    }

    /**
     * fHandler
     * OnClickListener for front button
     */
    View.OnClickListener fHandler = new View.OnClickListener() {
        public void onClick(View v) {
            frontChooser.show();

        }
    };

    /**
     * bHandler
     * OnClickListener for back button
     */
    View.OnClickListener bHandler = new View.OnClickListener() {
        public void onClick(View v) {
            // backCirc.setColorFilter(ContextCompat.getColor(getContext(), bColors.get(test)));
            backChooser.show();
        }
    };

    /**
     * aHandler
     * OnClickListener for accent button
     */
    View.OnClickListener aHandler = new View.OnClickListener() {
        public void onClick(View v) {
            accentChooser.show();
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

    public static void onColorSelection(ColorChooserDialog dialog, int selectedColor) {

    }
}
