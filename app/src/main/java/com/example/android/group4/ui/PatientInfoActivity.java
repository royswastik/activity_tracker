package com.example.android.group4.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

    public void savePatientData(Patient patient){
        SharedPreferenceUtil.saveCurrentPatient(patient);
        setResult(RESULT_OK);
        finish();
    }
}
