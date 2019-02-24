package com.example.asus.workit.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.workit.R;

public class SitUpActivity extends AppCompatActivity {

    private Button mButtonLetsGo;
    private Button mButtonHeartRate;
    private TextView heartRateFirst;
    private LinearLayout heartRatebutton;
    private LinearLayout linearLayOutLetsGoSitUp;
    private String heartRate;
    private TextView heartRateResult;
    private TextInputEditText textInputEditTextSitUp;
    private TextInputLayout textInputLayoutSitUp;
    private Button letsgo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sit_up);

        mButtonHeartRate = (Button) findViewById(R.id.heartRateCheck);
        mButtonHeartRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SitUpActivity.this, HeartRateActivity.class);
                startActivityForResult(i, 1);
            }
        });

        letsgo = findViewById(R.id.letsgoSitUp);
        letsgo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                textInputEditTextSitUp = (TextInputEditText) findViewById(R.id.inputSitUp);
                textInputLayoutSitUp = (TextInputLayout) findViewById(R.id.inputLayoutSitUp);

                TextView errorMessage = (TextView) findViewById(R.id.errorMessageSitUp);
                if (textInputEditTextSitUp.getText().toString().matches("")) {
                    errorMessage.setText("Please enter valid input.");
                } else
                {
                    errorMessage.setText("");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                heartRate = data.getStringExtra("result");
                String feedback = "Your heart rate is " + heartRate + " bpm";
                heartRateResult = (TextView) findViewById(R.id.HeartRateResult);
                heartRateResult.setText(feedback);

                heartRateFirst = (TextView) findViewById(R.id.textCheckHeartRate);
                heartRateFirst.setVisibility(View.GONE);
                heartRatebutton = (LinearLayout) findViewById(R.id.buttonCheckHeartRate);
                heartRatebutton.setVisibility(View.GONE);
                linearLayOutLetsGoSitUp= (LinearLayout) findViewById(R.id.linearLayOutLetsGoSitUp);
                linearLayOutLetsGoSitUp.setVisibility(View.VISIBLE);
            }
        }
    }
}
