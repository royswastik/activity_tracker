package group4.swastikroy.com.heart_rate_monitor_demo.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;


import group4.swastikroy.com.heart_rate_monitor_demo.db.DBHelper;
import group4.swastikroy.com.heart_rate_monitor_demo.model.AccelerometerAction;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;


public class SVMUtil {

    static String label;
    static int count = 0;

    private svm_parameter param;
    private svm_problem prob;
    private int cross_validation;
    private int nr_fold;
    DBHelper database;
    Context context;

    public SVMUtil(DBHelper database, Context context) {
        this.database = database;
        this.context = context;
    }

    private double accuracy = 0;

    private static List<AccelerometerAction> jumpDataList = new ArrayList<AccelerometerAction>();
    private static List<AccelerometerAction> walkDataList = new ArrayList<AccelerometerAction>();
    private static List<AccelerometerAction> runDataList = new ArrayList<AccelerometerAction>();

    public void setParameters() {

        param = new svm_parameter();
        // default values
        param.svm_type = svm_parameter.C_SVC;
        param.kernel_type = svm_parameter.POLY;
        param.degree = 2;
        param.gamma = 0.007;    // 1/num_features
        param.coef0 = 0;
        param.nu = 0.5;
        param.cache_size = 100;
        param.C = 10000;
        param.eps = 1e-2;
        param.p = 0.1;
        param.shrinking = 1;
        param.probability = 0;
        param.nr_weight = 0;
        param.weight_label = new int[0];
        param.weight = new double[0];
        cross_validation = 1;
        nr_fold = 4;
    }

