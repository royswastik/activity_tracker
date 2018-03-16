package com.example.android.group4;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.android.group4.helpers.PermissionHelper;
import com.example.android.group4.services.SensorHandlerService;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

//    //SQLite Databases
//    SQLiteDatabase db, db1;
//    String tableName = "";
//
//    //Patient Data
//    public int patientId, patientAge, sexValue;
//    public String patientName, patientSex;
//
//
//
//    //Buttons
//    Button runButton, stopButton, uploadButton, downloadButton;
//
//    //Graph values
//    ArrayList<Float> xValues, yValues, zValues;
//    public final Handler mHandler = new Handler();
//    public LineGraphSeries<DataPoint> series1;
//    public LineGraphSeries<DataPoint> series2;
//    public LineGraphSeries<DataPoint> series3;
//
//    //Graph variables
//    private static final String KEY_TimeStamp = "KEY_TimeStamp";
//    private static final String KEY_Xaxis = "KEY_Xaxis";
//    private static final String KEY_Yaxis = "KEY_Yaxis";
//    private static final String KEY_Zaxis = "KEY_Zaxis";
//
//    //Sensors
//    public SensorManager sensorManager;
//    public Sensor sensorAccelerometer;
//
//    //Other parameters
//    int secCounter=0;
//    int traverseEleCount=0;
//    boolean tableExistCheck=false;
//    boolean isRunning = false;
//    boolean tableExistCheck_onSensorChanged=false;
//    boolean clearEditTexts=false;
//    boolean flag_sense = false;
//    long lastUpdate = 0;
//    float last_x, last_y, last_z;
//    double graph2LastXValue = 0d;
//    private Runnable timer1;
//    boolean dataStoredInDB = false;
//
//    //File Paths
//    String downloadDir = "/CSE535_Assignment2/Other/";    //folder for download
//    String fileName = "Group4.db";  //databse name
//    String baseDbDir="/Android/Data/CSE535_ASSIGNMENT2";    //Path where db to be created
//    String phpPath = "";
//    String serverPath = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        PermissionHelper.checkPermissions(this);
//
//
//
//        //Initializing graoh values
//        xValues=new ArrayList<Float>();
//        yValues=new ArrayList<Float>();
//        zValues=new ArrayList<Float>();
//
//        //Finding buttons
//        runButton = (Button) findViewById(R.id.run_graph);
//        stopButton = (Button) findViewById(R.id.stop_graph);
//        uploadButton = (Button) findViewById(R.id.upload_data);
//        downloadButton = (Button) findViewById(R.id.download_data);
//
//
//        stopButton.setEnabled(false);
//
//        //Declaring accelerometer
//        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        sensorManager.registerListener((SensorEventListener) this, sensorAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
//
//        //Initializing parameters for the graph
//        final GraphView graph = (GraphView) findViewById(R.id.graph);
//        series1 = new LineGraphSeries<>();
//        series2 = new LineGraphSeries<>();
//        series3 = new LineGraphSeries<>();
//
//        series1.setColor(Color.RED);
//        series2.setColor(Color.GREEN);
//        series3.setColor(Color.BLUE);
//
//        graph.addSeries(series1);
//        graph.addSeries(series2);
//        graph.addSeries(series3);
//
//        graph.getViewport().setXAxisBoundsManual(true);
//        graph.getViewport().setMinX(0);
//        graph.getViewport().setMaxX(10);
//        graph.getGridLabelRenderer().setLabelVerticalWidth(60);
//
//        GridLabelRenderer gridLabel;
//        gridLabel = graph.getGridLabelRenderer();
//        gridLabel.setHorizontalAxisTitle("Time stamp");
//        gridLabel.setVerticalAxisTitle("Accelerometer values");
//
//
//
//        runButton.setOnClickListener(new View.OnClickListener() //Run button Click listener
//        {
//            public void onClick(View v)
//            {
//                if (traverseEleCount == 0)
//                {
//                    boolean allOk = tablenamecreator();
//                    if (allOk) {
//                        tableExistCheck_onSensorChanged = false;
//                        secCounter = 0;
//                        resetTheGraph();
//                        enableDisableEverything(false);
//                        Toast.makeText(MainActivity.this, "Please Wait..", Toast.LENGTH_SHORT).show();
//
//                        if (!isRunning)
//                        {
//                            isRunning = true;
//                            Log.d("RunningStatus", "onClick: running set to TRUE");
//                        }
//                    }
//                }
//                else
//                {
//                    if (!isRunning)
//                    {
//                        isRunning = true;
//
//                        runButton.setEnabled(false);
//                        stopButton.setEnabled(true);
//
//                        graph.addSeries(series1);
//                        graph.addSeries(series2);
//                        graph.addSeries(series3);
//                        onResume();
//                        Log.d("RunningStatus", "onClick: Running is set to TRUE");
//                    }
//                }
//            }
//
//        });
//
//        stopButton.setOnClickListener(new View.OnClickListener() //Stop button Click Listener
//        {
//            public void onClick(View v)
//            {
//                if (isRunning)
//                {
//                    isRunning = false;
//                    runButton.setEnabled(true);
//                    stopButton.setEnabled(false);
//                    graph.removeAllSeries();
//                    Log.d("RunningStatus", "onClick: Running is set to FALSE");
//                }
//            }
//
//        });
//
//        uploadButton.setOnClickListener(new View.OnClickListener()  //Upload button Click Listener
//        {
//            public void onClick(View v)
//            {
//                AsyncUploadFile upTask = new AsyncUploadFile();
//                upTask.execute();
//            }
//        });
//
//        downloadButton.setOnClickListener(new View.OnClickListener()   //Download button Click Listener
//        {
//            public void onClick(View v)
//            {
//                resetTheGraph();
//
//                nameString.setText("");
//                idValue.setText(0);
//                ageValue.setText(0);
//
//                RadioButton maleRadioButton, femaleRadioButton;
//                maleRadioButton = (RadioButton) findViewById(R.id.sex_male);
//                femaleRadioButton = (RadioButton) findViewById(R.id.sex_female);
//                maleRadioButton.setChecked(true);
//                femaleRadioButton.setChecked(false);
//
//                AsyncDownloadFile downTask = new AsyncDownloadFile();
//                downTask.execute();
//
//                enableDisableEverything(false);
//            }
//        });
//    }
//
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
//    {
//        if (requestCode == PermissionHelper.PERMISSION_REQUEST) {
//            for (int grantResult : grantResults) {
//                if (grantResult != PackageManager.PERMISSION_GRANTED) {
//                    finishAffinity();
//                }
//            }
//        }
//    }
//
//
//
//
//
//    public void resetTheGraph()
//    {
//        GraphView graph = (GraphView) findViewById(R.id.graph);
//        graph.removeAllSeries();
//
//        series1 = new LineGraphSeries<>();
//        series2 = new LineGraphSeries<>();
//        series3 = new LineGraphSeries<>();
//
//        series1.setColor(Color.RED);
//        series2.setColor(Color.GREEN);
//        series3.setColor(Color.BLUE);
//
//        graph.addSeries(series1);
//        graph.addSeries(series2);
//        graph.addSeries(series3);
//
//        graph.getViewport().setXAxisBoundsManual(true);
//        graph.getViewport().setMinX(0);
//        graph.getViewport().setMaxX(10);
//
//        graph2LastXValue=0d;
//    }
//
//
//
//    protected void onPause()
//    {
//        super.onPause();
//        sensorManager.unregisterListener((SensorEventListener) this);
//    }
//
//    @Override
//    public void onResume()
//    {
//        super.onResume();
//        sensorManager.registerListener((SensorEventListener) this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//        timer1 = new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                if(isRunning && dataStoredInDB && traverseEleCount<xValues.size())
//                {
//                    series1.appendData(new DataPoint(graph2LastXValue, xValues.get(traverseEleCount)), true, 15);
//                    series2.appendData(new DataPoint(graph2LastXValue, yValues.get(traverseEleCount)), true, 15);
//                    series3.appendData(new DataPoint(graph2LastXValue, zValues.get(traverseEleCount)), true, 15);
//
//                    graph2LastXValue += 1d;
//                    traverseEleCount++;
//                    mHandler.postDelayed(this, 1000);
//                }
//                else if(traverseEleCount != 0 && traverseEleCount>=xValues.size())
//                {
//                    dataStoredInDB=false;
//                    xValues.clear();
//                    yValues.clear();
//                    zValues.clear();
//                    traverseEleCount=0;
//                    isRunning=false;
//                    runButton.setEnabled(true);
//                    stopButton.setEnabled(false);
//                    downloadButton.setEnabled(true);
//                }
//            }
//
//        };
//        mHandler.postDelayed(timer1, 1000);
//    }
//
//    public void getDataFromdb() //Reads from db
//    {
//        final String TABLE_NAME = tableName;
//
//        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        if (cursor.moveToFirst())
//        {
//            do
//            {
//
//                xValues.add(Float.parseFloat(cursor.getString(1)));
//                yValues.add(Float.parseFloat(cursor.getString(2)));
//                zValues.add(Float.parseFloat(cursor.getString(3)));
//
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//    }
//
//
//
//
//
//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//
//    }



}