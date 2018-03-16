package com.example.android.group4.services;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.android.group4.db.DBHelper;
import com.example.android.group4.models.AccelerometerDatum;

import java.text.SimpleDateFormat;

public class SensorHandlerService extends Service  implements SensorEventListener {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    private long lastUpdate = 0;
    private SensorManager accelManage;
    private Sensor senseAccel;

    public static final String ACTION = "com.example.android.group4.services.SensorHandlerService";

    public SensorHandlerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
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
                AccelerometerDatum accelerometerDatum = new AccelerometerDatum();
                accelerometerDatum.setTimestamp(time_stamp);
                accelerometerDatum.setX(x);
                accelerometerDatum.setY(y);
                accelerometerDatum.setZ(z);
                DBHelper.insertAccelerometerData(accelerometerDatum);

                setEventMessage(accelerometerDatum);

            }

        }
    }

    public void setEventMessage(AccelerometerDatum accelerometerDatum){
        Intent intent = new Intent();
        intent.setAction(ACTION);
        Bundle b = new Bundle();
        b.putSerializable("data",accelerometerDatum );
        intent.putExtras(b);
        sendBroadcast(intent);
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

    public static boolean isServiceRunning(Activity activity) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("com.example.android.group4.services.SensorHandlerService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void stopService(Activity activity){
        activity.stopService(new Intent(activity, SensorHandlerService.class));
    }

    public static void startService(Activity activity){
        activity.startService(new Intent(activity, SensorHandlerService.class));
    }

}
