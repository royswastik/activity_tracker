package com.example.android.group4.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.group4.R;
import com.example.android.group4.helpers.PermissionHelper;

public class ListActivity extends AppCompatActivity {

    Button partABtn, partBBtn, partCBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        PermissionHelper.checkPermissions(this);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if (requestCode == PermissionHelper.PERMISSION_REQUEST) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    finishAffinity();
                }
            }
        }
    }
}