    private static double atof(String s) {
        double d = Double.valueOf(s).doubleValue();
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            System.err.print("NaN or Infinity in input\n");
            System.exit(1);
        }
        return (d);
    }

    private static int atoi(String s) {
        return Integer.parseInt(s);
    }

    public void extractData() throws IOException {
        try {

            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Log.d("Create File", "SDCard not found.");
            } else {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/mydata/");
                dir.mkdirs();
                File file = new File(dir, "Accdata.txt");

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Accdata.txt", Context.MODE_PRIVATE));
                jumpDataList = database.getData("JUMP");
                walkDataList = database.getData("WALK");
                runDataList = database.getData("RUN");

                for (int i = 0; i < 20; i++) {
                    StringBuilder sb = new StringBuilder("+1 ");

                    int upperBound = (i * 50) + 50;
                    int indexer = 1;
                    for (int j = i * 50; j < upperBound; j++) {
                        sb.append(String.valueOf(indexer++) + ":" + jumpDataList.get(j).getX() + " " + String.valueOf(indexer++) + ":" + jumpDataList.get(j).getY() + " " + String.valueOf(indexer++) + ":" + jumpDataList.get(j).getZ() + " ");
//                        System.out.println(String.valueOf(indexer++) + ":" + jumpDataList.get(j).getX() + " " + String.valueOf(indexer++) + ":" + jumpDataList.get(j).getY() + " " + String.valueOf(indexer++) + ":" + jumpDataList.get(j).getZ() + " ");
                    }
                    sb.append("\n");
                    outputStreamWriter.write(sb.toString());
                }

                for (int i = 0; i < 20; i++) {
                    StringBuilder sb = new StringBuilder("+2 ");
                    int upperBound = (i * 50) + 50;
                    int indexer = 1;
                    for (int j = i * 50; j < upperBound; j++) {
                        sb.append(String.valueOf(indexer++) + ":" + walkDataList.get(j).getX() + " " + String.valueOf(indexer++) + ":" + walkDataList.get(j).getY() + " " + String.valueOf(indexer++) + ":" + walkDataList.get(j).getZ() + " ");
//                        System.out.println(String.valueOf(indexer++) + ":" + walkDataList.get(j).getX() + " " + String.valueOf(indexer++) + ":" + walkDataList.get(j).getY() + " " + String.valueOf(indexer++) + ":" + walkDataList.get(j).getZ() + " ");
                    }
                    sb.append("\n");
                    outputStreamWriter.write(sb.toString());
                }

                for (int i = 0; i < 20; i++) {
                    StringBuilder sb = new StringBuilder("+3 ");
                    int upperBound = (i * 50) + 50;
                    int indexer = 1;
                    for (int j = i * 50; j < upperBound; j++) {
                        sb.append(String.valueOf(indexer++) + ":" + runDataList.get(j).getX() + " " + String.valueOf(indexer++) + ":" + runDataList.get(j).getY() + " " + String.valueOf(indexer++) + ":" + runDataList.get(j).getZ() + " ");
//                        System.out.println(String.valueOf(indexer++) + ":" + runDataList.get(j).getX() + " " + String.valueOf(indexer++) + ":" + runDataList.get(j).getY() + " " + String.valueOf(indexer++) + ":" + runDataList.get(j).getZ() + " ");
                    }
                    sb.append("\n");
                    outputStreamWriter.write(sb.toString());
                }
                Log.d("extractData: ","Data successfully exported to AccData.txt");
                outputStreamWriter.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void read_problem() throws IOException {
        //TODO: where is this file
        FileInputStream in = context.openFileInput("Accdata.txt");
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        Vector<Double> vy = new Vector<Double>();
        Vector<svm_node[]> vx = new Vector<svm_node[]>();
        int max_index = 0;

        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) break;

            System.out.println(line);
            StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");

            vy.addElement(atof(st.nextToken()));
            int m = st.countTokens() / 2;
            svm_node[] x = new svm_node[m];
            for (int j = 0; j < m; j++) {
                x[j] = new svm_node();
                x[j].index = atoi(st.nextToken());
                x[j].value = atof(st.nextToken());
            }
            if (m > 0) max_index = Math.max(max_index, x[m - 1].index);
            vx.addElement(x);
        }

        prob = new svm_problem();
        prob.l = vy.size();
        prob.x = new svm_node[prob.l][];
        for (int i = 0; i < prob.l; i++)
            prob.x[i] = vx.elementAt(i);
        prob.y = new double[prob.l];
        for (int i = 0; i < prob.l; i++)
            prob.y[i] = vy.elementAt(i);

        if (param.gamma == 0 && max_index > 0)
            param.gamma = 1.0 / max_index;

        if (param.kernel_type == svm_parameter.PRECOMPUTED)
            for (int i = 0; i < prob.l; i++) {
                if (prob.x[i][0].index != 0) {
                    System.err.print("Wrong kernel matrix: first column must be 0:sample_serial_number\n");
                    System.exit(1);
                }
                if ((int) prob.x[i][0].value <= 0 || (int) prob.x[i][0].value > max_index) {
                    System.err.print("Wrong input format: sample_serial_number out of range\n");
                    System.exit(1);
                }
            }

        bufferedReader.close();
    }

    private void do_cross_validation() {
        int i;
        int total_correct = 0;
        double total_error = 0;
        double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;
        double[] target = new double[prob.l];

        svm.svm_cross_validation(prob, param, nr_fold, target);
        if (param.svm_type == svm_parameter.EPSILON_SVR ||
                param.svm_type == svm_parameter.NU_SVR) {
            for (i = 0; i < prob.l; i++) {
                double y = prob.y[i];
                double v = target[i];
                total_error += (v - y) * (v - y);
                sumv += v;
                sumy += y;
                sumvv += v * v;
                sumyy += y * y;
                sumvy += v * y;
            }
            System.out.print("Cross Validation Mean squared error = " + total_error / prob.l + "\n");
            System.out.print("Cross Validation Squared correlation coefficient = " +
                    ((prob.l * sumvy - sumv * sumy) * (prob.l * sumvy - sumv * sumy)) /
                            ((prob.l * sumvv - sumv * sumv) * (prob.l * sumyy - sumy * sumy)) + "\n"
            );
        } else {
            for (i = 0; i < prob.l; i++)
                if (target[i] == prob.y[i])
                    ++total_correct;
            System.out.println("Total Correct: " + total_correct);
            System.out.println("Prob.l:" + prob.l);

            accuracy = (100.0 * total_correct / prob.l);
        }
    }

    public String calculateAccuracy() {

        setParameters();
        try {
            extractData();
            read_problem();
            String error_msg = svm.svm_check_parameter(prob, param);
            if (error_msg != null) {
                Log.d("SVM Errors", error_msg);
            }

            if (cross_validation != 0) {
                do_cross_validation();
            } else {
                svm_model model = svm.svm_train(prob, param);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("Result", accuracy + " ");
        return String.valueOf(accuracy);

    }
}
