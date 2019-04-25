package com.example.asus.workit.helpers;

import android.bluetooth.BluetoothServerSocket;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.example.asus.workit.R;
import com.example.asus.workit.activities.StartJogging;
import com.example.asus.workit.activities.StartPushUp;
import com.example.asus.workit.activities.StartSitUp;

public class WorkoutActivity extends AppCompatActivity {

    InputStream inputStream;
    Button On;
    Button dc;
    String done;
    byte buffer[];
    boolean stopThread;
    String address = null;
    private ProgressDialog progress;
    String workoutType;
    String calories;
    String total;
    String weight;
    String data;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;

    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(BlueToothActivity.EXTRA_ADDRESS); //receive the address of the bluetooth device

        Intent intent = getIntent();
        workoutType = intent.getStringExtra("type");
        calories = intent.getStringExtra("calories_total");
        total = intent.getStringExtra("total");
        weight = intent.getStringExtra("weight");

        //view of the ledControl
        setContentView(R.layout.activity_workout);

        //call the widgets
        On = (Button)findViewById(R.id.done_btn);
        dc = (Button)findViewById(R.id.disconnect_btn);
        new ConnectBT().execute(); //Call the class to connect

        //commands to be sent to bluetooth
        On.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doneWorkout();
                if (workoutType == "pushup") {
                    Intent i = new Intent(WorkoutActivity.this, StartPushUp.class);
                    i.putExtra("calories_total", calories);
                    startActivity(i);
                } else if (workoutType == "situp") {
                    Intent i = new Intent(WorkoutActivity.this, StartSitUp.class);
                    i.putExtra("calories_total", calories);
                    startActivity(i);
                } else if (workoutType == "jogging") {
                    Intent i = new Intent(WorkoutActivity.this, StartJogging.class);
                    i.putExtra("calories_total", calories);
                    startActivity(i);
                }
            }
        });

        dc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                disconnectConnection();
            }
        });

    }

    private void doneWorkout()
    {
        if (btSocket!=null) {
            try {
                data = workoutType +"."+ total+","+ weight+ "^"+calories;
                btSocket.getOutputStream().write(data.toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void disconnectConnection(){
        if (btSocket!=null) {
            try {
                btSocket.close();
            } catch (IOException e) {
                msg("Error Disconnecting");
            }
        }
    }

    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_led_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(WorkoutActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}