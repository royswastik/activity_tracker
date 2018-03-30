package group4.swastikroy.com.heart_rate_monitor_demo.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import group4.swastikroy.com.heart_rate_monitor_demo.ui.DataCollectionActivity;
import group4.swastikroy.com.heart_rate_monitor_demo.ui.DataCollectionListActivity;

/**
 * Created by sroy41 on 3/29/2018.
 */

public class ActivityUtil {
    public static void openNewActivity(Activity activity, Class targetActivity, Bundle bundle){
        Intent intent = new Intent(activity, targetActivity);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);
    }
}
