package group4.swastikroy.com.heart_rate_monitor_demo.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import group4.swastikroy.com.heart_rate_monitor_demo.R;
import group4.swastikroy.com.heart_rate_monitor_demo.model.PowerConsumption;

public class PerformanceVisualizationActivity extends AppCompatActivity {

    ViewHolder holder;
    private LineGraphSeries<DataPoint> series;

    List<DataPoint> dataPoints;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_performance_visualization);
//        setup();
//        plotGraphs();
    }

    private void setup(){
        holder = new ViewHolder(this);
        new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Power Consumption Data.");
        progressDialog.setCancelable(false);
    }

    private void plotGraphs(){
        progressDialog.show();
        drawGraph(getTrainingPowerConsumption(), holder.training_graph);
        drawGraph(getTestPowerConsumption(), holder.test_graph);
        progressDialog.hide();
    }



    public void drawGraph(List<PowerConsumption> dummy_health_data, GraphView graphView){

        dataPoints = new ArrayList<>();
        for(int i = 0; i < dummy_health_data.size(); i++){
            dataPoints.add(new DataPoint(dummy_health_data.get(i).getTimeInstance(),dummy_health_data.get(i).getPowerConsumption()));
        }

        series = new LineGraphSeries<>(toDataPointArray(dataPoints));
        series.setThickness(8);
        series.setColor(Color.rgb(22,160,133));
        graphView.addSeries(series);
    }

    private List<PowerConsumption> getTrainingPowerConsumption(){
        return null;    //TODO
        //should return, power consumption vs time for dummy health data
    }

    private List<PowerConsumption> getTestPowerConsumption(){
        return null;    //TODO
    }

    /**
     * Convert list of #DataPoint to array
     * @param dataPointsList
     * @return
     */
    public DataPoint[] toDataPointArray(List<DataPoint> dataPointsList){
        DataPoint[] arr = new DataPoint[dataPointsList.size()];
        for(int i=0; i<arr.length; i++){
            arr[i] = dataPointsList.get(i);
        }
        return arr;
    }


    static class ViewHolder{
        GraphView training_graph, test_graph;
        View container;
        ViewHolder(Activity activity){
            training_graph = (GraphView)activity.findViewById(R.id.training_graph);
            test_graph = (GraphView)activity.findViewById(R.id.test_graph);
            container = activity.findViewById(R.id.container);
        }
    }
}
