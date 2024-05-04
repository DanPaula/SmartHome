package com.example.smarthome.actions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.smarthome.ApiService;
import com.example.smarthome.HomeActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.smarthome.R;
import com.example.smarthome.RetrofitClient;
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
    DatabaseReference databaseReference, databaseReferenceS;

    Button btnAddSensor;

    TextInputEditText sensorName, sensorID;

    String[] item = {"Temperature and Humidity", "Motion"};

    String sensorImage, sensorType;

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;

    ProgressBar progressBar;

    ApiService apiService;
    double temperature = 20.5, humidity = 60.0;

    boolean sensorAdded = false;


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
        databaseReferenceS = firebaseDatabase.getReference("Sensors");
        // Create an instance of the Retrofit service interface
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Toast.makeText(getApplicationContext(), uid, Toast.LENGTH_SHORT).show();

        // Send the user ID to the ESP8266 server
        Call<Void> call = apiService.configureDevice(uid);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("MainActivity", "User ID sent successfully");
                } else {
                    Log.e("MainActivity", "Failed to send user ID: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("MainActivity", "Failed to send user ID: " + t.getMessage());
            }
        });

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
                /*databaseReferenceS.child("Sensors").child(sensor_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            temperature = snapshot.child("temperature").getValue(Double.class);
                            humidity = snapshot.child("humidity").getValue(Double.class);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Sensor must be first activated!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/
                TemperatureHumiditySensor temperatureHumiditySensor = new TemperatureHumiditySensor(sensor, temperature, humidity);

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(sensorAdded == false)
                            {
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
                                sensorAdded = true;
                            }
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