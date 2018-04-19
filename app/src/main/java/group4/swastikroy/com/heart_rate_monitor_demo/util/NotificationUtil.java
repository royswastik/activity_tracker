package group4.swastikroy.com.heart_rate_monitor_demo.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import group4.swastikroy.com.heart_rate_monitor_demo.R;

/**
 * Created by sroy41 on 3/29/2018.
 */

public class NotificationUtil {

    private static Toast toastMessage;

    public static void makeToast(Activity activity, String message){

        if(toastMessage != null){
            toastMessage.cancel();
        }
        // also supports Toast.LENGTH_LONG
        toastMessage = Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_SHORT);
        toastMessage.show();
    }
}
