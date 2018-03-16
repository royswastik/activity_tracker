package com.example.android.group4.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by jaydeep on 3/15/18.
 */

public class NotificationUtil {
    public static void makeAToast(Activity activity, String msg){
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }
}
