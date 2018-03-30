package group4.swastikroy.com.heart_rate_monitor_demo.util;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by sroy41 on 3/29/2018.
 */

public class ActionBarUtil {
    public static void setBackButton(Activity activity){
        ActionBar actionBar = activity.getActionBar();
        
        if(actionBar != null){
          actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if(activity instanceof AppCompatActivity){
            android.support.v7.app.ActionBar actionBarSupport = ((AppCompatActivity) activity).getSupportActionBar();
            actionBarSupport.setDisplayHomeAsUpEnabled(true);
        }
    }
}
