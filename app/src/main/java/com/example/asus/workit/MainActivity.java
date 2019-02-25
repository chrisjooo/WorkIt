package com.example.asus.workit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button buttonHome;
    Button buttonRecords;
    private String EMAIL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Intent intent = getIntent();
        EMAIL = intent.getStringExtra("EMAIL");

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

                Home f1 = new Home();
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

    public void goToSetting(View view) {
        Intent intentSetting = new Intent(getApplicationContext(), Setting.class);
        intentSetting.putExtra("EMAIL",EMAIL);
        startActivity(intentSetting);
    }
}
