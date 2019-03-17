package com.example.asus.workit.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import com.example.asus.workit.model.User;
import com.example.asus.workit.sql.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class JoggingActivity extends AppCompatActivity {

    private Button mButtonLetsGo;
    private Button mButtonHeartRate;
    private TextView heartRateFirst;
    private TextView heartRateText;
    private LinearLayout heartRatebutton;
    private LinearLayout linearLayOutLetsGoJogging;
    private String heartRate;
    private TextView heartRateResult;
    private TextInputEditText textInputEditTextJogging;
    private TextInputLayout textInputLayoutJogging;
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
        LinearLayout linearLayOutLetsGoJogging = findViewById(R.id.linearLayOutLetsGoJogging);
        linearLayOutLetsGoJogging.setBackgroundColor(colorDarkBackground);
        AppCompatImageView jogging = findViewById(R.id.jogging);
        jogging.setBackgroundColor(colorBackground);
        ImageViewCompat.setImageTintList(jogging, ColorStateList.valueOf(colorDarkBackground));
        TextView joggingTextview = findViewById(R.id.joggingTextview);
        joggingTextview.setBackgroundColor(colorBackground);
        joggingTextview.setTextColor(colorDarkBackground);
        TextView joggingTextview2 = findViewById(R.id.joggingTextview2);
        joggingTextview2.setBackgroundColor(colorBackground);
        joggingTextview2.setTextColor(colorDarkBackground);
        textInputLayoutJogging = findViewById(R.id.inputLayoutJogging);
        textInputLayoutJogging.setBackgroundColor(colorBackground);
        textInputEditTextJogging = findViewById(R.id.inputJogging);
        textInputEditTextJogging.setTextColor(colorDarkBackground); //ini jalan normal
        if(chosenGender == "man"){
            textInputEditTextJogging.setBackgroundColor(Color.parseColor("#FFD85C"));
        }else{
            textInputEditTextJogging.setBackgroundColor(Color.parseColor("#ffbbee"));
        }
        TextView joggingUnit = findViewById(R.id.joggingUnit);
        joggingUnit.setTextColor(colorDarkBackground);
        joggingUnit.setBackgroundColor(colorBackground);
        TextView errorMessageJogging = findViewById(R.id.errorMessageJogging);
        errorMessageJogging.setBackgroundColor(colorBackground);
        errorMessageJogging.setTextColor(colorDarkBackground);
        TextView textCheckHeartRate = findViewById(R.id.textCheckHeartRate);
        textCheckHeartRate.setTextColor(colorDarkBackground);
        textCheckHeartRate.setBackgroundColor(colorBackground);
        LinearLayout buttonCheckHeartRate = findViewById(R.id.buttonCheckHeartRate);
        buttonCheckHeartRate.setBackgroundColor(colorDarkBackground);
        TextView heartRateText = findViewById(R.id.heartRateText);
        heartRateText.setTextColor(colorDarkBackground);
        heartRateText.setBackgroundColor(colorBackground);
        LinearLayout joggingBackground = (LinearLayout) findViewById(R.id.joggingBackground);
        joggingBackground.setBackgroundColor(colorBackground);
        LinearLayout jogging2 = findViewById(R.id.jogging2);
        jogging2.setBackgroundColor(colorBackground);

        letsgo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO getting user by email
                DatabaseHelper dbHandler = new DatabaseHelper(context);
                User user = dbHandler.getUserByEmail(UserEmail);

                textInputEditTextJogging = (TextInputEditText) findViewById(R.id.inputJogging);
                textInputLayoutJogging = (TextInputLayout) findViewById(R.id.inputLayoutJogging);

                TextView errorMessage = (TextView) findViewById(R.id.errorMessageJogging);
                if (textInputEditTextJogging.getText().toString().matches("")) {
                    errorMessage.setText("Please enter valid input.");
                } else {
                    errorMessage.setText("");

                    String calorie = "";
                    String type = "getCaloryJogging";
                    String weight = Integer.toString(user.getBodyWeight());
//                    String weight="100";
                    String total = textInputEditTextJogging.getText().toString();

                    Log.d("EMAILNYA :",UserEmail);
                    Log.d("BERATNYA :",weight);
                    Log.d("BANYAKNYA : ",total);

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

            Intent i = new Intent(JoggingActivity.this, StartJogging.class);
            i.putExtra("total", textInputEditTextJogging.getText().toString());
            i.putExtra("type", "jogging");
            i.putExtra("calories_total", calories);
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


            }
        }
    }
}
