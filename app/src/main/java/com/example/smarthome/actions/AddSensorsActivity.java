package com.example.smarthome.actions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.smarthome.HomeActivity;
import com.example.smarthome.R;
import com.example.smarthome.data.Sensor;
import com.example.smarthome.data.SensorModal;
import com.example.smarthome.data.sensor_specific.TemperatureHumiditySensor;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.net.Uri;

public class AddSensorsActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Button btnAddSensor;

    TextInputEditText sensorName, sensorID;

    String[] item = {"Temperature and Humidity", "Motion"};

    String sensorImage, sensorType;

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;

    ProgressBar progressBar;
    double temperature = 20.5, humidity = 60.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sensors);

        sensorName = (TextInputEditText) findViewById(R.id.sensor_name);
        sensorID = (TextInputEditText) findViewById(R.id.sensor_id);
        btnAddSensor = (Button) findViewById(R.id.btnAddSensor);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        autoCompleteTextView = findViewById(R.id.autocompleteText);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, item);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sensorType = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(AddSensorsActivity.this, "Item: " + sensorType, Toast.LENGTH_SHORT).show();
            }
        });
        firebaseDatabase = FirebaseDatabase.getInstance();
        // on below line creating our database reference.
        databaseReference = firebaseDatabase.getReference("Users");

        btnAddSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String sensor_name = sensorName.getText().toString();
                String sensor_id = sensorID.getText().toString();
                progressBar.setVisibility(View.VISIBLE);

                if(sensorType == "Temperature and Humidity")
                {
                    sensorImage = "https://firebasestorage.googleapis.com/v0/b/smarthome-a51d2.appspot.com/o/thermometer.png?alt=media&token=56eaa17a-5680-4b4e-b24c-01bc36cbab47";
                }

                //this part can be made in a function with parameters that is called specifically for each sensor
                SensorModal sensor = new SensorModal(sensor_id, sensor_name, sensorType, sensorImage);
                TemperatureHumiditySensor temperatureHumiditySensor = new TemperatureHumiditySensor(sensor, temperature, humidity);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        //on below line we are setting data in our firebase database.
                        //databaseReference.child(uid).child("Sensors").child(sensor_id).setValue(sensor);

                        //issues with adding sensor??
                        databaseReference.child(uid).child("Sensors").child(sensor_id).setValue(temperatureHumiditySensor);
                        //displaying a toast message.
                        Toast.makeText(getApplicationContext(), "Sensor Added..", Toast.LENGTH_SHORT).show();
                        //starting a main activity.
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //displaying a failure message on below line.
                        Toast.makeText(getApplicationContext(), "Fail to add Sensor..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

}