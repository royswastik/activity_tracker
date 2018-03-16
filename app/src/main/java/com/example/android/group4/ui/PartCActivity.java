package com.example.android.group4.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.group4.R;
import com.example.android.group4.models.AccelerometerDatum;
import com.example.android.group4.utils.NetworkUtil;

import java.util.List;

public class PartCActivity extends AppCompatActivity {

    Button downloadBtn;
    TextView resp_text;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_c);

        downloadBtn = (Button) findViewById(R.id.upload_btn);
        resp_text = (TextView) findViewById(R.id.resp_msg);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadDB();
            }
        });
    }

    public void downloadDB(){
        NetworkUtil.downloadFile(new NetworkUtil.IDownloaderListener() {
            @Override
            public void progressUpdate(String[] values) {
                
            }

            @Override
            public void downloadComplete(List<AccelerometerDatum> accelerometerData) {

            }

            @Override
            public void downloadFailed() {

            }
        });
    }

}
