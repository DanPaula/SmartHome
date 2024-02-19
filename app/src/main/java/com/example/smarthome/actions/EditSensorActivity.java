package com.example.smarthome.actions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.smarthome.HomeActivity;
import com.example.smarthome.MainActivity;
import com.example.smarthome.R;
import com.example.smarthome.data.SensorModal;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditSensorActivity extends AppCompatActivity {

    private TextInputEditText sensorName, sensorTypeT;
    //AutoCompleteTextView autoCompleteTextView;
    //ArrayAdapter<String> adapterItems;
    //String[] item = {"Temperature and Humidity", "Motion"};
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    SensorModal sensorModal;
    private String sensorId, sensorType, sensorImg;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sensor);

        Button addSensorButton = findViewById(R.id.idBtnAddSensor);
        sensorName = findViewById(R.id.idEditSensorName);
        //autoCompleteTextView = findViewById(R.id.autocompleteTextEdit);
        //adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, item);
        //autoCompleteTextView.setAdapter(adapterItems);

        progressBar = findViewById(R.id.idProgressLoadingEdit);
        Button deleteSensorButton = findViewById(R.id.idBtnDeleteSensor);

        sensorModal = (SensorModal) getIntent().getParcelableExtra("sensor");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        if(sensorModal!=null){
            sensorId = sensorModal.getSensorID();
            sensorName.setText(sensorModal.getSensorName());
            sensorType = sensorModal.getSensorType();
            sensorImg = sensorModal.getSensorImg();
            // NEW SOLUTION HERE!!!
            //autoCompleteTextView.setText(sensorModal.getSensorType());
        }


        addSensorButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                String sensorNameS = sensorName.getText().toString();

                //THIS IS NULL AND THAT IS WHY TYPE IS NOT written
//                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        sensorTypeS = adapterView.getItemAtPosition(i).toString();
//                        //Toast.makeText(AddSensorsActivity.this, "Item: " + sensorType, Toast.LENGTH_SHORT).show();
//                    }
//                });
                //I have to write something like this https://stackoverflow.com/questions/48576560/how-to-update-child-node-data-in-firebase-database
                Map<String, Object> map = new HashMap<>();
                map.put("sensorID", sensorId);
                map.put("sensorImg", sensorImg);
                map.put("sensorName", sensorNameS);
                map.put("sensorType", sensorType);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(View.GONE);
                        databaseReference.child(uid).child("Sensors").child(sensorId).updateChildren(map);
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //displaying a failure message on toast.
                        Toast.makeText(EditSensorActivity.this, "Fail to update sensor..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        deleteSensorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calling a method to delete a course.
                deleteSensor();
            }
        });
    }

    private void deleteSensor() {
        //on below line calling a method to delete the course.
        databaseReference.child(uid).child("Sensors").child(sensorId).removeValue();
        //displaying a toast message on below line.
        Toast.makeText(this, "Sensor Deleted..", Toast.LENGTH_SHORT).show();
        //opening a main activity on below line.
        startActivity(new Intent(EditSensorActivity.this, MainActivity.class));
    }
}