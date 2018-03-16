package com.example.android.group4.utils;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.android.group4.Group4App;
import com.example.android.group4.PatientData;
import com.example.android.group4.models.Patient;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jaydeep on 3/15/18.
 */

public class SharedPreferenceUtil {
    public static Logger logger = Logger.getLogger(SharedPreferenceUtil.class.toString());
    private static final String CURRENT_PATIENT = "CURRENT_PATIENT";


    public static Patient getCurrentPatient(){
        try {
            ObjectMapper jsonMapper = new ObjectMapper();
            SharedPreferences pref = Group4App.getDefaultSharedPreferences();
            String patientDataString = pref.getString(CURRENT_PATIENT, "");
            if (!patientDataString.isEmpty()) {
                Patient patientData = jsonMapper.readValue(patientDataString, Patient.class);
                return patientData;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Input Retrieve Exception", e);
        }

        return null;
    }

    public static void saveCurrentPatient(Patient patientData){
        if (patientData == null)
            return;
        try {
            ObjectMapper jsonMapper = new ObjectMapper();
            String patientDataString = jsonMapper.writeValueAsString(patientData);
            SharedPreferences pref = Group4App.getDefaultSharedPreferences();
            pref.edit().putString(CURRENT_PATIENT, patientDataString).apply();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Current Patient Save Exception", e);
        }
    }

}
