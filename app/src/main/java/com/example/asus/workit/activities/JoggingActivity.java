package com.example.asus.workit.activities;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.asus.workit.R;

public class JoggingActivity extends AppCompatActivity {

    private TextInputEditText textInputEditTextJogging;
    private TextInputLayout textInputLayoutJogging;
    private Button letsgo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogging);

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
                }
            }
        });
    }
}
