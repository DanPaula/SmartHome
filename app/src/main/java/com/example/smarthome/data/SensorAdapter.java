package com.example.smarthome.data;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.smarthome.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.ViewHolder> {

    private ArrayList<SensorModal> sensorModalArrayList;
    private Context context;
    private SensorClickInterface sensorClickInterface;

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    int lastPos = -1;

    public SensorAdapter(ArrayList<SensorModal> sensorModalArrayList, Context context, SensorClickInterface sensorClickInterface) {
        this.sensorModalArrayList = sensorModalArrayList;
        this.context = context;
        this.sensorClickInterface = sensorClickInterface;
    }

    @NonNull
    @Override
    public SensorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating our layout file on below line.
        View view = LayoutInflater.from(context).inflate(R.layout.sensor_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //setting data to our recycler view item on below line.
        SensorModal sensorModal = sensorModalArrayList.get(position);
        holder.sensorName.setText(sensorModal.getSensorName());
        holder.sensorType.setText(sensorModal.getSensorType());

        String imagePath = sensorModal.getSensorImg();

        Glide.with(context).load(imagePath).into(holder.sensorImage);
        //adding animation to recycler view item on below line.
        setAnimation(holder.itemView, position);
        holder.sensorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorClickInterface.onSensorClick(position);
            }
        });
    }

    private void setAnimation(View itemView, int position) {
        if (position > lastPos) {
            //on below line we are setting animation.
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPos = position;
        }
    }

    @Override
    public int getItemCount() {
        return sensorModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //creating variable for our image view and text view on below line.
        private ImageView sensorImage;
        private TextView sensorName, sensorType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //initializing all our variables on below line.
            sensorImage = itemView.findViewById(R.id.idSensorImage);
            sensorName = itemView.findViewById(R.id.idSensorName);
            sensorType = itemView.findViewById(R.id.idSensorType);
        }
    }

    //creating a interface for on click
    public interface SensorClickInterface {
        void onSensorClick(int position);
    }
}
