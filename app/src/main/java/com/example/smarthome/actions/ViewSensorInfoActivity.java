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
    DatabaseReference databaseReference, databaseReferenceS;
    SensorModal sensorModal;
    private String sensorId, sensorType, sensorImg;
    Long temperature, humidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sensor_info);

        sensorName = (TextView) findViewById(R.id.idSensorName);
        sensorTemperature = (TextView) findViewById(R.id.idTemperature);
        sensorHumidity = (TextView) findViewById(R.id.idHumidity);

        firebaseDatabase = FirebaseDatabase.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        sensorModal = (SensorModal) getIntent().getParcelableExtra("sensor");

        if(sensorModal!=null){
            sensorId = sensorModal.getSensorID();
            sensorName.setText(sensorModal.getSensorName());
            sensorType = sensorModal.getSensorType();
            sensorImg = sensorModal.getSensorImg();
        }
        //databaseReferenceS = firebaseDatabase.getReference("Sensors").child(sensorId);
        databaseReference = firebaseDatabase.getReference("Users").child(uid).child("Sensors").child(sensorId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //TemperatureHumiditySensor temperatureHumiditySensor = snapshot.getValue(TemperatureHumiditySensor.class);
                humidity = snapshot.child("humidity").getValue(Long.class);
                temperature = snapshot.child("temperature").getValue(Long.class);

                sensorTemperature.setText("Temperature: " + temperature.toString() + "° C");
                sensorHumidity.setText("Humidity: " + humidity.toString());
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