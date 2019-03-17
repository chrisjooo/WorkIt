package com.example.asus.workit.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.asus.workit.R;

public class StartPushUp extends AppCompatActivity {

    private String sharedPrefFile = "com.example.asus.workit";
    private ImageView mChosenGender;
    private LinearLayout settingBackground;
    private SharedPreferences mPreferences;
    private final String GENDER_KEY = "gender";
    private final String BACKGROUND = "background";
    private final String BACKGROUND_TINT = "darkBackground";
    private final String EMAIL = "email";
    private String UserEmail;
    private String chosenGender = "man";
    private int colorDarkBackground;
    private int colorBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_push_up);

        Intent intent = getIntent();
        String calories_total = intent.getStringExtra("calories_total");
        TextView mPushupResult = (TextView) findViewById(R.id.pushupResult);

        //DEFAULT VALUE SharedPreferences
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        colorBackground = ContextCompat.getColor(this, R.color.colorBackground);
        colorDarkBackground= ContextCompat.getColor(this, R.color.maroon);;
        chosenGender = mPreferences.getString(GENDER_KEY, "man");
        UserEmail = mPreferences.getString(EMAIL, "email");
        // Restore preferences
        chosenGender = mPreferences.getString(GENDER_KEY, chosenGender);
        colorBackground = mPreferences.getInt(BACKGROUND, colorBackground);
        colorDarkBackground = mPreferences.getInt(BACKGROUND_TINT, colorDarkBackground);
        UserEmail = mPreferences.getString(EMAIL, UserEmail);
        //Change All Appereance according to theme
        ScrollView pushupResultScrollview = findViewById(R.id.pushupResultScrollview);
        pushupResultScrollview.setBackgroundColor(colorBackground);
        mPushupResult.setTextColor(colorDarkBackground);
        mPushupResult.setBackgroundColor(colorBackground);

        String message = "Congratulations! You've Burned "+calories_total+" calories!!";
        mPushupResult.setText(message);
    }
}
