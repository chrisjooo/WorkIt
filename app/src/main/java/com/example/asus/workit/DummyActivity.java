package com.example.asus.workit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.BinatangJalang.WorkIt.UnityPlayerActivity;

public class DummyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);

//        Intent intent = new Intent(this, UnityPlayerActivity.class);

        Intent intent = getPackageManager().getLaunchIntentForPackage("com.BinatangJalang.WorkIt");
        startActivity(intent);
    }

}
