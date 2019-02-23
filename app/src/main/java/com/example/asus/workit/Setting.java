package com.example.asus.workit;

import android.content.Context;
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

public class Setting extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.asus.workit";
    private String chosenGender = "man";
    private static final String TAG = "SettingActivity";
    private final String GENDER_KEY = "gender";
    private final String BACKGROUND = "background";
    private final String BACKGROUND_TINT = "darkBackground";
    private ImageView mChosenGender;
    int colorDarkBackground;
    int colorBackground;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        colorBackground = ContextCompat.getColor(this, R.color.colorBackground);
        colorDarkBackground= ContextCompat.getColor(this, R.color.maroon);;
        chosenGender = mPreferences.getString(GENDER_KEY, "man");
        // Restore preferences

        chosenGender = mPreferences.getString(GENDER_KEY, chosenGender);
        colorBackground = mPreferences.getInt(BACKGROUND, colorBackground);
        colorDarkBackground = mPreferences.getInt(BACKGROUND_TINT, colorDarkBackground);

        //Change man image
        mChosenGender = (AppCompatImageView) findViewById(R.id.theme_man);
        mChosenGender.setBackgroundColor(colorDarkBackground);
        ImageViewCompat.setImageTintList(mChosenGender, ColorStateList.valueOf(colorBackground));
        //Change woman image
        mChosenGender = (AppCompatImageView) findViewById(R.id.theme_woman);
        mChosenGender.setBackgroundColor(colorDarkBackground);
        ImageViewCompat.setImageTintList(mChosenGender, ColorStateList.valueOf(colorBackground));

    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(GENDER_KEY, chosenGender);
        preferencesEditor.putInt(BACKGROUND, colorBackground);
        preferencesEditor.putInt(BACKGROUND_TINT, colorDarkBackground);
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

                chosenGender = "woman";


                break;
        }




    }
}
