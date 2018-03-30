package group4.swastikroy.com.heart_rate_monitor_demo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import group4.swastikroy.com.heart_rate_monitor_demo.R;
import group4.swastikroy.com.heart_rate_monitor_demo.db.DBHelper;
import group4.swastikroy.com.heart_rate_monitor_demo.model.AccelerometerDataPoint;

public class DataCollectionActivity extends AppCompatActivity implements SensorEventListener {



    private int count = 0;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private String actionType;
    ViewHolder holder;

    private List<AccelerometerDataPoint> dataList = new ArrayList<AccelerometerDataPoint>();

    public static final int SENSOR_SAMPLING_RATE = 100000;
    public static final String TABLE_NAME = "accelerometer_data_table";
    DBHelper database = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collection);

        holder = new ViewHolder(this);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SENSOR_SAMPLING_RATE);

        Intent intent = getIntent();
        actionType = intent.getStringExtra("action");
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d("count : " + count, "T");
        long curTime = System.currentTimeMillis();
        if (count < 1000) {
            Sensor mySensor = sensorEvent.sensor;
            if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                if ((curTime - lastUpdate) > 100) {
                    AccelerometerDataPoint point = new AccelerometerDataPoint(x, y, z);
                    dataList.add(point);
                    count++;
                    lastUpdate = curTime;
                }
            }
        } else {
            holder.progressBar.setVisibility(View.GONE);
            holder.textMsg.setText(R.string.data_collection_complete);
            senSensorManager.unregisterListener(this);
            writeToDB(dataList, actionType);
        }
    }

    public void writeToDB(List<AccelerometerDataPoint> arr, String actionType) {
        Log.d("Write in DB","DB");
        database.insertAccelerometerDataList(arr,actionType);
        arr.clear();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SENSOR_SAMPLING_RATE);
    }

    static class ViewHolder{
        TextView textMsg;
        ProgressBar progressBar;
        ViewHolder(Activity activity){
            textMsg = (TextView) activity.findViewById(R.id.textMsg);
            progressBar = (ProgressBar) activity.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}
