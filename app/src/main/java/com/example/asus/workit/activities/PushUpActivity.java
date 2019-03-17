package com.example.asus.workit.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    private Context context = this;
    private String calories = "";

    private String sharedPrefFile = "com.example.asus.workit";
    private ImageView mChosenGender;
    private LinearLayout settingBackground;
    private SharedPreferences mPreferences;
    private final String GENDER_KEY = "gender";
    private final String BACKGROUND = "background";
    private final String BACKGROUND_TINT = "darkBackground";
    private final String EMAIL = "email";
    private String UserEmail;
    private String chosenGender = "man";
    private int colorDarkBackground;
    private int colorBackground;

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

        letsgo = findViewById(R.id.letsgoPushUp);

        //DEFAULT VALUE SharedPreferences
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        colorBackground = ContextCompat.getColor(this, R.color.colorBackground);
        colorDarkBackground= ContextCompat.getColor(this, R.color.maroon);;
        chosenGender = mPreferences.getString(GENDER_KEY, "man");
        UserEmail = mPreferences.getString(EMAIL, "email");
        // Restore preferences
        chosenGender = mPreferences.getString(GENDER_KEY, chosenGender);
        colorBackground = mPreferences.getInt(BACKGROUND, colorBackground);
        colorDarkBackground = mPreferences.getInt(BACKGROUND_TINT, colorDarkBackground);
        UserEmail = mPreferences.getString(EMAIL, UserEmail);

        //Change button and background
        mButtonHeartRate.setTextColor(colorBackground);
        letsgo.setTextColor(colorBackground);
        LinearLayout linearLayOutLetsGoPushUp = findViewById(R.id.linearLayOutLetsGoPushUp);
        linearLayOutLetsGoPushUp.setBackgroundColor(colorDarkBackground);
        AppCompatImageView pushup = findViewById(R.id.pushup);
        pushup.setBackgroundColor(colorBackground);
        ImageViewCompat.setImageTintList(pushup, ColorStateList.valueOf(colorDarkBackground));
        TextView pushupTextview = findViewById(R.id.pushupTextview);
        pushupTextview.setBackgroundColor(colorBackground);
        pushupTextview.setTextColor(colorDarkBackground);
        TextView pushupTextview2 = findViewById(R.id.pushupTextview2);
        pushupTextview2.setBackgroundColor(colorBackground);
        pushupTextview2.setTextColor(colorDarkBackground);
        textInputLayoutPushUp = findViewById(R.id.inputLayoutPushUp);
        textInputLayoutPushUp.setBackgroundColor(colorBackground);
        textInputEditTextPushUp = findViewById(R.id.inputPushUp);
        textInputEditTextPushUp.setTextColor(colorDarkBackground); //ini jalan normal
        if(chosenGender == "man"){
            textInputEditTextPushUp.setBackgroundColor(Color.parseColor("#FFD85C"));
        }else{
            textInputEditTextPushUp.setBackgroundColor(Color.parseColor("#ffbbee"));
        }
        TextView pushupUnit = findViewById(R.id.pushupUnit);
        pushupUnit.setTextColor(colorDarkBackground);
        pushupUnit.setBackgroundColor(colorBackground);
        TextView errorMessagePushUp = findViewById(R.id.errorMessagePushUp);
        errorMessagePushUp.setBackgroundColor(colorBackground);
        errorMessagePushUp.setTextColor(colorDarkBackground);
        TextView textCheckHeartRate = findViewById(R.id.textCheckHeartRate);
        textCheckHeartRate.setTextColor(colorDarkBackground);
        textCheckHeartRate.setBackgroundColor(colorBackground);
        LinearLayout buttonCheckHeartRate = findViewById(R.id.buttonCheckHeartRate);
        buttonCheckHeartRate.setBackgroundColor(colorDarkBackground);
        TextView heartRateText = findViewById(R.id.heartRateText);
        heartRateText.setTextColor(colorDarkBackground);
        heartRateText.setBackgroundColor(colorBackground);
        LinearLayout pushupBackground = (LinearLayout) findViewById(R.id.pushupBackground);
        pushupBackground.setBackgroundColor(colorBackground);
        LinearLayout pushup2 = findViewById(R.id.pushup2);
        pushup2.setBackgroundColor(colorBackground);

//        Intent intent = getIntent();
//        EMAIL = intent.getStringExtra("EMAIL");

        letsgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO getting user by email
                DatabaseHelper dbHandler = new DatabaseHelper(context);
                User user = dbHandler.getUserByEmail(UserEmail);

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
//                    String weight="100";
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
