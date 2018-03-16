package com.example.android.group4.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class SensorHandlerService extends Service  implements SensorEventListener {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    private long lastUpdate = 0;
    private SensorManager accelManage;
    private Sensor senseAccel;

    public SensorHandlerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long time_stamp = 0;
            time_stamp = System.currentTimeMillis();
            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 1000) {
                lastUpdate = curTime;
                Log.v("service", "data: "+sdf.format(time_stamp) + "  x:" + x + "  y:"+y + "  z:" + z);
            }

        /* if(index >= 127){
                index = 0;
                accelManage.unregisterListener(this);
                accelManage.registerListener(this, senseAccel, SensorManager.SENSOR_DELAY_NORMAL);
            }*/
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onCreate(){
        Log.v("Service", "Service Started");
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        accelManage = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senseAccel = accelManage.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelManage.registerListener(this, senseAccel, SensorManager.SENSOR_DELAY_NORMAL);

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}