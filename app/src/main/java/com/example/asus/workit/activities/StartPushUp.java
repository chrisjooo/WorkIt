package com.example.asus.workit.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.asus.workit.R;

public class StartPushUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_push_up);

        Intent intent = getIntent();
        String calories_total = intent.getStringExtra("calories_total");

        TextView mPushupResult = (TextView) findViewById(R.id.pushupResult);
        String message = "Congratulations! You've Burned "+calories_total+" calories!!";
        mPushupResult.setText(message);
    }
}
