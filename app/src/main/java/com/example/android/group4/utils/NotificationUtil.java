package com.example.android.group4.utils;

import android.app.Activity;
import android.widget.Toast;

import com.example.android.group4.Group4App;

/**
 * Created by jaydeep on 3/15/18.
 */

public class NotificationUtil {
    public static void makeAToast(Activity activity, String msg){
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }

    public static void makeAToast(String msg){
        Toast.makeText(Group4App.getAppContext(), msg, Toast.LENGTH_LONG).show();
    }
}
