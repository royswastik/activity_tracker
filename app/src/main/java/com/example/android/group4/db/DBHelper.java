package com.example.android.group4.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;

import com.example.android.group4.models.*;
import com.example.android.group4.utils.Constants;

import java.io.File;

/**
 * Created by jaydeep on 3/15/18.
 */

public class DBHelper {
    SQLiteDatabase db;
    String downloadDir = "/CSE535_Assignment2/Other/";    //folder for download
    static String dbFileName = "Group4.db";  //databse name
    static String baseDbDir="/Android/Data/CSE535_ASSIGNMENT2";
    //Path where db to be created
    static String dbFilePath = baseDbDir+"/"+dbFileName;
    public static void saveToDB(){

    }

    public static void saveToDB(Patient patientData){
        checkDBFolder();    //This creates the database folder if not created
        SQLiteDatabase db =  SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + dbFilePath, null);
        db.beginTransaction();
        try
        {
            String tableName = patientData.getName() + "_" + patientData.getId() + "_" + patientData.getAge() + "_" + patientData.getSex();
                db.execSQL("CREATE TABLE " + tableName +
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
}
