package group4.swastikroy.com.heart_rate_monitor_demo.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import group4.swastikroy.com.heart_rate_monitor_demo.R;
import group4.swastikroy.com.heart_rate_monitor_demo.db.DatabaseHandler;
import group4.swastikroy.com.heart_rate_monitor_demo.util.SVMUtil;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MAIN_ACTIVITY";
    private Button collectData;
    private Button trainSVM, clearDataBtn, showGraphButton;
    private TextView textView;
    DatabaseHandler database = new DatabaseHandler(this);
    public static final String TABLE_NAME = "accelerometer_data_table";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moveFile("/data/user/0/com.task3.mc.assignment3/databases/", "SVM");
        moveFile("/data/data/com.task3.mc.assignment3/databases/", "SVM");

        moveFile("/data/user/0/com.task3.mc.assignment3/databases/", "SVM-journal");
        moveFile("/data/data/com.task3.mc.assignment3/databases/", "SVM-journal");


        setContentView(R.layout.activity_main);
        collectData = (Button) findViewById(R.id.collect_btn);
        trainSVM = (Button) findViewById(R.id.button_trainsvm);
        clearDataBtn = (Button) findViewById(R.id.clearBtn);
        showGraphButton = (Button) findViewById(R.id.showGraphButton);
        textView = (TextView) findViewById(R.id.AccuracyTextView);

        database.createTable();

        collectData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dataActivityIntent = new Intent(MainActivity.this, DataCollectionListActivity.class);
                startActivity(dataActivityIntent);
            }
        });

        clearDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.cleardata();
            }
        });

        trainSVM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SVMUtil svmAccuracy = new SVMUtil(database, getApplicationContext());
                String accuracy = svmAccuracy.calculateAccuracy();
                textView.setText("Accuracy = " + accuracy);

            }
        });

        showGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONArray jump = database.getDataForGraph("JUMP");
                JSONArray walk = database.getDataForGraph("WALK");
                JSONArray run = database.getDataForGraph("RUN");

                Intent loadGraphActivityIntent = new Intent(MainActivity.this, DataVisualizationActivity.class);

                loadGraphActivityIntent.putExtra("jump", String.valueOf(jump));
                loadGraphActivityIntent.putExtra("walk", String.valueOf(walk));
                loadGraphActivityIntent.putExtra("run", String.valueOf(run));

                startActivity(loadGraphActivityIntent);

            }
        });
    }

    private void moveFile(String outputPath, String filename) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }


            in = getAssets().open(filename);
            out = new FileOutputStream(outputPath + filename);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;


        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getStackTrace().toString());

        } catch (Exception e) {
            Log.e("tag", e.getStackTrace().toString());
        }

    }
}
