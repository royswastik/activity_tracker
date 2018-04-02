package group4.swastikroy.com.heart_rate_monitor_demo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import group4.swastikroy.com.heart_rate_monitor_demo.R;
import group4.swastikroy.com.heart_rate_monitor_demo.db.DBHelper;
import group4.swastikroy.com.heart_rate_monitor_demo.util.SVMUtil;

public class ClassificationActivity extends AppCompatActivity {

    SVMUtil svmUtil;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);

        dbHelper = new DBHelper(this);
        svmUtil = new SVMUtil(dbHelper, getApplicationContext());
    }

    private void train(){
        svmUtil.setHyperParameters();
    }

    private void test(){

    }
}

