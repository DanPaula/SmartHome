package com.example.smarthome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.Menu;

import com.example.smarthome.data.SensorAdapter;
import com.example.smarthome.data.SensorModal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity{

    private FloatingActionButton add;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    ProgressBar progressBar;

    private ArrayList<SensorModal> sensorModalArrayList;
    private SensorAdapter sensorAdapter;
    private RelativeLayout relativeLayout;
    private RecyclerView sensorView;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.idLogOut){
            //displaying a toast message on user logged out inside on click.
            Toast.makeText(getApplicationContext(), "User Logged Out", Toast.LENGTH_LONG).show();
            //on below line we are signing out our user.
            mAuth.signOut();
            //on below line we are opening our login activity.
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            this.finish();
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //on below line we are inflating our menu file for displaying our menu options.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sensorView = findViewById(R.id.idSensors);
        add = findViewById(R.id.idAddSensor);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        progressBar = findViewById(R.id.idPBLoading);
        sensorModalArrayList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();

        if(user==null){
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }
        else{
            add.setOnClickListener(new View.OnClickListener (){

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),AddSensorsActivity.class);
                    startActivity(intent);
                }
            });

            //on below line initializing our adapter class.
            sensorAdapter = new SensorAdapter(sensorModalArrayList, this, this::onSensorClick);
            //setting layout malinger to recycler view on below line.
            sensorView.setLayoutManager(new LinearLayoutManager(this));
            //setting adapter to recycler view on below line.
            sensorView.setAdapter(sensorAdapter);
            //on below line calling a method to fetch courses from database.
            getSensors();
        }
    }

    private void onSensorClick(int position) {
        //calling a method to display a bottom sheet on below line.
        //displayBottomSheet(sensorModalArrayList.get(position));
    }

    private void getSensors() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        sensorModalArrayList.clear();
        databaseReference = firebaseDatabase.getReference("Users");
        databaseReference.child(uid).child("Sensors").addChildEventListener(new ChildEventListener() {
            /**
             * This method is triggered when a new child is added to the location to which this listener was
             * added.
             *
             * @param snapshot          An immutable snapshot of the data at the new child location
             * @param previousChildName The key name of sibling location ordered before the new child. This
             *                          will be null for the first child node of a location.
             */
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //on below line we are hiding our progress bar.
                progressBar.setVisibility(View.GONE);
                //adding snapshot to our array list on below line.
                sensorModalArrayList.add(snapshot.getValue(SensorModal.class));
                //notifying our adapter that data has changed.
                sensorAdapter.notifyDataSetChanged();

            }

            /**
             * This method is triggered when the data at a child location has changed.
             *
             * @param snapshot          An immutable snapshot of the data at the new data at the child location
             * @param previousChildName The key name of sibling location ordered before the child. This will
             *                          be null for the first child node of a location.
             */
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //this method is called when new child is added we are notifying our adapter and making progress bar visibility as gone.
                sensorAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            /**
             * This method is triggered when a child is removed from the location to which this listener was
             * added.
             *
             * @param snapshot An immutable snapshot of the data at the child that was removed.
             */
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                //notifying our adapter when child is removed.
                sensorAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            /**
             * This method is triggered when a child location's priority changes. See {@link
             * DatabaseReference#setPriority(Object)} and <a
             * href="https://firebase.google.com/docs/database/android/retrieve-data#data_order"
             * target="_blank">Ordered Data</a> for more information on priorities and ordering data.
             *
             * @param snapshot          An immutable snapshot of the data at the location that moved.
             * @param previousChildName The key name of the sibling location ordered before the child
             *                          location. This will be null if this location is ordered first.
             */
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //notifying our adapter when child is moved.
                sensorAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            /**
             * This method will be triggered in the event that this listener either failed at the server, or
             * is removed as a result of the security and Firebase rules. For more information on securing
             * your data, see: <a href="https://firebase.google.com/docs/database/security/quickstart"
             * target="_blank"> Security Quickstart</a>
             *
             * @param error A description of the error that occurred
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}