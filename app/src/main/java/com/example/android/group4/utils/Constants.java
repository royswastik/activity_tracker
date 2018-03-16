package com.example.android.group4.utils;

import com.example.android.group4.db.DBHelper;

import java.nio.file.Paths;

/**
 * Created by jaydeep on 3/15/18.
 */

public class Constants {
    //Graph variables
    public static final String KEY_TimeStamp = "KEY_TimeStamp";
    public static final String KEY_Xaxis = "KEY_Xaxis";
    public static final String KEY_Yaxis = "KEY_Yaxis";
    public static final String KEY_Zaxis = "KEY_Zaxis";


    public static final String PHP_PATH = "http://impact.asu.edu/CSE535Spring18Folder/UploadToServer.php";

    public static final String SERVER_DB_PATH = PHP_PATH+ DBHelper.dbFilePath;

    public static final String DownloadDir = "/Android/Data/CSE535_ASSIGNMENT2_DOWN/";

    public static String getFullDownloadDir(){
        return android.os.Environment.getExternalStorageDirectory() +"/"+ DownloadDir;
    }
}
