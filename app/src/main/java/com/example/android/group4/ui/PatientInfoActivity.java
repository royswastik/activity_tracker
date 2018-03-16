package com.example.android.group4.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.android.group4.R;
import com.example.android.group4.models.Patient;
import com.example.android.group4.utils.SharedPreferenceUtil;

public class PatientInfoActivity extends AppCompatActivity {

    //EditText inputs
    EditText idValue, ageValue, nameString;
    RadioGroup sexGroup;

    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);

        //Getting patient data
        idValue = (EditText) findViewById(R.id.patient_id);
        ageValue = (EditText) findViewById(R.id.patient_age);
        nameString = (EditText) findViewById(R.id.patient_name);
        sexGroup = (RadioGroup) findViewById(R.id.patient_sex);
        saveBtn = (Button) findViewById(R.id.save_patient_data);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Patient patient = new Patient();
                patient.setId(idValue.getText().toString());
                patient.setName(nameString.getText().toString());
                patient.setAge(ageValue.getText().toString());
                int sexValue = sexGroup.getCheckedRadioButtonId();
                RadioButton rd = (RadioButton) findViewById(sexValue);
                String patientSex = rd.getText().toString();
                patient.setSex(patientSex);
                savePatientData(patient);
            }
        });
    }

    public boolean isValidated(){
        if(idValue.getText().toString().length() == 0 || idValue.getText().toString().isEmpty()){
            Toast.makeText(PatientInfoActivity.this, "Please select your id", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(ageValue.getText().toString().length() == 0 || ageValue.getText().toString().isEmpty()){
            Toast.makeText(PatientInfoActivity.this, "Please select your age", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(nameString.getText().toString().length() == 0 || nameString.getText().toString().isEmpty()){
            Toast.makeText(PatientInfoActivity.this, "Please select your name", Toast.LENGTH_SHORT).show();
            return false;
        }
        int sexValue = sexGroup.getCheckedRadioButtonId();
        RadioButton rd = (RadioButton) findViewById(sexValue);
        String patientSex = rd.getText().toString();
        if(patientSex.toString().length() == 0 || patientSex.toString().isEmpty()){
            Toast.makeText(PatientInfoActivity.this, "Please select your sex", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void savePatientData(Patient patient){
        if(!isValidated()){
            return;
        }
        SharedPreferenceUtil.saveCurrentPatient(patient);
        Intent intent = new Intent(PatientInfoActivity.this, PartAActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(PatientInfoActivity.this, ListActivity.class);
        startActivity(intent);
        finish();
    }
}
