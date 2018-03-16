package com.example.android.group4.models;

//setters and getters to set and get the x,y,z and timestamp values from the accelerometer
public class AccelerometerDatum {

    private long timestamp;
    private float x;
    private float y;
    private float z;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

}


