package swastikroy.com.group4;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import swastikroy.com.group4.models.HealthDatum;
import swastikroy.com.group4.models.Patient;
import swastikroy.com.group4.util.NotificationUtil;
import swastikroy.com.group4.util.RandomHealthDataGenerator;

/**
 * This is the main activity
 */
public class HeartRateMonitorActivity extends AppCompatActivity {

    ViewHolder holder;  //This holds all view instances
    public static int FLOAT_ARRAY_SIZE = 140;
    List<DataPoint> dataPoints;
    public static int MAX_Y_AXIS = 2400;    //Max value of Heart Rate
    public static int MIN_Y_AXIS = 0;       //Min value of Heart Rate
    private LineGraphSeries<DataPoint> series;      //This stores instances of heart rate to show in graph
    boolean running = false;    //Flag to check if graph is running
    Patient patient;       //To store patient data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_monitor);
        setup();
    }



    /**
     * Sets up everything in the app
     */
    public void setup(){
        this.holder = new ViewHolder(this);
        addListeners();

        this.holder.graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {

                if (isValueX) {
                    value = value*5;
                    if((((int)value) - value) >0.01){
                        return "";
                    }
                    return super.formatLabel((int)value, isValueX);
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });

    }

    /*
     * Adds listeners to UI elements
     */
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


    /*
     * Runs Monitoring task when 'Run' button is pressed
     */
    public void runMonitoring(){
        List<HealthDatum> dummy_health_data = RandomHealthDataGenerator.get_dummy_health_data(FLOAT_ARRAY_SIZE, MAX_Y_AXIS, MIN_Y_AXIS);
        if(running == true){
            NotificationUtil.notify("Already running", this);
            return;
        }
        if(dataPoints == null){
            drawGraph(dummy_health_data);
        }
        else{
            holder.graph.addSeries(series);
        }
        holder.stopBtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorStopBtn));
        holder.runBtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorGray));
        handler.removeCallbacks(runnableCode);
        handler.post(runnableCode);
        running = true;
    }

    public void drawGraph(List<HealthDatum> dummy_health_data){

        dataPoints = new ArrayList<>();
        for(int i = 0; i < dummy_health_data.size(); i++){
            dataPoints.add(new DataPoint(dummy_health_data.get(i).getTime(),dummy_health_data.get(i).getValue()));
        }

        series = new LineGraphSeries<>(toDataPointArray(dataPoints));
        series.setThickness(8);
        series.setColor(Color.rgb(22,160,133));
        holder.graph.addSeries(series);
    }

    Handler handler = new Handler();
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            updateGraph();
            handler.postDelayed(this, 100);
        }
    };

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

    /**
     * Called to update graph when new data is fetched
     */
    public void updateGraph(){
        DataPoint[] newDataPoints = RandomHealthDataGenerator.get_dummy_health_data_list(2, MAX_Y_AXIS, MIN_Y_AXIS);
        int i = 0 ;
        for(i = 0; i < 2; i++){
            DataPoint lastValue = dataPoints.get(dataPoints.size()-1);
            DataPoint newDataPoint = new DataPoint(lastValue.getX()+0.01d, newDataPoints[i].getY());
            dataPoints.add(newDataPoint);
            dataPoints.remove(0);
            series.appendData(newDataPoint, true, 40);
        }
        series.resetData(toDataPointArray(dataPoints));
    }

    /**
     * Called when 'stop' button is clicked
     */
    public void stopMonitoring(){
        handler.removeCallbacks(runnableCode);

        holder.graph.removeAllSeries();
        holder.stopBtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorGray));
        holder.runBtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorRunBtn));
        running = false;
    }

    /**
     * Method to save patient information
     * @param view
     */
    public void savePatient(View view){
        patient = new Patient();
        patient.setName(((EditText)findViewById(R.id.patient_name)).getText().toString());
        patient.setId(((EditText)findViewById(R.id.patient_id)).getText().toString());

        if(patient.getName().trim().length() == 0){
            NotificationUtil.notify("Please set patient name", this);
            return;
        }
        if(patient.getId().trim().length() == 0){
            NotificationUtil.notify("Please set patient id", this);
            return;
        }
        NotificationUtil.notify("Hello "+patient.getName()+"! Saved.", this);
    }

    /**
     * Class to contain all the views
     */
    static class ViewHolder{
        GraphView graph;
        Button runBtn, stopBtn;
        View container;
        public ViewHolder(Activity activity) {
            container = activity.findViewById(R.id.container);
            runBtn = (Button) activity.findViewById(R.id.run_btn);
            graph = (GraphView) activity.findViewById(R.id.graph);
            stopBtn = (Button) activity.findViewById(R.id.stop_btn);

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(0.5);
            graph.getViewport().setMaxY(2400);
            graph.getGridLabelRenderer().setVerticalAxisTitle("Heart Rate");
            graph.getGridLabelRenderer().setHorizontalAxisTitle("Seconds");

            graph.getGridLabelRenderer().setGridStyle( GridLabelRenderer.GridStyle.HORIZONTAL );
        }
    }
}