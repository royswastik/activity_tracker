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

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //SQLite Databases
    SQLiteDatabase db, db1;
    String tableName = "";

    //Patient Data
    public int patientId, patientAge, sexValue;
    public String patientName, patientSex;

    //EditText inputs
    EditText idValue, ageValue, nameString;
    RadioGroup sexGroup;

    //Buttons
    Button runButton, stopButton, uploadButton, downloadButton;

    //Graph values
    ArrayList<Float> xValues, yValues, zValues;
    public final Handler mHandler = new Handler();
    public LineGraphSeries<DataPoint> series1;
    public LineGraphSeries<DataPoint> series2;
    public LineGraphSeries<DataPoint> series3;

    //Graph variables
    private static final String KEY_TimeStamp = "KEY_TimeStamp";
    private static final String KEY_Xaxis = "KEY_Xaxis";
    private static final String KEY_Yaxis = "KEY_Yaxis";
    private static final String KEY_Zaxis = "KEY_Zaxis";

    //Sensors
    public SensorManager sensorManager;
    public Sensor sensorAccelerometer;

    //Other parameters
    int secCounter=0;
    int traverseEleCount=0;
    boolean tableExistCheck=false;
    boolean isRunning = false;
    boolean tableExistCheck_onSensorChanged=false;
    boolean clearEditTexts=false;
    boolean flag_sense = false;
    long lastUpdate = 0;
    float last_x, last_y, last_z;
    double graph2LastXValue = 0d;
    private Runnable timer1;
    boolean dataStoredInDB = false;

    //File Paths
    String downloadDir = "/CSE535_Assignment2/Other/";    //folder for download
    String fileName = "Group4.db";  //databse name
    String baseDbDir="/Android/Data/CSE535_ASSIGNMENT2";    //Path where db to be created
    String phpPath = "";
    String serverPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionHelper.checkPermissions(this);

        //Swastik - Start
        startAccelerometerService();


        //Swastik - End

        //Initializing graoh values
        xValues=new ArrayList<Float>();
        yValues=new ArrayList<Float>();
        zValues=new ArrayList<Float>();

        //Finding buttons
        runButton = (Button) findViewById(R.id.run_graph);
        stopButton = (Button) findViewById(R.id.stop_graph);
        uploadButton = (Button) findViewById(R.id.upload_data);
        downloadButton = (Button) findViewById(R.id.download_data);

        //Getting patient data
        idValue = (EditText) findViewById(R.id.patient_id);
        ageValue = (EditText) findViewById(R.id.patient_age);
        nameString = (EditText) findViewById(R.id.patient_name);
        sexGroup = (RadioGroup) findViewById(R.id.patient_sex);

        stopButton.setEnabled(false);

        //Declaring accelerometer
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener((SensorEventListener) this, sensorAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        //Initializing parameters for the graph
        final GraphView graph = (GraphView) findViewById(R.id.graph);
        series1 = new LineGraphSeries<>();
        series2 = new LineGraphSeries<>();
        series3 = new LineGraphSeries<>();

        series1.setColor(Color.RED);
        series2.setColor(Color.GREEN);
        series3.setColor(Color.BLUE);

        graph.addSeries(series1);
        graph.addSeries(series2);
        graph.addSeries(series3);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);
        graph.getGridLabelRenderer().setLabelVerticalWidth(60);

        GridLabelRenderer gridLabel;
        gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Time stamp");
        gridLabel.setVerticalAxisTitle("Accelerometer values");

        //Converting user inputs
        idValue.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int j, int k) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int j, int k)
            {
//                idValue.setInputType(InputType.TYPE_CLASS_TEXT);
                patientId = Integer.parseInt(idValue.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        ageValue.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int j, int k) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int j, int k)
            {
//                ageValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                patientAge = Integer.parseInt(ageValue.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        nameString.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int j, int k) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int j, int k)
            {
                nameString.setInputType(InputType.TYPE_CLASS_TEXT);
                patientName = nameString.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i)
            {
                sexValue = radioGroup.getCheckedRadioButtonId();
                RadioButton rd = (RadioButton) findViewById(sexValue);
                patientSex = rd.getText().toString();
            }
        });

        runButton.setOnClickListener(new View.OnClickListener() //Run button Click listener
        {
            public void onClick(View v)
            {
                if (traverseEleCount == 0)
                {
                    boolean allOk = tablenamecreator();
                    if (allOk) {
                        tableExistCheck_onSensorChanged = false;
                        secCounter = 0;
                        resetTheGraph();
                        enableDisableEverything(false);
                        Toast.makeText(MainActivity.this, "Please Wait..", Toast.LENGTH_SHORT).show();

                        if (!isRunning)
                        {
                            isRunning = true;
                            Log.d("RunningStatus", "onClick: running set to TRUE");
                        }
                    }
                }
                else
                {
                    if (!isRunning)
                    {
                        isRunning = true;

                        runButton.setEnabled(false);
                        stopButton.setEnabled(true);

                        graph.addSeries(series1);
                        graph.addSeries(series2);
                        graph.addSeries(series3);
                        onResume();
                        Log.d("RunningStatus", "onClick: Running is set to TRUE");
                    }
                }
            }

        });

        stopButton.setOnClickListener(new View.OnClickListener() //Stop button Click Listener
        {
            public void onClick(View v)
            {
                if (isRunning)
                {
                    isRunning = false;
                    runButton.setEnabled(true);
                    stopButton.setEnabled(false);
                    graph.removeAllSeries();
                    Log.d("RunningStatus", "onClick: Running is set to FALSE");
                }
            }

        });

        uploadButton.setOnClickListener(new View.OnClickListener()  //Upload button Click Listener
        {
            public void onClick(View v)
            {
                AsyncUploadFile upTask = new AsyncUploadFile();
                upTask.execute();
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener()   //Download button Click Listener
        {
            public void onClick(View v)
            {
                resetTheGraph();

                nameString.setText("");
                idValue.setText(0);
                ageValue.setText(0);

                RadioButton maleRadioButton, femaleRadioButton;
                maleRadioButton = (RadioButton) findViewById(R.id.sex_male);
                femaleRadioButton = (RadioButton) findViewById(R.id.sex_female);
                maleRadioButton.setChecked(true);
                femaleRadioButton.setChecked(false);

                AsyncDownloadFile downTask = new AsyncDownloadFile();
                downTask.execute();

                enableDisableEverything(false);
            }
        });
    }

    public void startAccelerometerService(){
        Intent i = new Intent(this, SensorHandlerService.class);
        startService(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if (requestCode == PermissionHelper.PERMISSION_REQUEST) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    finishAffinity();
                }
            }
        }
    }

    //Accelerometer
    public void onSensorChanged(SensorEvent sensorEvent) //Accelerometer sensor for change in values
    {
        Sensor mySensor = sensorEvent.sensor;

        if(clearEditTexts)
        {
            clearEditTexts=false;
            enableDisableEverything(true);
            stopButton.setEnabled(false);

            RadioButton maleRadioButton, femaleRadioButton;
            maleRadioButton = (RadioButton) findViewById(R.id.sex_male);
            femaleRadioButton = (RadioButton) findViewById(R.id.sex_female);

            nameString.setText("");
            idValue.setText(0);
            ageValue.setText(0);
            maleRadioButton.setChecked(true);
            femaleRadioButton.setChecked(false);
        }

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER && flag_sense && isRunning && !tableExistCheck)
        {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 1000 && secCounter<11)
            {
                lastUpdate = curTime;

                last_x = x;
                last_y = y;
                last_z = z;

                try
                {
                    ContentValues values = new ContentValues();
                    values.put("KEY_TimeStamp", System.currentTimeMillis());
                    values.put("KEY_Xaxis", last_x);
                    values.put("KEY_Yaxis", last_y);
                    values.put("KEY_Zaxis", last_z);
                    db.beginTransaction();
                    db.insert(patientName + "_" + patientId + "_" + patientAge + "_" + patientSex, null, values);
                    db.setTransactionSuccessful();
                } catch (SQLiteException e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }

                secCounter++;
            }
            else if(secCounter>=11)
            {
                Toast.makeText(MainActivity.this, "Table Created!", Toast.LENGTH_SHORT).show();
                enableDisableEverything(true);
                runButton.setEnabled(false);
                stopButton.setEnabled(true);
                downloadButton.setEnabled(false);
                flag_sense = false;
                getDataFromdb();
                dataStoredInDB=true;
                onResume();
            }
        }
        else if(tableExistCheck && !tableExistCheck_onSensorChanged)
        {
            tableExistCheck_onSensorChanged=true;

            Toast.makeText(MainActivity.this, "Table Exists!!", Toast.LENGTH_SHORT).show();
            enableDisableEverything(true);
            runButton.setEnabled(false);
            stopButton.setEnabled(true);
            downloadButton.setEnabled(false);
            flag_sense = false;
            getDataFromdb();
            dataStoredInDB=true;
            onResume();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void resetTheGraph()
    {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.removeAllSeries();

        series1 = new LineGraphSeries<>();
        series2 = new LineGraphSeries<>();
        series3 = new LineGraphSeries<>();

        series1.setColor(Color.RED);
        series2.setColor(Color.GREEN);
        series3.setColor(Color.BLUE);

        graph.addSeries(series1);
        graph.addSeries(series2);
        graph.addSeries(series3);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);

        graph2LastXValue=0d;
    }

    public void enableDisableEverything(boolean flag) //Enabler/Disabler
    {
        idValue.setEnabled(flag);
        nameString.setEnabled(flag);
        ageValue.setEnabled(flag);

        runButton.setEnabled(flag);
        stopButton.setEnabled(flag);
        uploadButton.setEnabled(flag);
        downloadButton.setEnabled(flag);

        RadioButton maleRadioButton, femaleRadioButton;
        maleRadioButton = (RadioButton) findViewById(R.id.sex_male);
        femaleRadioButton = (RadioButton) findViewById(R.id.sex_female);
        maleRadioButton.setEnabled(flag);
        femaleRadioButton.setEnabled(flag);
    }

    protected void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener((SensorEventListener) this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        sensorManager.registerListener((SensorEventListener) this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        timer1 = new Runnable()
        {
            @Override
            public void run()
            {
                if(isRunning && dataStoredInDB && traverseEleCount<xValues.size())
                {
                    series1.appendData(new DataPoint(graph2LastXValue, xValues.get(traverseEleCount)), true, 15);
                    series2.appendData(new DataPoint(graph2LastXValue, yValues.get(traverseEleCount)), true, 15);
                    series3.appendData(new DataPoint(graph2LastXValue, zValues.get(traverseEleCount)), true, 15);

                    graph2LastXValue += 1d;
                    traverseEleCount++;
                    mHandler.postDelayed(this, 1000);
                }
                else if(traverseEleCount != 0 && traverseEleCount>=xValues.size())
                {
                    dataStoredInDB=false;
                    xValues.clear();
                    yValues.clear();
                    zValues.clear();
                    traverseEleCount=0;
                    isRunning=false;
                    runButton.setEnabled(true);
                    stopButton.setEnabled(false);
                    downloadButton.setEnabled(true);
                }
            }

        };
        mHandler.postDelayed(timer1, 1000);
    }

    public void getDataFromdb() //Reads from db
    {
        final String TABLE_NAME = tableName;

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {

                xValues.add(Float.parseFloat(cursor.getString(1)));
                yValues.add(Float.parseFloat(cursor.getString(2)));
                zValues.add(Float.parseFloat(cursor.getString(3)));

            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public boolean isTableExists(String tableName) //Checks for table
    {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null)
        {
            if(cursor.getCount()>0)
            {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public boolean tablenamecreator()
    {
        if(patientName!=null && !patientName.equalsIgnoreCase(""))
        {
            if(patientId != '\0')
            {
                if(patientAge != '\0')
                {
                    if(patientSex!=null)
                    {
                        try
                        {
                            File folder = new File(Environment.getExternalStorageDirectory() + baseDbDir);
                            boolean success = true;
                            if (!folder.exists())
                            {
                                success = folder.mkdirs();
                            }
                            if (success)
                            {

                            }
                            else
                            {

                            }
                            db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + baseDbDir + "/" + fileName, null);
                            db.beginTransaction();
                            try
                            {
                                tableName = patientName + "_" + patientId + "_" + patientAge + "_" + patientSex;
                                tableExistCheck = isTableExists(tableName);
                                if(!tableExistCheck)
                                {
                                    db.execSQL("CREATE TABLE " + patientName + "_" + patientId + "_" + patientAge + "_" + patientSex +
                                            "(" + KEY_TimeStamp + " BIGINT PRIMARY KEY, " + KEY_Xaxis + " INTEGER, " + KEY_Yaxis + " INTEGER, " + KEY_Zaxis + " INTEGER" + ")");
                                    db.setTransactionSuccessful();
                                }
                                flag_sense = true;
                            }
                            catch (SQLiteException e)
                            {
                                Toast.makeText(MainActivity.this, "SQLiteException" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                            finally
                            {
                                db.endTransaction();
                            }
                        }
                        catch (SQLException e)
                        {

                        }
                        return true;
                    }
                    else
                    {
                        sexGroup.requestFocus();
                        Toast.makeText(MainActivity.this, "Please select your sex", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Please enter your age", Toast.LENGTH_SHORT).show();
                    ageValue.requestFocus();
                    return false;
                }
            }
            else
            {
                Toast.makeText(MainActivity.this, "Please enter patient ID", Toast.LENGTH_SHORT).show();
                idValue.requestFocus();
                return false;
            }
        }
        else
        {
            Toast.makeText(MainActivity.this, "Please enter patient name", Toast.LENGTH_SHORT).show();
            nameString.requestFocus();
            return false;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //Upload
    private class AsyncUploadFile extends AsyncTask<Void, String, Void>
    {
        int flag = 0;
        ProgressDialog waitDialog;
        //strings to create multipart format message
        String nl = "\r\n";
        String hyphen = "--";
        String boundary =  "*****";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            waitDialog = ProgressDialog.show(MainActivity.this,"","Wait till upload completes",true);
        }

        protected Void doInBackground(Void... params) {
            try {


                File fileToUpload = new File(String.valueOf(android.os.Environment.getExternalStorageDirectory()) + baseDbDir + "/", fileName); //get database file from storage
                URL upPath = new URL(phpPath);
                HttpURLConnection urlConnect = (HttpURLConnection) upPath.openConnection(); //create http object
                urlConnect.setDoOutput(true);   //flag to set server to be written to using output stream
                urlConnect.setDoInput(true);
                urlConnect.setUseCaches(false);
                urlConnect.setRequestMethod("POST");    //send data using POST
                urlConnect.setRequestProperty("Connection", "Keep-Alive");  //keep connection without disconnecting
                urlConnect.setRequestProperty("ENCTYPE", "multipart/form-data");    //send multipart format data to server
                urlConnect.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                urlConnect.setRequestProperty("uploaded_file", String.valueOf(fileToUpload));   //To be sent to php code for setting file path
                DataOutputStream upStream = new DataOutputStream(urlConnect.getOutputStream());

                //open output stream in server
                upStream.writeBytes(hyphen + boundary + nl);    //start the message with boundary
                upStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + String.valueOf(fileToUpload) + "\""+nl);   //write the type of data with filename and path
                upStream.writeBytes(nl); //new line which is followed by the actual file contents

                FileInputStream fStream = new FileInputStream(fileToUpload);
                DataInputStream readStream = new DataInputStream(fStream);

                int length;
                byte[] buffer = new byte[4096];
                while ((length = readStream.read(buffer)) != -1) {
                    upStream.write(buffer, 0, length);
                }

                //after file is written, finish the message with boundary
                upStream.writeBytes(nl);
                upStream.writeBytes(hyphen + boundary + hyphen + nl);

                //System.out.println(urlConnect.getResponseCode());
                //To check success response from server
                if(urlConnect.getResponseCode() == 200)
                {
                    flag = 1;
                }

                //close streams
                readStream.close();
                fStream.close();
                upStream.close();

            }
            catch (MalformedURLException ex)
            {
                flag = 2;
                publishProgress(ex.getMessage());
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            } catch (FileNotFoundException nf)
            {
                flag = 2;
                publishProgress(nf.getMessage());
            } catch (IOException ioe)
            {
                Log.e("Debug", "error: " + ioe.getMessage(), ioe);
                flag = 2;
                publishProgress(ioe.getMessage());
            }


            return null;
        }

        protected void onProgressUpdate(String... value) {
            super.onProgressUpdate(value);
            //display message if there is any exception
            if(flag == 2)
                Toast.makeText(MainActivity.this, "ERROR: "+ value[0], Toast.LENGTH_LONG).show();
        }


        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            waitDialog.dismiss();
            //display if upload is success
            if(flag == 1)
                Toast.makeText(MainActivity.this, "Upload completed", Toast.LENGTH_SHORT).show();

        }
    }

    //Download
    private class AsyncDownloadFile extends AsyncTask<Void, String, Void>  //returns exception strings to onProgressUpdate
    {
        boolean flag = false;   //flag to check for any exception
        ProgressDialog waitDialog;  //progress dialog to disable activity during download
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            waitDialog = ProgressDialog.show(MainActivity.this,"","Wait until download finishes",true);
        }

        protected Void doInBackground(Void... params) {
            try {
                File downloadedFile1 = new File(android.os.Environment.getExternalStorageDirectory(), downloadDir);   //create SD path based on device with /CSE535_ASSIGNMENT2_Extra/ folder
                if(!downloadedFile1.isDirectory())
                    downloadedFile1.mkdir();    //create directory if none exists
                File downloadedFile = new File(downloadedFile1, fileName);  //create complete path for file
                URL urlPath = new URL(serverPath + fileName);  //create URL object of server path
                HttpURLConnection urlConnect = (HttpURLConnection) urlPath.openConnection();    //create object to establish http connection
                int contentLength = urlConnect.getContentLength();  //get length of the file to be downloaded
                DataInputStream iStream = new DataInputStream(urlPath.openStream());    //new input stream to save buffer of downloaded file
                byte[] buffer = new byte[contentLength];    //buffer size is size of file
                //System.out.println(contentLength);

                int length;
                FileOutputStream fStream = new FileOutputStream(downloadedFile);    //output stream to write from buffer and save as file
                DataOutputStream oStream = new DataOutputStream(fStream);

                //transfer from input stream to output stream using buffer
                while ((length = iStream.read(buffer)) != -1) {
                    oStream.write(buffer, 0, length);
                }

                //close streams
                iStream.close();
                oStream.flush();
                oStream.close();
            }

            catch (FileNotFoundException e)
            {
                flag = true;    //flag true if there is exception
                System.out.println(e.getMessage());
                publishProgress(e.getMessage());
            }
            catch (MalformedURLException e)
            {
                flag = true;
                publishProgress(e.getMessage());
                e.printStackTrace();
            }
            catch (IOException e)
            {
                flag = true;
                publishProgress(e.getMessage());
            }
            return null;
        }

        protected void onProgressUpdate(String... value) {
            super.onProgressUpdate(value);
            //show message if there exception flag is set
            if(flag)
                Toast.makeText(MainActivity.this, "ERROR:  "+value[0], Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            waitDialog.dismiss();   //remove dialog after download completes

            //show message if download is successful
            if(!flag)
                Toast.makeText(MainActivity.this, "Download completed", Toast.LENGTH_SHORT).show();

            db1 = SQLiteDatabase.openOrCreateDatabase(android.os.Environment.getExternalStorageDirectory() + downloadDir + fileName, null);
            db1.beginTransaction();

            Cursor c = db1.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            c.moveToLast();
            tableName = c.getString(0);

            String [] tabNameSplit = tableName.split("_");

            nameString.setText(tabNameSplit[0]);
            idValue.setText(tabNameSplit[1]);
            ageValue.setText(tabNameSplit[2]);

            RadioButton maleRadioButton, femaleRadioButton;
            maleRadioButton = (RadioButton) findViewById(R.id.sex_male);
            femaleRadioButton = (RadioButton) findViewById(R.id.sex_female);
            if(tabNameSplit[3].equalsIgnoreCase("male"))
            {
                maleRadioButton.setChecked(true);
                femaleRadioButton.setChecked(false);
                patientSex = maleRadioButton.getText().toString();
            }
            else
            {
                maleRadioButton.setChecked(false);
                femaleRadioButton.setChecked(true);
                patientSex = femaleRadioButton.getText().toString();
            }

            patientName=nameString.getText().toString(); //Receives patient info
            patientId=Integer.parseInt(idValue.getText().toString());
            patientAge=Integer.parseInt(ageValue.getText().toString());





            final String TABLE_NAME = tableName;
            String selectQuery = "SELECT  * FROM " + TABLE_NAME;  //selects entries from table
            Cursor cursor = db1.rawQuery(selectQuery, null);
            db1.endTransaction();

            if (cursor.moveToFirst())
            {
                do
                {

                    xValues.add(Float.parseFloat(cursor.getString(1)));
                    yValues.add(Float.parseFloat(cursor.getString(2)));
                    zValues.add(Float.parseFloat(cursor.getString(3)));

                } while (cursor.moveToNext());
            }
            cursor.close();

            final Timer timer = new Timer();
            timer.scheduleAtFixedRate( new TimerTask() {
                public void run() {                             //plots graph from db

                    if(traverseEleCount<xValues.size())
                    {

                        series1.appendData(new DataPoint(graph2LastXValue, xValues.get(traverseEleCount)), true, 15);
                        series2.appendData(new DataPoint(graph2LastXValue, yValues.get(traverseEleCount)), true, 15);
                        series3.appendData(new DataPoint(graph2LastXValue, zValues.get(traverseEleCount)), true, 15);
                        graph2LastXValue += 1d;

                        traverseEleCount++;
                    }
                    else if(traverseEleCount != 0 && traverseEleCount>=xValues.size())
                    {
                        xValues.clear();
                        yValues.clear();
                        zValues.clear();
                        traverseEleCount=0;

                        clearEditTexts=true;

                        timer.cancel();
                        timer.purge();
                    }
                }
            }, 0, 1000);

        }
    }
}