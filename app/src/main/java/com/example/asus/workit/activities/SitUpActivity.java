package com.example.asus.workit.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
    private TextView heartRateText;
    private LinearLayout heartRatebutton;
    private LinearLayout linearLayOutLetsGoSitUp;
    private String heartRate;
    private TextView heartRateResult;
    private TextInputEditText textInputEditTextSitUp;
    private TextInputLayout textInputLayoutSitUp;
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
        LinearLayout situpLayout2 = findViewById(R.id.situpLayout2);
        situpLayout2.setBackgroundColor(colorBackground);
        ScrollView situpScrollview = findViewById(R.id.situpScrollview);
        situpScrollview.setBackgroundColor(colorBackground);
        
        mButtonHeartRate.setTextColor(colorBackground);
        letsgo.setTextColor(colorBackground);
        LinearLayout linearLayOutLetsGoSitup = findViewById(R.id.linearLayOutLetsGoSitUp);
        linearLayOutLetsGoSitup.setBackgroundColor(colorDarkBackground);
        AppCompatImageView situp = findViewById(R.id.situp);
        situp.setBackgroundColor(colorBackground);
        ImageViewCompat.setImageTintList(situp, ColorStateList.valueOf(colorDarkBackground));
        TextView situpTextview = findViewById(R.id.situpTextview);
        situpTextview.setBackgroundColor(colorBackground);
        situpTextview.setTextColor(colorDarkBackground);
        TextView situpTextview2 = findViewById(R.id.situpTextview2);
        situpTextview2.setBackgroundColor(colorBackground);
        situpTextview2.setTextColor(colorDarkBackground);
        textInputLayoutSitUp = findViewById(R.id.inputLayoutSitUp);
        textInputLayoutSitUp.setBackgroundColor(colorBackground);
        textInputEditTextSitUp = findViewById(R.id.inputSitUp);
        textInputEditTextSitUp.setTextColor(colorDarkBackground);
        textInputEditTextSitUp.setBackgroundColor(colorBackground);

        TextView situpUnit = findViewById(R.id.situpUnit);
        situpUnit.setTextColor(colorDarkBackground);
        situpUnit.setBackgroundColor(colorBackground);
        TextView errorMessageSitup = findViewById(R.id.errorMessageSitUp);
        errorMessageSitup.setBackgroundColor(colorBackground);
        errorMessageSitup.setTextColor(colorDarkBackground);
        TextView textCheckHeartRate = findViewById(R.id.textCheckHeartRate);
        textCheckHeartRate.setTextColor(colorDarkBackground);
        textCheckHeartRate.setBackgroundColor(colorBackground);
        LinearLayout buttonCheckHeartRate = findViewById(R.id.buttonCheckHeartRate);
        buttonCheckHeartRate.setBackgroundColor(colorDarkBackground);
        TextView heartRateText = findViewById(R.id.heartRateText);
        heartRateText.setTextColor(colorDarkBackground);
        heartRateText.setBackgroundColor(colorBackground);
        LinearLayout situpBackground = (LinearLayout) findViewById(R.id.situpBackground);
        situpBackground.setBackgroundColor(colorBackground);
        LinearLayout situp2 = findViewById(R.id.situp2);
        situp2.setBackgroundColor(colorBackground);

        letsgo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                textInputEditTextSitUp = (TextInputEditText) findViewById(R.id.inputSitUp);
                textInputLayoutSitUp = (TextInputLayout) findViewById(R.id.inputLayoutSitUp);
                // TODO getting user by email
                DatabaseHelper dbHandler = new DatabaseHelper(context);
                User user = dbHandler.getUserByEmail(UserEmail);

                TextView errorMessage = (TextView) findViewById(R.id.errorMessageSitUp);
                if (textInputEditTextSitUp.getText().toString().matches("")) {
                    errorMessage.setText("Please enter valid input.");
                } else {
                    errorMessage.setText("");

                    String calorie = "";
                    String type = "getCalorySitup";
                    String weight = Integer.toString(user.getBodyWeight());
//                    String weight="100";
                    String total = textInputEditTextSitUp.getText().toString();
                    new CalorieRequest(calorie).execute(type, weight, total);
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

            Intent i = new Intent(SitUpActivity.this, StartSitUp.class);
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

                heartRateText = (TextView) findViewById(R.id.heartRateText);
                heartRateText.setVisibility(View.GONE);
                heartRateFirst = (TextView) findViewById(R.id.textCheckHeartRate);
                heartRateFirst.setVisibility(View.GONE);
                heartRatebutton = (LinearLayout) findViewById(R.id.buttonCheckHeartRate);
                heartRatebutton.setVisibility(View.GONE);
            }
        }
    }
}
