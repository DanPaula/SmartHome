package com.example.smarthome.data;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smarthome.R;
import com.squareup.picasso.Picasso;

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.ViewHolder> {

    private ArrayList<SensorModal> sensorModalArrayList;
    private Context context;
    private SensorClickInterface sensorClickInterface;
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
        holder.sensorTV.setText(sensorModal.getSensorName());
        Picasso.get().load(sensorModal.getSensorImg()).into(holder.sensorIV);
        //adding animation to recycler view item on below line.
        setAnimation(holder.itemView, position);
        holder.sensorTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorClickInterface.onCourseClick(position);
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
        private ImageView sensorIV;
        private TextView sensorTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //initializing all our variables on below line.
            sensorIV = itemView.findViewById(R.id.idSensorImage);
            sensorTV = itemView.findViewById(R.id.idSensorName);
        }
    }

    //creating a interface for on click
    public interface SensorClickInterface {
        void onCourseClick(int position);
    }
}
