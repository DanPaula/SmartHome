package com.example.smarthome.data.sensor_specific;

import com.example.smarthome.data.SensorModal;

public class TemperatureHumiditySensor extends SensorModal {

    double temperature, humidity;

    public TemperatureHumiditySensor(SensorModal sensor, double temperature, double humidity) {
        super(sensor.getSensorID(), sensor.getSensorName(), sensor.getSensorType(), sensor.getSensorImg());
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public TemperatureHumiditySensor(double temperature, double humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public TemperatureHumiditySensor(){

    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
}
