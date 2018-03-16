package com.example.android.group4.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.group4.R;

public class ListActivity extends AppCompatActivity {

    Button partABtn, partBBtn, partCBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        partABtn = (Button) findViewById(R.id.part_a);
        partBBtn = (Button) findViewById(R.id.part_b);
        partCBtn = (Button) findViewById(R.id.part_c);

        partABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(PartAActivity.class);
            }
        });
        partBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(PartBActivity.class);
            }
        });
        partCBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(PartCActivity.class);
            }
        });
    }

    private void openActivity(Class activity){
        Intent intent= new Intent(this, activity);
        startActivity(intent);
    }
}
