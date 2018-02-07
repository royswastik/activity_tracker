package swastikroy.com.mobile_computing_assignment1;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import swastikroy.com.mobile_computing_assignment1.models.HealthDatum;
import swastikroy.com.mobile_computing_assignment1.models.Patient;
import swastikroy.com.mobile_computing_assignment1.util.NotificationUtil;
import swastikroy.com.mobile_computing_assignment1.util.RandomHealthDataGenerator;

public class MainActivity2 extends AppCompatActivity {

    ViewHolder holder;
    public static int FLOAT_ARRAY_SIZE = 140;
    List<DataPoint> dataPoints;
    public static int MAX_Y_AXIS = 2400;
    public static int MIN_Y_AXIS = 0;
    private LineGraphSeries<DataPoint> series;
    long lastTimeStamp = 0;
    long currSecond = 0;
    boolean running = false;
    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_monitor);

        setup();
    }

    public void setup(){
        this.holder = new ViewHolder(this);
        addListeners();

        this.holder.graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {

                if (isValueX) {
                    value = value*5;
//                    long currTimeStamp = System.currentTimeMillis();
//                    if(lastTimeStamp == 0){
//                        currSecond++;
//                    }
//                    else if(currTimeStamp >=lastTimeStamp+1000){
//                        currSecond++;
//                    }
//                    else{
//                        return "";
//                    }
//                    lastTimeStamp = currTimeStamp;

                    // show normal x values
                    if((((int)value) - value) >0.01){
                        return "";
                    }
                    return super.formatLabel((int)value, isValueX);
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX);
                }
            }
        });

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
//        dataPoints = new float[FLOAT_ARRAY_SIZE];
//        for(int i = 0; i < FLOAT_ARRAY_SIZE; i++){
//            dataPoints[i] = (float) dummy_health_data.get(i).getY();
//        }
//        holder.graph.setValues(dataPoints);
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


// Start the initial runnable task by posting through the handler
        handler.removeCallbacks(runnableCode);
        handler.post(runnableCode);
        running = true;
    }

    public void drawGraph(List<HealthDatum> dummy_health_data){

        dataPoints = new ArrayList<>();
        for(int i = 0; i < dummy_health_data.size(); i++){
            dataPoints.add(new DataPoint(dummy_health_data.get(i).getX(),dummy_health_data.get(i).getY()));
        }

        series = new LineGraphSeries<>(toDataPointArray(dataPoints));
        series.setThickness(8);
//        series.setBackgroundColor(Color.BLACK);
        series.setColor(Color.rgb(22,160,133));

//        holder.graph.removeAllSeries();
        holder.graph.addSeries(series);
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

    public DataPoint[] toDataPointArray(List<DataPoint> dataPointsList){
        DataPoint[] arr = new DataPoint[dataPointsList.size()];
        for(int i=0; i<arr.length; i++){
            arr[i] = dataPointsList.get(i);
        }
        return arr;
    }

    public void updateGraph(){
        DataPoint[] newDataPoints = RandomHealthDataGenerator.get_dummy_health_data_list(2, MAX_Y_AXIS, MIN_Y_AXIS);
//        dataPoints = new DataPoint[FLOAT_ARRAY_SIZE];
        int i = 0 ;
        for(i = 0; i < 2; i++){
//            dataPoints[i] = new DataPoint(newDataPoints[i].getX(),newDataPoints[i].getY());
            DataPoint lastValue = dataPoints.get(dataPoints.size()-1);
            DataPoint newDataPoint = new DataPoint(lastValue.getX()+0.01d, newDataPoints[i].getY());
            dataPoints.add(newDataPoint);
            dataPoints.remove(0);
            series.appendData(newDataPoint, true, 40);
        }
        series.resetData(toDataPointArray(dataPoints));
//        holder.graph.removeAllSeries();
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>((DataPoint[]) dataPoints.toArray());
//        holder.graph.appe(series);
//        series.appendData(newDataPointsArr);
    }



    public void stopMonitoring(){
//        holder.graph.setValues(new float[]{});
//        drawGraph(new float[]{});

        handler.removeCallbacks(runnableCode);
//        holder.graphViewContainer.removeAllViews();
//        holder.graph.removeAllSeries();

        holder.graph.removeAllSeries();
        running = false;
    }

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
//        holder.graph.setTitle(patient.getName());
        NotificationUtil.notify("Hello "+patient.getName()+"! Saved.", this);
//        series.setTitle(patient.getName());
    }

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
//            graph.getGridLabelRenderer().setGridColor(Color.BLACK);

            graph.getGridLabelRenderer().setGridStyle( GridLabelRenderer.GridStyle.HORIZONTAL );
//            graph.setTitle("Patient Not Set(Getting dummy data)");
//            graph.getLegendRenderer().setVisible(true);





        }
    }
}
