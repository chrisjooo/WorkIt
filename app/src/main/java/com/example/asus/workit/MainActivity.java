package com.example.asus.workit;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button buttonHome;
    Button buttonRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonHome = (Button) findViewById(R.id.homeButton);
        buttonHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Home f1 = new Home();
                fragmentTransaction.add(R.id.fragment_container, f1);
                fragmentTransaction.commit();
            }
        });

        buttonRecords = (Button) findViewById(R.id.recordsButton);
        buttonRecords.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Records f2 = new Records();
                fragmentTransaction.add(R.id.fragment_container, f2);
                fragmentTransaction.commit();
            }
        });
    }
}
