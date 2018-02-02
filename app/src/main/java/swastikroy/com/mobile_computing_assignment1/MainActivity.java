package swastikroy.com.mobile_computing_assignment1;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import swastikroy.com.mobile_computing_assignment1.models.HealthDatum;
import swastikroy.com.mobile_computing_assignment1.util.RandomHealthDataGenerator;

public class MainActivity extends AppCompatActivity {

    ViewHolder holder;
    public static int FLOAT_ARRAY_SIZE = 20;
    public static int MAX_Y_AXIS = 3000;
    public static int MIN_Y_AXIS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        DataPoint[] dataPoints = new DataPoint[FLOAT_ARRAY_SIZE];
        for(int i = 0; i < dummy_health_data.size(); i++){
            dataPoints[i] = new DataPoint(dummy_health_data.get(i).getX(),dummy_health_data.get(i).getY());
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        holder.graph.removeAllSeries();
        holder.graph.addSeries(series);

    }

    public void stopMonitoring(){
        holder.graph.removeAllSeries();
    }

    static class ViewHolder{
        GraphView graph;
        Button runBtn, stopBtn;
        public ViewHolder(Activity activity) {
            graph = (GraphView) activity.findViewById(R.id.graph);
            runBtn = (Button) activity.findViewById(R.id.run_btn);
            stopBtn = (Button) activity.findViewById(R.id.stop_btn);
        }
    }
}
