package swastikroy.com.mobile_computing_assignment1.util;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by sroy41 on 2/6/2018.
 */

public class NotificationUtil {
    public static void notify(String text, Activity activity){
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
    }
}
