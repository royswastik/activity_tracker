package group4.swastikroy.com.heart_rate_monitor_demo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import group4.swastikroy.com.heart_rate_monitor_demo.R;
import group4.swastikroy.com.heart_rate_monitor_demo.db.DBHelper;
import group4.swastikroy.com.heart_rate_monitor_demo.model.AccelerometerDataPoint;
import group4.swastikroy.com.heart_rate_monitor_demo.util.ActionBarUtil;

public class DataCollectionActivity extends AppCompatActivity implements SensorEventListener {



    private int count = 0;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private String actionType;
    ViewHolder holder;

    volatile AccelerometerDataPoint current_sensor_value;


    int countdown_timer = 0;

    int collection_timer = 0;

    int sampling_frequency = 10;

    private List<AccelerometerDataPoint> dataList = new ArrayList<AccelerometerDataPoint>();

    public static final int SENSOR_SAMPLING_RATE = 100000;
    public static final String TABLE_NAME = "accelerometer_data_table";
    DBHelper database = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collection);
        Intent intent = getIntent();
        actionType = intent.getStringExtra("action");
        holder = new ViewHolder(this);
        setup();
//        ActionBarUtil.setBackButton(this);
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SENSOR_SAMPLING_RATE);




    }

    private void setup(){
        current_sensor_value = new AccelerometerDataPoint();
        holder.collect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCollect();
            }
        });

        holder.data_count.setText(String.valueOf(DBHelper.getCount(actionType)));
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.v("Sensor", "Changed");
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            current_sensor_value.setX(sensorEvent.values[0]);
            current_sensor_value.setY(sensorEvent.values[1]);
            current_sensor_value.setZ(sensorEvent.values[2]);
            Log.v("Sensor", current_sensor_value.toString());
        }

        Log.d("count : " + collection_timer, "T");
//        long curTime = System.currentTimeMillis();
//        if (count < 20) {
//            Sensor mySensor = sensorEvent.sensor;
//            if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//                float x = sensorEvent.values[0];
//                float y = sensorEvent.values[1];
//                float z = sensorEvent.values[2];
//
//                if ((curTime - lastUpdate) > 100) {
//                    AccelerometerDataPoint point = new AccelerometerDataPoint(x, y, z);
//                    dataList.add(point);
//                    count++;
//                    lastUpdate = curTime;
//                }
//            }
//        } else {
////            holder.progressBar.setVisibility(View.GONE);
////            holder.textMsg.setText(R.string.data_collection_complete);
//            senSensorManager.unregisterListener(this);
//            writeToDB(dataList, actionType);
//        }
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

    private void onClickCollect(){
        holder.collection_container.setVisibility(View.GONE);
        holder.countdown_container.setVisibility(View.VISIBLE);
        this.countdown_timer = 3;
        holder.countdown.setText(String.valueOf(countdown_timer));
        start_countdown();
    }

    private void start_countdown(){
        holder.collect_btn.setEnabled(false);
        handler.postDelayed(runnableCodeCountdown,1000);
    }

    /**
     * Called when countdown is over
     */
    private void countdown_done(){
        holder.collection_msg.setText(getString(R.string.data_collection_message));
        collect_data();
    }

    private void collect_data(){
        holder.collection_container.setVisibility(View.VISIBLE);
        holder.countdown_container.setVisibility(View.GONE);
        collection_timer = 5*sampling_frequency;
        dataList = new ArrayList<>();
        handler.post(runnableCodeCollection);
            //Update progress bar after each second
            //Store data in DB
    }

    /**
     * Called when collection is done
     */
    private void on_collection_done(){
        Log.v("Data", "Collection done");
        holder.collect_btn.setEnabled(true);
        holder.collection_msg.setText("Collection Done");

        writeToDB(dataList, actionType);
        holder.data_count.setText(String.valueOf(DBHelper.getCount(actionType)));

    }

    Handler handler = new Handler();
    private Runnable runnableCodeCountdown = new Runnable() {
        @Override
        public void run() {
            countdown_timer--;
            if(countdown_timer > 0){
                holder.countdown.setText(String.valueOf(countdown_timer));
                handler.postDelayed(this, 1000);
            }
            else{
                countdown_done();
            }

        }
    };

    private Runnable runnableCodeCollection = new Runnable() {
        @Override
        public void run() {
            collection_timer--;
            if(collection_timer >= 0){
                int progress = ((int)(5-(collection_timer/sampling_frequency)));
                holder.progressBar.setProgress(progress);
                AccelerometerDataPoint newData = new AccelerometerDataPoint();
                newData.setX(current_sensor_value.getX());
                newData.setY(current_sensor_value.getY());
                newData.setZ(current_sensor_value.getZ());
                dataList.add(newData);
                handler.postDelayed(this,(1000/sampling_frequency));
            }
            else{
                on_collection_done();
            }

        }
    };



    static class ViewHolder{
        TextView countdown, collection_msg, data_count;
        ProgressBar progressBar;
        View countdown_container, collection_container;
        Button collect_btn;


        ViewHolder(Activity activity){
            collection_msg = (TextView) activity.findViewById(R.id.collection_msg);
            data_count  = (TextView) activity.findViewById(R.id.data_count);
            countdown = (TextView) activity.findViewById(R.id.countdown);
            progressBar = (ProgressBar) activity.findViewById(R.id.progressBar);
//            progressBar.setVisibility(View.VISIBLE);

            countdown_container = activity.findViewById(R.id.countdown_container);
            collection_container = activity.findViewById(R.id.collection_container);

            collect_btn = (Button) activity.findViewById(R.id.collect_btn);
        }
    }
}
