package com.example.android.group4.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.group4.R;
import com.example.android.group4.db.DBHelper;
import com.example.android.group4.models.Patient;
import com.example.android.group4.services.SensorHandlerService;
import com.example.android.group4.utils.SharedPreferenceUtil;
import com.jjoe64.graphview.GraphView;

public class PartAActivity extends AppCompatActivity {

    public static int PATIENT_INFO_REQUEST = 1;
    Patient patient;

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
        instantiateDatabase();
        initiateService();

    }

    public void instantiateDatabase(){
        DBHelper.initDB(patient);
    }

    public void initiateService(){
        Intent i = new Intent(this, SensorHandlerService.class);
        startService(i);
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
    }

    public boolean checkPatientData(){
        patient = SharedPreferenceUtil.getCurrentPatient();
        if(patient == null){
            //If Patient is not set
            Intent intent = new Intent(this, PatientInfoActivity.class);
            startActivityForResult(intent, PATIENT_INFO_REQUEST);
            return false;
        }
        return true;
    }

    public void setupGraphInitial(){

    }

    static class ViewHolder{
        GraphView graphView;

    }

}
