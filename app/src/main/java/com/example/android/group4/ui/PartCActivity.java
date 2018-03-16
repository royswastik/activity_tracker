package com.example.android.group4.ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.group4.R;
import com.example.android.group4.db.DBHelper;
import com.example.android.group4.models.AccelerometerDatum;
import com.example.android.group4.models.Patient;
import com.example.android.group4.utils.NetworkUtil;
import com.example.android.group4.utils.SharedPreferenceUtil;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

public class PartCActivity extends AppCompatActivity {

    Button downloadBtn;
    TextView resp_text;
    ProgressBar progressBar;

    public LineGraphSeries<DataPoint> series1;
    public LineGraphSeries<DataPoint> series2;
    public LineGraphSeries<DataPoint> series3;
    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_c);

        downloadBtn = (Button) findViewById(R.id.download_btn);
        resp_text = (TextView) findViewById(R.id.resp_msg);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadDB();
            }
        });
    }
    public void setupGraphFinal(){

        Patient patient = SharedPreferenceUtil.getCurrentPatient();
        List<AccelerometerDatum> accLst = DBHelper.getLast10SecondsDataForPatient(patient);

        DataPoint[] x_values = new DataPoint[10];
        DataPoint[] y_values = new DataPoint[10];
        DataPoint[] z_values = new DataPoint[10];

        int index = 0;
        for(AccelerometerDatum acc: accLst){
            DataPoint ds_x = new DataPoint(acc.getTimestamp(), acc.getX());
            DataPoint ds_y = new DataPoint(acc.getTimestamp(), acc.getY());
            DataPoint ds_z = new DataPoint(acc.getTimestamp(), acc.getZ());
            x_values[index] = ds_x;
            y_values[index] = ds_y;
            z_values[index] = ds_z;
        }

        Toast.makeText(this, "setup initial", Toast.LENGTH_LONG).show();
        graph = (GraphView) findViewById(R.id.graphC);
        series1 = new LineGraphSeries<>(x_values);
        series2 = new LineGraphSeries<>(y_values);
        series3 = new LineGraphSeries<>(z_values);

        series1.setColor(Color.RED);
        series2.setColor(Color.GREEN);
        series3.setColor(Color.BLUE);

        graph.addSeries(series1);
        graph.addSeries(series2);
        graph.addSeries(series3);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);
        graph.getGridLabelRenderer().setLabelVerticalWidth(60);

        GridLabelRenderer gridLabel;
        gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Time stamp");
        gridLabel.setVerticalAxisTitle("Accelerometer values");

    }

    public void downloadDB(){
        NetworkUtil.downloadFile(new NetworkUtil.IDownloaderListener() {
            @Override
            public void progressUpdate(String[] values) {

            }

            @Override
            public void downloadComplete(int responseCode) {
                resp_text.setText("Download Complete");
            }

            @Override
            public void downloadFailed() {

            }
        });
    }

}
