package com.example.asus.workit.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.workit.R;
import com.example.asus.workit.helpers.NetworkUtils;
import com.example.asus.workit.model.User;
import com.example.asus.workit.sql.DatabaseHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PushUpActivity extends AppCompatActivity {

    private Button mButtonLetsGo;
    private Button mButtonHeartRate;
    private TextView heartRateFirst;
    private TextView heartRateText;
    private LinearLayout heartRatebutton;
    private LinearLayout linearLayOutLetsGoPushUp;
    private String heartRate;
    private TextView heartRateResult;
    private TextInputEditText textInputEditTextPushUp;
    private TextInputLayout textInputLayoutPushUp;
    private Button letsgo;
    private String EMAIL;
    private Context context = this;
    private String calories = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_up);

        mButtonHeartRate = (Button) findViewById(R.id.heartRateCheck);
        mButtonHeartRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PushUpActivity.this, HeartRateActivity.class);
                startActivityForResult(i, 1);
            }
        });

        Intent intent = getIntent();
        EMAIL = intent.getStringExtra("EMAIL");

        letsgo = findViewById(R.id.letsgoPushUp);
        letsgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting user by email
                DatabaseHelper dbHandler = new DatabaseHelper(context);
                User user = dbHandler.getUserByEmail(EMAIL);

                textInputEditTextPushUp = (TextInputEditText) findViewById(R.id.inputPushUp);
                textInputLayoutPushUp = (TextInputLayout) findViewById(R.id.inputLayoutPushUp);

                TextView errorMessage = (TextView) findViewById(R.id.errorMessagePushUp);
                if (textInputEditTextPushUp.getText().toString().matches("")) {
                    errorMessage.setText("Please enter valid input.");
                } else
                {
                    errorMessage.setText("");

                    String calorie="";
                    String type="getCaloryPushup";
                    String weight= Integer.toString(user.getBodyWeight());
                    String total=textInputEditTextPushUp.getText().toString();
                    new CalorieRequest(calorie).execute(type,weight,total);
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
            i.putExtra("total",textInputEditTextPushUp.getText().toString());
            i.putExtra("type","pushup");
            i.putExtra("calories_total",calories);
            startActivity(i);
        }
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

                heartRateText = (TextView) findViewById(R.id.heartRateText);
                heartRateText.setVisibility(View.GONE);
                heartRateFirst = (TextView) findViewById(R.id.textCheckHeartRate);
                heartRateFirst.setVisibility(View.GONE);
                heartRatebutton = (LinearLayout) findViewById(R.id.buttonCheckHeartRate);
                heartRatebutton.setVisibility(View.GONE);
//                linearLayOutLetsGoPushUp = (LinearLayout) findViewById(R.id.linearLayOutLetsGoPushUp);
//                linearLayOutLetsGoPushUp.setVisibility(View.VISIBLE);
            }
        }
    }
}
