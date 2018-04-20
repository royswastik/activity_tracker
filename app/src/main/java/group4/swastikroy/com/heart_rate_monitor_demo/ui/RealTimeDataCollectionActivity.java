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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import group4.swastikroy.com.heart_rate_monitor_demo.R;
import group4.swastikroy.com.heart_rate_monitor_demo.model.AccelerometerDataInstance;
import group4.swastikroy.com.heart_rate_monitor_demo.model.AccelerometerDataPoint;
import group4.swastikroy.com.heart_rate_monitor_demo.model.ActionLabel;
import group4.swastikroy.com.heart_rate_monitor_demo.util.ActionBarUtil;
import group4.swastikroy.com.heart_rate_monitor_demo.util.Constants;
import group4.swastikroy.com.heart_rate_monitor_demo.util.SVMUtil;
import libsvm.svm_model;

public class RealTimeDataCollectionActivity extends AbstractInnerActivity implements SensorEventListener {

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

    TextView xView, yView, zView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_real_time_data);
        ActionBarUtil.setBackButton(this);



        xView = (TextView) findViewById(R.id.x_value);
        yView = (TextView) findViewById(R.id.y_value);
        zView = (TextView) findViewById(R.id.z_value);




        try {
            SVMUtil.load_model();
            Toast.makeText(RealTimeDataCollectionActivity.this, "Loading Model", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Button startTracking = (Button) findViewById(R.id.startCollection);


        startTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //NotificationUtil.makeToast(RealTimeDataCollectionActivity.this, "Model default Parameters Loaded");
//                Toast.makeText(RealTimeDataCollectionActivity.this, "Loading Model", Toast.LENGTH_SHORT).show();
                //load model here


                //NotificationUtil.makeToast(RealTimeDataCollectionActivity.this, "Collecting Data Now");
                Toast.makeText(RealTimeDataCollectionActivity.this, "Collecting Data Now...", Toast.LENGTH_SHORT).show();
//                Toast.makeText(RealTimeDataCollectionActivity.this, "Collecting Data Now", Toast.LENGTH_SHORT).show();
                startDataCollection();

                // creature the feature vector


            }
        });
    }

//        newData.setY(current_sensor_value.getY());
//        newData.setZ(current_sensor_value.getZ());
    public void startDataCollection() {
        //List<Float> dataList = new ArrayList<>();

        int SENSOR_SAMPLING_RATE = 100000;


        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener((SensorEventListener) this, senAccelerometer, SENSOR_SAMPLING_RATE);

        current_sensor_value = new AccelerometerDataPoint();


        handler.postDelayed(runnableClassifierTimer,2500);
        handler.post(runnableCodeCollection);


    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(senSensorManager != null)
             senSensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.v("Sensor", "Changed");
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            current_sensor_value.setX(sensorEvent.values[0]);
            current_sensor_value.setY(sensorEvent.values[1]);
            current_sensor_value.setZ(sensorEvent.values[2]);
            //Log.v("Sensor", current_sensor_value.toString());
        }
        //Log.d("count : " + collection_timer, "T");
    }

    Handler handler = new Handler();
    private Runnable runnableClassifierTimer = new Runnable() {
        @Override
        public void run() {
            classify();
            handler.postDelayed(this, 2000);

        }
    };

    public void classify(){
        if(dataQueue.size() < 50){
            return;
        }
        AccelerometerDataInstance dataInstance = new AccelerometerDataInstance(dataQueue);
        dataInstance.setActionType(Constants.ACTIONS.UNKNOWN);
        //Use this class to show predicted label in UI and proper GIF
        ActionLabel actionLabel = SVMUtil.classifyInstance(dataInstance);

        TextView activityLabel = (TextView) findViewById(R.id.activity_type);
        activityLabel.setText(actionLabel.getLabel());
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
        imageView.setVisibility(View.VISIBLE);
        if(this.isFinishing())
            return;
        if(actionLabel.getLabel().equals(Constants.ACTIONS.RUN)) {
            Glide.with(getApplicationContext()).load("https://media.giphy.com/media/ICoxhc8wGbJ8k/giphy.gif").into(imageViewTarget);
        }
        else if(actionLabel.getLabel().equals(Constants.ACTIONS.WALK)) {
            Glide.with(getApplicationContext()).load("https://media.giphy.com/media/kA158Gup0eSpG/giphy.gif").into(imageViewTarget);
        }
        else if(actionLabel.getLabel().equals(Constants.ACTIONS.JUMP)) {
            Glide.with(getApplicationContext()).load("https://media.giphy.com/media/3oEduVY5mqa2GDxgcM/giphy.gif").into(imageViewTarget);
        }
        else if(actionLabel.getLabel().equals(Constants.ACTIONS.IDLE)) {
            Glide.with(getApplicationContext()).load("https://media.giphy.com/media/xT0xeDjlMBXbXcEIko/giphy.gif").into(imageViewTarget);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }


    }

    private Runnable runnableCodeCollection = new Runnable() {
        @Override
        public void run() {

                AccelerometerDataPoint newData = new AccelerometerDataPoint();
                newData.setX(current_sensor_value.getX());
                newData.setY(current_sensor_value.getY());
                newData.setZ(current_sensor_value.getZ());

                dataQueue.add(newData);
                if(dataQueue.size() == 51){
                    dataQueue.remove();
                }

                xView.setText(String.valueOf(dataQueue.peek().getX()));
                yView.setText(String.valueOf(dataQueue.peek().getY()));
                zView.setText(String.valueOf(dataQueue.peek().getZ()));


                handler.postDelayed(this,(1000/sampling_frequency));


        }
    };
}

/*GIF

ImageView imageView = (ImageView) findViewById(R.id.imageView);
GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
Glide.with(this).load("https://media.giphy.com/media/ICoxhc8wGbJ8k/giphy.gif").into(imageViewTarget);
*/