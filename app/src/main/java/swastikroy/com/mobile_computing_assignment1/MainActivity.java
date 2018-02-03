package swastikroy.com.mobile_computing_assignment1;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.List;

import swastikroy.com.mobile_computing_assignment1.models.HealthDatum;
import swastikroy.com.mobile_computing_assignment1.ui.views.GraphView;
import swastikroy.com.mobile_computing_assignment1.util.RandomHealthDataGenerator;

public class MainActivity extends AppCompatActivity {

    ViewHolder holder;
    public static int FLOAT_ARRAY_SIZE = 50;
    float[] dataPoints;
    public static int MAX_Y_AXIS = 3000;
    public static int MIN_Y_AXIS = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_monitor);

        setup();
    }

    public void setup(){
        this.holder = new ViewHolder(this);
        addListeners();

    }


    public void addListeners(){
        holder.runBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runMonitoring();
            }
        });

        holder.stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopMonitoring();
            }
        });
    }



    public void runMonitoring(){
        List<HealthDatum> dummy_health_data = RandomHealthDataGenerator.get_dummy_health_data(FLOAT_ARRAY_SIZE, MAX_Y_AXIS, MIN_Y_AXIS);
        dataPoints = new float[FLOAT_ARRAY_SIZE];
        for(int i = 0; i < FLOAT_ARRAY_SIZE; i++){
            dataPoints[i] = (float) dummy_health_data.get(i).getY();
        }
        holder.graph.setValues(dataPoints);
        drawGraph(dataPoints);
//        DataPoint[] dataPoints = new DataPoint[FLOAT_ARRAY_SIZE];
//        for(int i = 0; i < dummy_health_data.size(); i++){
//            dataPoints[i] = new DataPoint(dummy_health_data.get(i).getX(),dummy_health_data.get(i).getY());
//        }
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
//        holder.graph.removeAllSeries();
//        holder.graph.addSeries(series);
// Start the initial runnable task by posting through the handler
        handler.removeCallbacks(runnableCode);
        handler.post(runnableCode);
    }

    public void drawGraph(float[] points){
        String[] horLabels = {"2700", "2750", "2800", "2850", "2900", "2950", "3000", "3050", "3100"};
        String[] verLabels = {"500", "1000", "1500", "2000"};
        holder.graph  = new GraphView(this, points,"Heart Rate Monitor", horLabels, verLabels, GraphView.LINE);
        holder.graphViewContainer.removeAllViews();
        holder.graphViewContainer.addView(holder.graph);
    }

    // Create the Handler object (on the main thread by default)
    Handler handler = new Handler();
    // Define the code block to be executed
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            updateGraph();
            // Do something here on the main thread
            Log.d("Handlers", "Called on main thread");
            handler.postDelayed(this, 100);
        }
    };
// Run

    public void updateGraph(){
        float[] oldDataPoints = dataPoints;
        List<HealthDatum> dummy_health_data = RandomHealthDataGenerator.get_dummy_health_data(2, MAX_Y_AXIS, MIN_Y_AXIS);
        dataPoints = new float[FLOAT_ARRAY_SIZE];
        List<Float> newDataPoints = new ArrayList<>();
        int i = 0 ;
        for(i = 0; i < 2; i++){
            newDataPoints.add((float)dummy_health_data.get(i).getY());
        }

        for(i = i; i < FLOAT_ARRAY_SIZE; i++){
//            dataPoints[i] = oldDataPoints[i-10];
            newDataPoints.add(oldDataPoints[i-1]);
        }
        for(int j = 0; j < newDataPoints.size(); j++){
            dataPoints[j] = newDataPoints.get(j);
        }
        holder.graph.setValues(dataPoints);
        drawGraph(dataPoints);
    }

    public void stopMonitoring(){
        holder.graph.setValues(new float[]{});
        drawGraph(new float[]{});
        handler.removeCallbacks(runnableCode);
        holder.graphViewContainer.removeAllViews();

//        holder.graph.removeAllSeries();
    }

    static class ViewHolder{
        GraphView graph;
        Button runBtn, stopBtn;
        LinearLayout graphViewContainer;
        public ViewHolder(Activity activity) {
            String[] horLabels = {"2700", "2750", "2800", "2850", "2900", "2950", "3000", "3050", "3100"};
            String[] verLabels = {"500", "1000", "1500", "2000"};
            graph = new GraphView(activity, new float[]{},"Heart Rate Monitor", horLabels, verLabels, false);
            runBtn = (Button) activity.findViewById(R.id.run_btn);
            stopBtn = (Button) activity.findViewById(R.id.stop_btn);
            graphViewContainer = (LinearLayout) activity.findViewById(R.id.graphview_container);
            graphViewContainer.addView(graph);
        }
    }
}
