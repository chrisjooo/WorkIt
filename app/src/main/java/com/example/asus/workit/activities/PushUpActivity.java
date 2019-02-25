package com.example.asus.workit.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.example.asus.workit.model.User;
import com.example.asus.workit.sql.DatabaseHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class PushUpActivity extends AppCompatActivity {

    private Button mButtonLetsGo;
    private Button mButtonHeartRate;
    private TextView heartRateFirst;
    private LinearLayout heartRatebutton;
    private LinearLayout linearLayOutLetsGoPushUp;
    private String heartRate;
    private TextView heartRateResult;
    private TextInputEditText textInputEditTextPushUp;
    private TextInputLayout textInputLayoutPushUp;
    private Button letsgo;
    private String EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_up);

//        mButtonHeartRate = (Button) findViewById(R.id.heartRateCheck);
//        mButtonHeartRate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(PushUpActivity.this, HeartRateActivity.class);
//                startActivityForResult(i, 1);
//            }
//        });

        Intent intent = getIntent();
        EMAIL = intent.getStringExtra("EMAIL");

        letsgo = findViewById(R.id.letsgoPushUp);
        letsgo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //getting user by email
                DatabaseHelper dbHandler = new DatabaseHelper(null);
                User user = dbHandler.getUserByEmail(EMAIL);

                textInputEditTextPushUp = (TextInputEditText) findViewById(R.id.inputPushUp);
                textInputLayoutPushUp = (TextInputLayout) findViewById(R.id.inputLayoutPushUp);

                TextView errorMessage = (TextView) findViewById(R.id.errorMessagePushUp);
                if (textInputEditTextPushUp.getText().toString().matches("")) {
                    errorMessage.setText("Please enter valid input.");
                } else
                {
                    errorMessage.setText("");

                    //initiator
                    HttpURLConnection httpConn = null;
                    BufferedReader reader = null;
                    String responseJSON = null;
                    //HttpUrlConnection
                    try {
                        String queryURL = "http://localhost:8080/getCaloryPushup/?weight="+Integer.toString(user.getBodyWeight())
                                +"&total="+textInputEditTextPushUp.getText().toString();
                        URL url = new URL(queryURL);
                        httpConn = (HttpURLConnection)url.openConnection();
                        httpConn.setRequestMethod("GET");
                        httpConn.connect();

                        InputStream inputStream = httpConn.getInputStream();
                        StringBuffer buffer = new StringBuffer();

                        reader = new BufferedReader(new InputStreamReader(inputStream));
                        String line;
                        while ((line = reader.readLine()) != null) {

                            buffer.append(line + "\n");
                        }
                        responseJSON = buffer.toString();

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (httpConn != null) {
                            httpConn.disconnect();
                        }
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    String LOG_TAG = "HASIL JSON";
                    Log.d(LOG_TAG, responseJSON);
                    //parsing JSON
                    JSONObject jsonObject = new JSONObject(responseJSON);
                    JSONArray itemsArray = jsonObject.getJSONArray("items");

                    String calorie;

                    for(int i = 0; i<itemsArray.length(); i++){
                        JSONObject result = itemsArray.getJSONObject(i); //Get the current item
                        JSONObject volumeInfo = result.getJSONObject("resultInfo");

                        calorie = volumeInfo.getString("calories_total");

                    }

                    Intent i = new Intent(PushUpActivity.this, StartPushUp.class);
                    i.putExtra("total",textInputEditTextPushUp.getText().toString());
                    i.putExtra("type","pushup");
                    i.putExtra("weight",user.getBodyWeight());
                    i.putExtra("calories_total",calorie);
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

                heartRateFirst = (TextView) findViewById(R.id.textCheckHeartRate);
                heartRateFirst.setVisibility(View.GONE);
                heartRatebutton = (LinearLayout) findViewById(R.id.buttonCheckHeartRate);
                heartRatebutton.setVisibility(View.GONE);
                linearLayOutLetsGoPushUp = (LinearLayout) findViewById(R.id.linearLayOutLetsGoPushUp);
                linearLayOutLetsGoPushUp.setVisibility(View.VISIBLE);
            }
        }
    }
}
