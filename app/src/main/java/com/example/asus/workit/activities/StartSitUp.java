package com.example.asus.workit.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.asus.workit.R;

public class StartSitUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_sit_up);

        Intent intent = getIntent();
        String calories_total = intent.getStringExtra("calories_total");

        TextView mSitupResult = (TextView) findViewById(R.id.situpResult);
        String message = "Congratulations! You've Burned "+calories_total+" calories!!";
        mSitupResult.setText(message);
    }
}
