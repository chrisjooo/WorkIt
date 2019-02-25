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

public class JoggingActivity extends AppCompatActivity {

    private Button mButtonLetsGo;
    private Button mButtonHeartRate;
    private TextView heartRateFirst;
    private LinearLayout heartRatebutton;
    private LinearLayout linearLayOutLetsGoJogging;
    private String heartRate;
    private TextView heartRateResult;
    private TextInputEditText textInputEditTextJogging;
    private TextInputLayout textInputLayoutJogging;
    private Button letsgo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogging);

        mButtonHeartRate = (Button) findViewById(R.id.heartRateCheck);
        mButtonHeartRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(JoggingActivity.this, HeartRateActivity.class);
                startActivityForResult(i, 1);
            }
        });

        letsgo = findViewById(R.id.letsgoJogging);
        letsgo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                textInputEditTextJogging= (TextInputEditText) findViewById(R.id.inputJogging);
                textInputLayoutJogging= (TextInputLayout) findViewById(R.id.inputLayoutJogging);

                TextView errorMessage = (TextView) findViewById(R.id.errorMessageJogging);
                if (textInputEditTextJogging.getText().toString().matches("")) {
                    errorMessage.setText("Please enter valid input.");
                } else
                {
                    errorMessage.setText("");

                    Intent i = new Intent(JoggingActivity.this, StartJogging.class);
                    startActivity(i);
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
                heartRateResult.setVisibility(View.VISIBLE);

                heartRateFirst = (TextView) findViewById(R.id.textCheckHeartRate);
                heartRateFirst.setVisibility(View.GONE);
                heartRatebutton = (LinearLayout) findViewById(R.id.buttonCheckHeartRate);
                heartRatebutton.setVisibility(View.GONE);


            }
        }
    }
}
