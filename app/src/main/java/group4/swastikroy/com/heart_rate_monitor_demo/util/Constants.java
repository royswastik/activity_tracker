package group4.swastikroy.com.heart_rate_monitor_demo.util;

import android.os.Environment;

/**
 * Created by sroy41 on 3/29/2018.
 */

public class Constants {
    public static interface ACTIONS{
        String RUN = "RUN";
        String JUMP = "JUMP";
        String WALK = "WALK";
        String UNKNOWN = "UNKNOWN";
        String IDLE = "IDLE";
    }


    public static String svmModelName = "SVMModelSave.txt";  //SVM Model name
    public static String baseModelDir="/Android/Data/CSE535_ASSIGNMENT3";
    //Path where db to be created
    public static String MFilePath = baseModelDir+"/"+svmModelName;
    public static String modelFilePath = Environment.getExternalStorageDirectory() + MFilePath;

}
