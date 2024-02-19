package com.example.smarthome.actions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthome.HomeActivity;
import com.example.smarthome.R;
import com.example.smarthome.data.SensorModal;
import com.example.smarthome.data.sensor_specific.TemperatureHumiditySensor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ViewSensorInfoActivity extends AppCompatActivity {

    //aici ar putea fi o interfata cu view pentru diferiti sensor si sa faci implement la activitatea care e specifica senzorului
    TextView sensorName, sensorTemperature, sensorHumidity;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SensorModal sensorModal;
    private String sensorId, sensorType, sensorImg;
    String temperature, humidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sensor_info);

        sensorName = (TextView) findViewById(R.id.idSensorName);
        sensorTemperature = (TextView) findViewById(R.id.idTemperature);
        sensorHumidity = (TextView) findViewById(R.id.idHumidity);

        firebaseDatabase = FirebaseDatabase.getInstance();
        // on below line creating our database reference.


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //on below line we are setting data in our firebase database.
        //databaseReference.child(uid).child("Sensors").child(sensor_id).setValue(sensor);
        sensorModal = (SensorModal) getIntent().getParcelableExtra("sensor");

        if(sensorModal!=null){
            sensorId = sensorModal.getSensorID(); //this is null and creates the issue below
            sensorName.setText(sensorModal.getSensorName());
            sensorType = sensorModal.getSensorType();
            sensorImg = sensorModal.getSensorImg();
            //here also get temp and humidity??
        }

        //this is null...
        databaseReference = firebaseDatabase.getReference("Users").child(uid).child("Sensors").child(sensorId);
       // TemperatureHumiditySensor temperatureHumiditySensor = databaseReference.child(uid).child("Sensors").child(sensorId).getValue("temperature");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot sensorSnapshot: snapshot.getChildren())
                {
                    if(sensorSnapshot.getKey().equals("temperature"))
                    {
                        temperature = sensorSnapshot.child("temperature").getValue().toString();
                    }
                    else if(sensorSnapshot.getKey().equals("humidity"))
                    {
                        humidity = sensorSnapshot.child("humidity").getValue().toString();
                    }
                }

                sensorTemperature.setText(temperature);
                sensorHumidity.setText(humidity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(ViewSensorInfoActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}