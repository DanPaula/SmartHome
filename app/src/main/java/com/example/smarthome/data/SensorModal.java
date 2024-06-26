package com.example.smarthome.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class SensorModal implements Parcelable{

    private String sensorName;
    private String sensorType;
    private String sensorImg;
    private String sensorID;

    public SensorModal(String sensorName, String sensorType, String sensorImg) {
        this.sensorName = sensorName;
        this.sensorType = sensorType;
        this.sensorImg = sensorImg;
    }

    protected SensorModal(Parcel in) {
        sensorID = in.readString();
        sensorName = in.readString();
        sensorType = in.readString();
        sensorImg = in.readString();
    }

    public SensorModal(String sensorId, String sensorName, String sensorType, String sensorImage) {
        this.sensorID = sensorId;
        this.sensorName = sensorName;
        this.sensorType = sensorType;
        this.sensorImg = sensorImage;
    }

    public void setSensorImg(String sensorImg) {
        this.sensorImg = sensorImg;
    }

    public String getSensorImg() {
        return sensorImg;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public void setSensorID(String sensorID) {
        this.sensorID = sensorID;
    }

    public SensorModal() {
    }

    public String getSensorType() {
        return sensorType;
    }

    public String getSensorID() {
        return sensorID;
    }

    public static final Creator<SensorModal> CREATOR = new Creator<SensorModal>() {
        @Override
        public SensorModal createFromParcel(Parcel in) {
            return new SensorModal(in);
        }

        @Override
        public SensorModal[] newArray(int size) {
            return new SensorModal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(sensorID);
        parcel.writeString(sensorName);
        parcel.writeString(sensorType);
        parcel.writeString(sensorImg);
    }

}
