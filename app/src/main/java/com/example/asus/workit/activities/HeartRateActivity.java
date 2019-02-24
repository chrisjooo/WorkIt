package com.example.asus.workit.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.workit.R;

import org.w3c.dom.Text;

public class HeartRateActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private SensorEventListener heartRateEventListener;
    private static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 123;
    private ImageView fingerPrintLogo;
    private ProgressBar loading;
    private TextView loadingStatement;
    public static final String EXTRA_REPLY =
            "com.example.asus.workit.activities.REPLY";
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.asus.workit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        loadingStatement = (TextView) findViewById(R.id.fingerPrintInstruction);
        fingerPrintLogo = (ImageView) findViewById(R.id.fingerprint);
        loading = (ProgressBar) findViewById(R.id.loading);

        if (heartRateSensor == null) {
            Toast.makeText(getApplicationContext(), "This device doesn't have a compatible sensor", Toast.LENGTH_SHORT).show();
            finish();
        }

        heartRateEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                fingerPrintLogo.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                loadingStatement.setText("Hold still");

                if (event.values[0] > 0) {
                    Intent result = new Intent();
                    result.putExtra("result", String.valueOf(Math.round(event.values[0])));
                    setResult(Activity.RESULT_OK, result);
                    finish();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(heartRateEventListener, heartRateSensor, sensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(heartRateEventListener);
    }
}
