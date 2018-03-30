package group4.swastikroy.com.heart_rate_monitor_demo.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import group4.swastikroy.com.heart_rate_monitor_demo.R;
import group4.swastikroy.com.heart_rate_monitor_demo.util.ActivityUtil;

public class DataCollectionListActivity extends AppCompatActivity {

    ViewHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collection_list);
        holder = new ViewHolder(this);
        addListeners();
    }

    private void addListeners(){
        holder.runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCollectionActivity("RUN");
            }
        });

        holder.jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCollectionActivity("JUMP");
            }
        });

        holder.walkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCollectionActivity("WALK");
            }
        });
    }

    private void startCollectionActivity(String action){
        Bundle bundle = new Bundle();
        bundle.putString("action", action);
        ActivityUtil.openNewActivity(DataCollectionListActivity.this, DataCollectionActivity.class, bundle);
    }

    static class ViewHolder{
        Button runButton, walkButton, jumpButton;
        ViewHolder(Activity activity){
            runButton = (Button) activity.findViewById(R.id.run_button);
            walkButton = (Button) activity.findViewById(R.id.walk_button);
            jumpButton = (Button) activity.findViewById(R.id.jump_button);
        }
    }
}
