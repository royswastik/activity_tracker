package com.example.android.group4.models;

/**
 * Created by Swastik on 1/22/2018.
 * The model is used to store an instance of Heart Rate Data
 */
public class HealthDatum {
    private double time;    //In Seconds
    private double value;   //Heart rate
    Patient patient;

    public HealthDatum(double time, double value) {
        this.time = time;
        this.value = value;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }


}
