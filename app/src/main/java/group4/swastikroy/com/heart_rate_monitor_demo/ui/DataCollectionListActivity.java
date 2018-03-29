package group4.swastikroy.com.heart_rate_monitor_demo.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import group4.swastikroy.com.heart_rate_monitor_demo.R;

public class DataCollectionListActivity extends AppCompatActivity {

    private Button runButton, walkButton, jumpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collection_list);

        runButton = (Button) findViewById(R.id.run_button);
        walkButton = (Button) findViewById(R.id.walk_button);
        jumpButton = (Button) findViewById(R.id.jump_button);

        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent runActivityIntent = new Intent(DataCollectionListActivity.this, DataCollectionActivity.class);
                runActivityIntent.putExtra("action", "RUN");
                startActivity(runActivityIntent);
            }
        });

        jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jumpActivityIntent = new Intent(DataCollectionListActivity.this, DataCollectionActivity.class);
                jumpActivityIntent.putExtra("action", "JUMP");
                startActivity(jumpActivityIntent);
            }
        });

        walkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent walkActivityIntent = new Intent(DataCollectionListActivity.this, DataCollectionActivity.class);
                walkActivityIntent.putExtra("action", "WALK");
                startActivity(walkActivityIntent);
            }
        });
    }
}
