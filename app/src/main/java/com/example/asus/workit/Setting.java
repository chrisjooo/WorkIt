package com.example.asus.workit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.asus.workit.model.User;

public class Setting extends AppCompatActivity {

    private static final String TAG = "SettingActivity";
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
        setContentView(R.layout.activity_setting);

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

        //Change man image
        mChosenGender = (AppCompatImageView) findViewById(R.id.theme_man);
        mChosenGender.setBackgroundColor(colorDarkBackground);
        ImageViewCompat.setImageTintList(mChosenGender, ColorStateList.valueOf(colorBackground));
        //Change woman image
        mChosenGender = (AppCompatImageView) findViewById(R.id.theme_woman);
        mChosenGender.setBackgroundColor(colorDarkBackground);
        ImageViewCompat.setImageTintList(mChosenGender, ColorStateList.valueOf(colorBackground));
        //Change background
        LinearLayout settingBackground = (LinearLayout) findViewById(R.id.settingBackground);
        settingBackground.setBackgroundColor(colorBackground);

        Log.d("EMAILNYA : ", UserEmail);
//        Intent intent = getIntent();
//        EMAIL = intent.getStringExtra("EMAIL");
//        Log.d("EMAIL", EMAIL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(GENDER_KEY, chosenGender);
        preferencesEditor.putInt(BACKGROUND, colorBackground);
        preferencesEditor.putInt(BACKGROUND_TINT, colorDarkBackground);
        preferencesEditor.putString(EMAIL, UserEmail);
        preferencesEditor.apply();
    }

    public void changeBackground(View view) {
        Context context = this;
        switch (view.getId()) {
            case R.id.theme_man:
                colorDarkBackground = ContextCompat.getColor(context, R.color.maroon);
                colorBackground = ContextCompat.getColor(context, R.color.colorBackground);
                //Change man image
                mChosenGender = (AppCompatImageView) findViewById(R.id.theme_man);
                mChosenGender.setBackgroundColor(colorDarkBackground);
                ImageViewCompat.setImageTintList(mChosenGender, ColorStateList.valueOf(colorBackground));

                //Change woman image
                mChosenGender = (AppCompatImageView) findViewById(R.id.theme_woman);
                mChosenGender.setBackgroundColor(colorDarkBackground);
                ImageViewCompat.setImageTintList(mChosenGender, ColorStateList.valueOf(colorBackground));

                //Change setting background
                settingBackground = (LinearLayout) findViewById(R.id.settingBackground);
                settingBackground.setBackgroundColor(colorBackground);

                chosenGender = "man";
                break;
            case R.id.theme_woman:
                colorDarkBackground = ContextCompat.getColor(context, R.color.colorDarkBackgroundWoman);
                colorBackground = ContextCompat.getColor(context, R.color.colorBackgroundWoman);
                //Change man image
                mChosenGender = (AppCompatImageView) findViewById(R.id.theme_man);
                mChosenGender.setBackgroundColor(colorDarkBackground);
                ImageViewCompat.setImageTintList(mChosenGender, ColorStateList.valueOf(colorBackground));

                //Change woman image
                mChosenGender = (AppCompatImageView) findViewById(R.id.theme_woman);
                mChosenGender.setBackgroundColor(colorDarkBackground);
                ImageViewCompat.setImageTintList(mChosenGender, ColorStateList.valueOf(colorBackground));

                //Change setting background
                settingBackground = (LinearLayout) findViewById(R.id.settingBackground);
                settingBackground.setBackgroundColor(colorBackground);


                chosenGender = "woman";


                break;
        }




    }
}
