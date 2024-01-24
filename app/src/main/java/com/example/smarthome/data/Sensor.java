package com.example.smarthome.data;

public class Sensor {

    String sensorName, sensorType, sensorID, sensorImage;

    public Sensor(String sensorName, String sensorType, String sensorImage) {
        this.sensorName = sensorName;
        this.sensorType = sensorType;
        this.sensorImage = sensorImage;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getSensorID() {
        return sensorID;
    }

    public void setSensorID(String sensorID) {
        this.sensorID = sensorID;
    }

    public String getSensorImage() {
        return sensorImage;
    }

    public void setSensorImage(String sensorImage) {
        this.sensorImage = sensorImage;
    }
}
