package com.example.android.group4.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;

import com.example.android.group4.models.*;
import com.example.android.group4.utils.Constants;
import com.example.android.group4.utils.SharedPreferenceUtil;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by jaydeep on 3/15/18.
 */

public class DBHelper {
    SQLiteDatabase db;
    String downloadDir = "/CSE535_Assignment2/Other/";    //folder for download
    public static String dbFileName = "Group4.db";  //databse name
    static String baseDbDir="/Android/Data/CSE535_ASSIGNMENT2";
    //Path where db to be created
    public static String dbFilePath = baseDbDir+"/"+dbFileName;

    public static void initPatientTable(Patient patientData){


        checkDBFolder();    //This creates the database folder if not created
        SQLiteDatabase db =  SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + dbFilePath, null);
        if(isTableExists(patientData.retrieve_table_name(), db)){
            return;
        }
        db.beginTransaction();
        try
        {
                db.execSQL("CREATE TABLE " + patientData.retrieve_table_name() +
                        "(" + Constants.KEY_TimeStamp + " BIGINT PRIMARY KEY, " + Constants.KEY_Xaxis + " INTEGER, " + Constants.KEY_Yaxis + " INTEGER, " + Constants.KEY_Zaxis + " INTEGER" + ")");
                db.setTransactionSuccessful();
        }
        catch (SQLiteException e)
        {
//            Toast.makeText(MainActivity.this, "SQLiteException" + e.toString(), Toast.LENGTH_SHORT).show();
        }
        finally
        {
            db.endTransaction();
        }
    }

    public static boolean isTableExists(String tableName, SQLiteDatabase db) //Checks for table
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


    public static void insertAccelerometerData(AccelerometerDatum accelerometerDatum){
        Patient patient = SharedPreferenceUtil.getCurrentPatient();
        SQLiteDatabase db =  SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + dbFilePath, null);

        try
        {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("KEY_TimeStamp", accelerometerDatum.getTimestamp());
            values.put("KEY_Xaxis", accelerometerDatum.getX());
            values.put("KEY_Yaxis", accelerometerDatum.getY());
            values.put("KEY_Zaxis", accelerometerDatum.getZ());
            db.insert(patient.retrieve_table_name(), null, values);


            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static List<AccelerometerDatum> getLast10SecondsDataForPatient(Patient patient){
        //TODO
        SQLiteDatabase db =  SQLiteDatabase.openOrCreateDatabase(Constants.getFullDownloadDir() + dbFileName, null);
        List<AccelerometerDatum> accDataLst = new ArrayList<AccelerometerDatum>();


        final String TABLE_NAME = patient.retrieve_table_name();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY KEY_TimeStamp desc LIMIT 10";
        Cursor cursor = db.rawQuery(selectQuery, null);

        int count = 0;
        if (cursor.moveToFirst())
        {
            do
            {
                AccelerometerDatum acc = new AccelerometerDatum();
                acc.setTimestamp(count++);
                acc.setX(cursor.getLong(1));
                acc.setY(cursor.getLong(2));
                acc.setZ(cursor.getLong(3));

                accDataLst.add(acc);
            } while (cursor.moveToNext());
        }
        cursor.close();


        return accDataLst;
    }

    public static File checkDBFolder(){
        File folder = new File(Environment.getExternalStorageDirectory() + baseDbDir);
        if(!folder.exists() || !folder.isDirectory()){
            boolean folderCreated = folder.mkdir();
            if(!folderCreated){ //Return if could not create folder
                Log.e("DB Folder", "Could not create DB Folder");
                throw new RuntimeException("Could not create DB Folder");
            }
        }
        return folder;
    }

    public static String getDBFilePath(){
        return android.os.Environment.getExternalStorageDirectory().toString() + dbFilePath;
    }
}
