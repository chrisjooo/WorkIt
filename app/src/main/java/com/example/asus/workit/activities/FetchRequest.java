package com.example.asus.workit.activities;

import android.os.AsyncTask;
import android.util.Log;

import com.example.asus.workit.helpers.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FetchRequest extends AsyncTask<String,Void,String> {
    private String calories;

    FetchRequest(String burnedCalories) {
        this.calories = burnedCalories;
    }

    @Override
    protected String doInBackground(String... strings) {
        return NetworkUtils.getCaloryInfo(strings[0],strings[1],strings[2]);
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
    }

}
