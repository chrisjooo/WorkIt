package com.example.asus.workit.activities;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.asus.workit.R;

public class PushUpActivity extends AppCompatActivity {

    private TextInputEditText textInputEditTextPushUp;
    private TextInputLayout textInputLayoutPushUp;
    private Button letsgo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_up);

        letsgo = findViewById(R.id.letsgoPushUp);
        letsgo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                textInputEditTextPushUp = (TextInputEditText) findViewById(R.id.inputPushUp);
                textInputLayoutPushUp = (TextInputLayout) findViewById(R.id.inputLayoutPushUp);

                TextView errorMessage = (TextView) findViewById(R.id.errorMessagePushUp);
                if (textInputEditTextPushUp.getText().toString().matches("")) {
                    errorMessage.setText("Please enter valid input.");
                } else
                {
                    errorMessage.setText("");
                }
            }
        });
    }


}
