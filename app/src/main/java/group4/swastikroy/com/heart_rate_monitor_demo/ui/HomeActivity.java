package group4.swastikroy.com.heart_rate_monitor_demo.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import group4.swastikroy.com.heart_rate_monitor_demo.db.DBHelper;
import group4.swastikroy.com.heart_rate_monitor_demo.util.NotificationUtil;
import group4.swastikroy.com.heart_rate_monitor_demo.util.SVMUtil;

public class HomeActivity extends AppCompatActivity {

    private Button collectData;
    private Button classifier, clearDataBtn, showGraphButton,showGraphPerformanceButton,realTimeDataCollectionButton;

    DBHelper database = new DBHelper(this);
    public static final String TABLE_NAME = "accelerometer_data_table";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setContentView(R.layout.activity_main);
        collectData = (Button) findViewById(R.id.collect_btn);
        classifier = (Button) findViewById(R.id.button_classifier);
        clearDataBtn = (Button) findViewById(R.id.clearBtn);
        showGraphButton = (Button) findViewById(R.id.showGraphButton);
        realTimeDataCollectionButton = (Button) findViewById(R.id.realTimeDataCollection);

        database.createTable();

        collectData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dataActivityIntent = new Intent(HomeActivity.this, DataCollectionListActivity.class);
                startActivity(dataActivityIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        clearDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });

        classifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent trainingIntent = new Intent(HomeActivity.this, ClassificationActivity.class);
                startActivity(trainingIntent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                String accuracy = svmAccuracy.calculateAccuracy();
//                textView.setText("Accuracy = " + accuracy);

            }
        });

        showGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONArray jump = database.getDataForGraph("JUMP");
                JSONArray walk = database.getDataForGraph("WALK");
                JSONArray run = database.getDataForGraph("RUN");


                Intent loadGraphActivityIntent = new Intent(HomeActivity.this, DataVisualizationActivity.class);

                loadGraphActivityIntent.putExtra("jump", String.valueOf(jump));
                loadGraphActivityIntent.putExtra("walk", String.valueOf(walk));
                loadGraphActivityIntent.putExtra("run", String.valueOf(run));

                startActivity(loadGraphActivityIntent);

            }
        });


        realTimeDataCollectionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent loadRealTimeDataCollectionIntent = new Intent(HomeActivity.this, RealTimeDataCollectionActivity.class);
                startActivity(loadRealTimeDataCollectionIntent);
            }
        });
    }

    private void clearData(){
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to delete data?")
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        database.cleardata();
                        NotificationUtil.makeToast(HomeActivity.this, "Data Cleared");
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        dialog.show();

    }

//    private void moveFile(String outputPath, String filename) {
//
//        InputStream in = null;
//        OutputStream out = null;
//        try {
//
//            //create output directory if it doesn't exist
//            File dir = new File(outputPath);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//
//
//            in = getAssets().open(filename);
//            out = new FileOutputStream(outputPath + filename);
//
//            byte[] buffer = new byte[1024];
//            int read;
//            while ((read = in.read(buffer)) != -1) {
//                out.write(buffer, 0, read);
//            }
//            in.close();
//            in = null;
//
//            // write the output file
//            out.flush();
//            out.close();
//            out = null;
//
//
//        } catch (FileNotFoundException fnfe1) {
//            Log.e("tag", fnfe1.getStackTrace().toString());
//
//        } catch (Exception e) {
//            Log.e("tag", e.getStackTrace().toString());
//        }
//
//    }
//

}
