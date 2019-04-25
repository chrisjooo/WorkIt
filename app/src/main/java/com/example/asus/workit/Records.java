package com.example.asus.workit;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.BinatangJalang.WorkIt.UnityPlayerActivity;
import com.example.asus.workit.activities.PushUpActivity;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Records#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Records extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String sharedPrefFile = "com.example.asus.workit";
    private SharedPreferences mPreferences;
    private ImageView mChosenGender;
    private LinearLayout settingBackground;
    private final String GENDER_KEY = "gender";
    private final String BACKGROUND = "background";
    private final String BACKGROUND_TINT = "darkBackground";
    private final String EMAIL = "email";
    private String UserEmail;
    private String chosenGender = "man";
    private int colorDarkBackground;
    private int colorBackground;
    private Button mButtonSimulation;

    private Context ctx;

    public Records() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Records.
     */
    // TODO: Rename and change types and number of parameters
    public static Records newInstance(String param1, String param2) {
        Records fragment = new Records();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //DEFAULT VALUE SharedPreferences
        mPreferences = getActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        colorBackground = ContextCompat.getColor(ctx, R.color.colorBackground);
        colorDarkBackground= ContextCompat.getColor(ctx, R.color.maroon);;
        chosenGender = mPreferences.getString(GENDER_KEY, "man");
        UserEmail = mPreferences.getString(EMAIL, "email");
        // Restore preferences
        chosenGender = mPreferences.getString(GENDER_KEY, chosenGender);
        colorBackground = mPreferences.getInt(BACKGROUND, colorBackground);
        colorDarkBackground = mPreferences.getInt(BACKGROUND_TINT, colorDarkBackground);

        //Change background and text
        LinearLayout simulationLayout = getView().findViewById(R.id.simulationLayout);
        simulationLayout.setBackgroundColor(colorBackground);
        TextView simulationText = getView().findViewById(R.id.simulationText);
        simulationText.setBackgroundColor(colorBackground);
        simulationText.setTextColor(colorDarkBackground);
        ScrollView recordScrollview = getView().findViewById(R.id.recordScrollview);
        recordScrollview.setBackgroundColor(colorBackground);
        LinearLayout simulationLayout2 = getView().findViewById(R.id.simulationLayout2);
        simulationLayout2.setBackgroundColor(colorBackground);
        mButtonSimulation = view.findViewById(R.id.simulationButton);
        mButtonSimulation.setTextColor(colorBackground);
        mButtonSimulation.setBackgroundColor(colorDarkBackground);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_records, container, false);

        mButtonSimulation = view.findViewById(R.id.simulationButton);

        mButtonSimulation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DummyActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
    }
}
