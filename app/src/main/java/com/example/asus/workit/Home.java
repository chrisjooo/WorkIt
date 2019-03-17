package com.example.asus.workit;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.asus.workit.activities.JoggingActivity;
import com.example.asus.workit.activities.LocationService;
import com.example.asus.workit.activities.LoginActivity;
import com.example.asus.workit.activities.PushUpActivity;
import com.example.asus.workit.activities.SitUpActivity;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.SENSOR_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements SensorEventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Button mButtonPushUp;
    public Button mButtonSitUp;
    public Button mButtonJogging;
    public Button mButtonShare;
    public TextView location;
    public LocationService locationService;
    private TextView temperaturelabel;
    private SensorManager mSensorManager;
    private Sensor mTemperature;
    private final static String NOT_SUPPORTED_MESSAGE = "Sorry, sensor not available for this device.";

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

    private Context ctx;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2, String EMAIL) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString("EMAIL",EMAIL);
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

        ScrollView homeScrollview = getView().findViewById(R.id.homeScrollview);
        homeScrollview.setBackgroundColor(colorBackground);
        LinearLayout homeLayout2 = getView().findViewById(R.id.homeLayout2);
        homeLayout2.setBackgroundColor(colorBackground);
        LinearLayout pushupLayout = getView().findViewById(R.id.pushupLayout);
        pushupLayout.setBackgroundColor(colorDarkBackground);
        ImageView pushupImageLogo = getView().findViewById(R.id.pushupImageLogo);
        pushupImageLogo.setBackgroundColor(colorDarkBackground);
        ImageViewCompat.setImageTintList(pushupImageLogo, ColorStateList.valueOf(colorBackground));
        TextView pushupText = getView().findViewById(R.id.pushupText);
        pushupText.setTextColor(colorDarkBackground);
        Button pushupButton = getView().findViewById(R.id.pushupButton);
        pushupButton.setTextColor(colorDarkBackground);
        LinearLayout situpLayout = getView().findViewById(R.id.situpLayout);
        situpLayout.setBackgroundColor(colorDarkBackground);
        ImageView situpImageLogo = getView().findViewById(R.id.situpImageLogo);
        situpImageLogo.setBackgroundColor(colorDarkBackground);
        ImageViewCompat.setImageTintList(situpImageLogo, ColorStateList.valueOf(colorBackground));
        TextView situpText = getView().findViewById(R.id.situpText);
        situpText.setTextColor(colorDarkBackground);
        Button situpButton = getView().findViewById(R.id.situpButton);
        situpButton.setTextColor(colorDarkBackground);
        LinearLayout joggingLayout = getView().findViewById(R.id.joggingLayout);
        joggingLayout.setBackgroundColor(colorDarkBackground);
        ImageView joggingImageLogo = getView().findViewById(R.id.joggingImageLogo);
        joggingImageLogo.setBackgroundColor(colorDarkBackground);
        ImageViewCompat.setImageTintList(joggingImageLogo, ColorStateList.valueOf(colorBackground));
        TextView joggingText = getView().findViewById(R.id.joggingText);
        joggingText.setTextColor(colorDarkBackground);
        Button joggingButton = getView().findViewById(R.id.joggingButton);
        joggingButton.setTextColor(colorDarkBackground);
        LinearLayout bottomHomeLayout = getView().findViewById(R.id.bottomHomeLayout);
        bottomHomeLayout.setBackgroundColor(colorBackground);
        LinearLayout temperatureLayout = getView().findViewById(R.id.temperatureLayout);
        temperatureLayout.setBackgroundColor(colorDarkBackground);
        ImageView temperatureImageLogo = getView().findViewById(R.id.temperatureImageLogo);
        temperatureImageLogo.setBackgroundColor(colorDarkBackground);
        ImageViewCompat.setImageTintList(temperatureImageLogo, ColorStateList.valueOf(colorBackground));
        TextView temperatureText = getView().findViewById(R.id.temperature);
        temperatureText.setTextColor(colorDarkBackground);
        LinearLayout locationLayout = getView().findViewById(R.id.locationLayout);
        locationLayout.setBackgroundColor(colorDarkBackground);
        ImageView locationImageLogo = getView().findViewById(R.id.locationImageLogo);
        locationImageLogo.setBackgroundColor(colorDarkBackground);
        ImageViewCompat.setImageTintList(locationImageLogo, ColorStateList.valueOf(colorBackground));
        TextView locationText = getView().findViewById(R.id.location);
        locationText.setTextColor(colorDarkBackground);
        LinearLayout sharelayout = getView().findViewById(R.id.shareLayout);
        sharelayout.setBackgroundColor(colorDarkBackground);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

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

        temperaturelabel = (TextView) view.findViewById(R.id.temperature);
        mSensorManager = (SensorManager) getActivity().getSystemService (SENSOR_SERVICE);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mTemperature= mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE); // requires API level 14.
        }
        if (mTemperature == null) {
            temperaturelabel.setText(NOT_SUPPORTED_MESSAGE);
        }

        mButtonPushUp = view.findViewById(R.id.pushupButton);
        mButtonSitUp = view.findViewById(R.id.situpButton);
        mButtonJogging = view.findViewById(R.id.joggingButton);
        mButtonShare = view.findViewById(R.id.shareButton);

        mButtonPushUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PushUpActivity.class);
                startActivity(i);
            }
        });

        mButtonSitUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SitUpActivity.class);
                startActivity(i);
            }
        });

        mButtonJogging.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), JoggingActivity.class);
                startActivity(i);
            }
        });

        mButtonShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String txt = "I'm doing some workout with WorkIt! app!";
                String mimeType = "text/plain";

                ShareCompat.IntentBuilder
                        .from(Home.super.getActivity())
                        .setType(mimeType)
                        .setChooserTitle("Share this text with: ")
                        .setText(txt)
                        .startChooser();
            }

        });

        location = (TextView) view.findViewById(R.id.location);
        locationService = new LocationService(getContext());
        String cityName = "You are working out at " + locationService.getCity(getContext(), locationService.latitude, locationService.longitude) + ", which is a good place to work out!";
        location.setText(cityName);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(EMAIL, UserEmail);
        preferencesEditor.apply();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float ambient_temperature = event.values[0];
        Log.d("INI SUHUNYAAAAAAAAA!!!!", String.valueOf(event.values[0]));
        temperaturelabel.setText(String.valueOf(ambient_temperature) + " Celsius");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
