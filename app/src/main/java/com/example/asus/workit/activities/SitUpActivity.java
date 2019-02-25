package com.example.asus.workit.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.workit.R;
import com.example.asus.workit.model.User;
import com.example.asus.workit.sql.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

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
    private Context context = this;
    private String calories = "";
    private String EMAIL;

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

        Intent intent = getIntent();
        EMAIL = intent.getStringExtra("EMAIL");

        letsgo = findViewById(R.id.letsgoSitUp);
        letsgo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                textInputEditTextSitUp = (TextInputEditText) findViewById(R.id.inputSitUp);
                textInputLayoutSitUp = (TextInputLayout) findViewById(R.id.inputLayoutSitUp);
                //getting user by email
                DatabaseHelper dbHandler = new DatabaseHelper(context);
                User user = dbHandler.getUserByEmail(EMAIL);

                textInputEditTextSitUp = (TextInputEditText) findViewById(R.id.inputSitUp);
                textInputLayoutSitUp = (TextInputLayout) findViewById(R.id.inputLayoutSitUp);

                TextView errorMessage = (TextView) findViewById(R.id.errorMessagePushUp);
                if (textInputEditTextSitUp.getText().toString().matches("")) {
                    errorMessage.setText("Please enter valid input.");
                } else {
                    errorMessage.setText("");

                    String calorie = "";
                    String type = "getCalorySitup";
                    String weight = Integer.toString(user.getBodyWeight());
                    String total = textInputEditTextSitUp.getText().toString();
                    new SitUpActivity.CalorieRequest(calorie).execute(type, weight, total);
                }
            }
        });
    }

    private class CalorieRequest extends FetchRequest {
        CalorieRequest(String burnedCalories) {
            super(burnedCalories);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject exercise = new JSONObject(s);
                String newResult = null;
                try {
                    newResult = exercise.getString("calories_total");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (newResult != null) {
                    calories = newResult;
                } else {
                    calories = null;
                }

            } catch (JSONException e) {
                // If onPostExecute does not receive a proper JSON string,
                // update the UI to show failed results.
                calories = null;
                e.printStackTrace();
            }

            Intent i = new Intent(PushUpActivity.this, StartPushUp.class);
            i.putExtra("total", textInputEditTextSitUp.getText().toString());
            i.putExtra("type", "situp");
            i.putExtra("calories_total", calories);
            startActivity(i);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
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
