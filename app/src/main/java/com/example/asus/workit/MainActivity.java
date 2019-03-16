package com.example.asus.workit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    Button buttonHome;
    Button buttonRecords;
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
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

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
        // Get EMAIL from intent
        Intent intent = getIntent();
        UserEmail = intent.getStringExtra("EMAIL");

        //TODO INSERT ALL CODE TO CHANGE BACKGROUND

        buttonHome = (Button) findViewById(R.id.homeButton);
        buttonRecords = (Button) findViewById(R.id.recordsButton);

        Home home = new Home();
        fragmentTransaction.add(R.id.fragment_container, home);
        fragmentTransaction.commit();
        buttonHome.setEnabled(false);

        buttonHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Bundle args = new Bundle();
                args.putString("EMAIL", EMAIL);

                Home f1 = new Home();
                f1.setArguments(args);

                fragmentTransaction.add(R.id.fragment_container, f1);
                fragmentTransaction.commit();

                buttonHome.setEnabled(false);
                buttonRecords.setEnabled(true);
            }
        });

        buttonRecords.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Records f2 = new Records();
                fragmentTransaction.add(R.id.fragment_container, f2);
                fragmentTransaction.commit();

                buttonHome.setEnabled(true);
                buttonRecords.setEnabled(false);
            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        Intent intent1 = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
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

    public void goToSetting(View view) {
        Intent intentSetting = new Intent(getApplicationContext(), Setting.class);
        intentSetting.putExtra("EMAIL",EMAIL);
        startActivity(intentSetting);
    }
}
