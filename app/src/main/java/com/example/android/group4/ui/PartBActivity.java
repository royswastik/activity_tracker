package com.example.android.group4.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.group4.R;
import com.example.android.group4.utils.NetworkUtil;

import org.w3c.dom.Text;

public class PartBActivity extends AppCompatActivity {

    ProgressBar progressBar, progressBarHorizontal;
    TextView resp_text;
    Button uploadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_b);

        uploadBtn = (Button) findViewById(R.id.upload_btn);
        resp_text = (TextView) findViewById(R.id.resp_msg);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadDB();
            }
        });
    }

    public void uploadDB(){
        progressBar.setVisibility(View.VISIBLE);
        resp_text.setVisibility(View.VISIBLE);
        resp_text.setText("Uploading");
        NetworkUtil.uploadFile(new NetworkUtil.IUploaderListener() {
            @Override
            public void progressUpdate(String[] values) {

            }

            @Override
            public void uploadComplete(int resultCode) {
                resp_text.setText("Upload Complete");
                progressBar.setVisibility(View.GONE);
                progressBar.setProgress(100);
            }

            @Override
            public void uploadFailed(int resultCode) {

            }
        });
    }
}
