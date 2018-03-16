package com.example.android.group4.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.group4.R;
import com.example.android.group4.db.DBHelper;
import com.example.android.group4.models.AccelerometerDatum;
import com.example.android.group4.models.Patient;
import com.example.android.group4.receivers.DataReceiver;
import com.example.android.group4.services.SensorHandlerService;
import com.example.android.group4.utils.NotificationUtil;
import com.example.android.group4.utils.SharedPreferenceUtil;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class PartAActivity extends AppCompatActivity {

    public static int PATIENT_INFO_REQUEST = 1;
    Patient patient;

    public LineGraphSeries<DataPoint> series1;
    public LineGraphSeries<DataPoint> series2;
    public LineGraphSeries<DataPoint> series3;
    GraphView graph;

    int timercount = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_a);
        setup();
    }

    public void setup(){
        if(!checkPatientData()){
            return;
        }
        fillPatientData();
        instantiateDatabase();
        Log.v("Part A", "Database Instantiated");
        NotificationUtil.makeAToast(this, "Database Instantiated");
        initiateService();
        Log.v("Part A", "Service Initiated");
        NotificationUtil.makeAToast(this, "Database Instantiated");
        setupGraphInitial();
    }

    public void instantiateDatabase(){
        DBHelper.initPatientTable(patient);
    }

    public void initiateService(){
        if(SensorHandlerService.isServiceRunning(this)){
            return;
        }
        SensorHandlerService.startService(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == PATIENT_INFO_REQUEST) {
           checkPatientData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPatientData();
        IntentFilter filter = new IntentFilter(SensorHandlerService.ACTION);
        registerReceiver(dataReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(dataReceiver);
    }

    public boolean checkPatientData(){
        patient = SharedPreferenceUtil.getCurrentPatient();
        if(patient == null){
            //If Patient is not set
            Intent intent = new Intent(this, PatientInfoActivity.class);
            startActivity(intent);
            finish();
            return false;
        }
        return true;
    }

    // Define the callback for what to do when data is received
    public DataReceiver dataReceiver = new DataReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AccelerometerDatum accelerometerDatum = (AccelerometerDatum) intent.getSerializableExtra("data");
            onReceiveData(accelerometerDatum);

        }
    };

    public void onReceiveData(AccelerometerDatum accelerometerDatum){

//        System.out.println(accelerometerDatum);
//        Toast.makeText(this, "On received Reached", Toast.LENGTH_LONG).show();
        series1.appendData(new DataPoint(timercount,(int)accelerometerDatum.getX()), true, 40);
        series2.appendData(new DataPoint(timercount,(int)accelerometerDatum.getY()), true, 40);
        series3.appendData(new DataPoint(timercount,(int)accelerometerDatum.getZ()), true, 40);
        timercount++;
        graph.addSeries(series1);
        graph.addSeries(series2);
        graph.addSeries(series3);


    }

    public void fillPatientData(){
        ((TextView) findViewById(R.id.pId)).setText(patient.getId());
        ((TextView) findViewById(R.id.patientName)).setText(patient.getName());
        ((TextView) findViewById(R.id.patientGender)).setText(patient.getSex());
        ((Button) findViewById(R.id.fill_patient_data_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PartAActivity.this, PatientInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SensorHandlerService.stopService(this);
    }

    public void setupGraphInitial(){
        Toast.makeText(this, "setup initial", Toast.LENGTH_LONG).show();
        graph = (GraphView) findViewById(R.id.graphA);
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

    }

    static class ViewHolder{
        GraphView graphView;

    }

}
