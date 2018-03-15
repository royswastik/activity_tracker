package com.example.android.group4;

import android.provider.BaseColumns;

public class PatientData {
    private PatientData() {}

    public static final class PatientEntry implements BaseColumns {

        //public final static String TABLE_NAME = "pets";

        public final static String COLUMN_PATIENT_ID = "id";
        public final static String COLUMN_PATIENT_NAME = "name";
        public final static String COLUMN_PET_AGE = "age";
        public final static String COLUMN_PATIENT_SEX = "sex";

        public static final int GENDER_MALE = 0;
        public static final int GENDER_FEMALE = 0;

    }
}