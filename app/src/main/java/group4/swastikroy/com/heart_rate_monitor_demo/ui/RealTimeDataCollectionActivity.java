package group4.swastikroy.com.heart_rate_monitor_demo.ui;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import group4.swastikroy.com.heart_rate_monitor_demo.R;
import group4.swastikroy.com.heart_rate_monitor_demo.model.AccelerometerDataInstance;
import group4.swastikroy.com.heart_rate_monitor_demo.model.AccelerometerDataPoint;
import group4.swastikroy.com.heart_rate_monitor_demo.model.ActionLabel;
import group4.swastikroy.com.heart_rate_monitor_demo.util.ActionBarUtil;
import group4.swastikroy.com.heart_rate_monitor_demo.util.Constants;
import group4.swastikroy.com.heart_rate_monitor_demo.util.NotificationUtil;
import group4.swastikroy.com.heart_rate_monitor_demo.util.SVMUtil;
import libsvm.svm;
import libsvm.svm_model;
import java.util.Queue;

public class RealTimeDataCollectionActivity extends AppCompatActivity implements SensorEventListener {

    int count = 0;
    ClassificationActivity classify = new ClassificationActivity();
    String fp = classify.modelFilePath;
    SensorManager senSensorManager;
    Sensor senAccelerometer;
    long lastUpdate = 0;
    int countdown_timer = 0;
    int collection_timer = 0;
    int sampling_frequency = 10;
    volatile AccelerometerDataPoint current_sensor_value;
    String svmModelName = "SVMModelSave.txt";  //SVM Model name
    String baseModelDir = "/Android/Data/CSE535_ASSIGNMENT3";
    //Path where db to be created
    String MFilePath = baseModelDir + "/" + svmModelName;
    String modelFilePath = Environment.getExternalStorageDirectory() + MFilePath;
    svm_model svmModel = new svm_model();

    Queue<AccelerometerDataPoint> dataQueue = new LinkedList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_real_time_data);
        ActionBarUtil.setBackButton(this);
        try {
            SVMUtil.load_model();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Button startTracking = (Button) findViewById(R.id.startCollection);

        startTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //NotificationUtil.makeToast(RealTimeDataCollectionActivity.this, "Model default Parameters Loaded");
                Toast.makeText(RealTimeDataCollectionActivity.this, "Loading Model", Toast.LENGTH_SHORT).show();
                //TODO : load model here


                //NotificationUtil.makeToast(RealTimeDataCollectionActivity.this, "Collecting Data Now");
                Toast.makeText(RealTimeDataCollectionActivity.this, "Collecting Data Now...", Toast.LENGTH_SHORT).show();
//                Toast.makeText(RealTimeDataCollectionActivity.this, "Collecting Data Now", Toast.LENGTH_SHORT).show();
                startDataCollection();

                // creature the feature vector


            }
        });
    }

    public void startDataCollection() {
        //List<Float> dataList = new ArrayList<>();

        int SENSOR_SAMPLING_RATE = 100000;

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener((SensorEventListener) this, senAccelerometer, SENSOR_SAMPLING_RATE);

        current_sensor_value = new AccelerometerDataPoint();



        AccelerometerDataPoint newData = new AccelerometerDataPoint();
        newData.setX(current_sensor_value.getX());
        newData.setY(current_sensor_value.getY());
        newData.setZ(current_sensor_value.getZ());
        dataQueue.add(newData);
        if(dataQueue.size() == 51){
            dataQueue.remove();
        }

        Log.d("New Data = ",newData.toString());

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
    }

    Handler handler = new Handler();
    private Runnable runnableCodeCountdown = new Runnable() {
        @Override
        public void run() {
            classify();
            handler.postDelayed(this, 2000);
        }
    };

    public void classify(){
        AccelerometerDataInstance dataInstance = new AccelerometerDataInstance(dataQueue);
        dataInstance.setActionType(Constants.ACTIONS.UNKNOWN);
        ActionLabel actionLabel = SVMUtil.classifyInstance(dataInstance);
        //Use this class to show predicted label in UI and proper GIF
    }
}

/*GIF

ImageView imageView = (ImageView) findViewById(R.id.imageView);
GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
Glide.with(this).load("https://media.giphy.com/media/ICoxhc8wGbJ8k/giphy.gif").into(imageViewTarget);
*/