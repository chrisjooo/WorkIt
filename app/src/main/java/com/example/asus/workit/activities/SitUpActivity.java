package com.example.asus.workit.activities;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.asus.workit.R;

public class SitUpActivity extends AppCompatActivity {

    private TextInputEditText textInputEditTextSitUp;
    private TextInputLayout textInputLayoutSitUp;
    private Button letsgo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sit_up);

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
}
