package group4.swastikroy.com.heart_rate_monitor_demo.util;

/**
 * Created by sroy41 on 3/29/2018.
 */

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;


public class PermissionUtil {
    public static final int PERMISSION_REQUEST = 1;

    public static void checkPermissions(Activity activity){
//        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                ) {
//            ActivityCompat.requestPermissions(activity,
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    PERMISSION_REQUEST);
//        }
    }
}