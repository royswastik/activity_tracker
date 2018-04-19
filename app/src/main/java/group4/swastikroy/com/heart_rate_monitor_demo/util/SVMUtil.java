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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.Vector;


import group4.swastikroy.com.heart_rate_monitor_demo.db.DBHelper;
import group4.swastikroy.com.heart_rate_monitor_demo.model.AccelerometerDataInstance;
import group4.swastikroy.com.heart_rate_monitor_demo.model.AccelerometerDataPoint;
import group4.swastikroy.com.heart_rate_monitor_demo.model.ActionLabel;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import libsvm.*;

import static group4.swastikroy.com.heart_rate_monitor_demo.util.FeatureExtractor.peak;
import static group4.swastikroy.com.heart_rate_monitor_demo.util.FeatureExtractor.variance;

public class SVMUtil {

    static String label;
    static int count = 0;

//    private svm_parameter param;
//    private svm_problem prob;

    DBHelper database;
    Context context;
    static svm_model svmModel;

    public SVMUtil(DBHelper database, Context context) {
        this.database = database;
        this.context = context;
    }

    private double accuracy = 0.0;



    public void setHyperParameters(svm_parameter param) {

//        param = new svm_parameter();
        // default values
        param.svm_type = svm_parameter.C_SVC;
        param.kernel_type = svm_parameter.LINEAR;
        param.degree = 3;
        param.gamma = 0.02;    // 1/num_features
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

    }

    public static void load_model() throws IOException {
        svmModel = svm.svm_load_model(Constants.modelFilePath);
    }

    public static ActionLabel classifyInstance(AccelerometerDataInstance data){
        double predictedClass = svm.svm_predict(svmModel, getFeatures(data));
        ActionLabel predictedLabel = new ActionLabel();
        if(predictedClass == 1.0){
            predictedLabel.setLabel(Constants.ACTIONS.JUMP);
        }
        else if(predictedClass == 2.0){
            predictedLabel.setLabel(Constants.ACTIONS.RUN);
        }
        else if(predictedClass == 3.0){
            predictedLabel.setLabel(Constants.ACTIONS.WALK);
        }
        else if(predictedClass == 4.0){
            predictedLabel.setLabel(Constants.ACTIONS.IDLE);
        }
        else{
            predictedLabel.setLabel(Constants.ACTIONS.UNKNOWN);
        }
       return predictedLabel;
    }

    public static svm_node[] getFeatures(AccelerometerDataInstance data){

        int featureIndex = 1;
        int featureCount = 6;
        double featureVal;

        svm_node[] featureValueSetTest = new svm_node[featureCount];

        while (featureIndex < featureCount + 1) {
            svm_node featureValueTest = new svm_node();
            featureValueTest.index = featureIndex;

            switch (featureIndex) {
                case 1:
                    featureVal = variance(data.getX());
                    break;
                case 2:
                    featureVal = variance(data.getY());
                    break;
                case 3:
                    featureVal = variance(data.getZ());
                    break;
                case 4:
                    featureVal = peak(data.getX());
                    break;
                case 5:
                    featureVal = peak(data.getY());
                    break;
                case 6:
                    featureVal = peak(data.getZ());
                    break;
                default:
                    featureVal = 0.0;
                    break;

            }
            featureValueTest.value = featureVal;
            featureValueSetTest[featureIndex - 1] = featureValueTest;
            featureIndex++;
        }
        return featureValueSetTest;
    }


public static Queue<AccelerometerDataPoint> normalizeDataPoints(Queue<AccelerometerDataPoint> accelerometerDataPoints){

    Comparator<AccelerometerDataPoint> xComp = new Comparator<AccelerometerDataPoint>() {
        @Override
        public int compare(AccelerometerDataPoint lhs, AccelerometerDataPoint rhs) {
            return Float.compare(lhs.getX(), rhs.getX()) ;
        }
    };
    float maxX = Collections.max(accelerometerDataPoints, xComp).getX();
    float minX = Collections.min(accelerometerDataPoints, xComp).getX();

    Comparator<AccelerometerDataPoint> yComp = new Comparator<AccelerometerDataPoint>() {
        @Override
        public int compare(AccelerometerDataPoint lhs, AccelerometerDataPoint rhs) {
            return Float.compare(lhs.getY(), rhs.getY()) ;
        }
    };
    float maxY = Collections.max(accelerometerDataPoints, xComp).getY();
    float minY = Collections.min(accelerometerDataPoints, xComp).getY();

    Comparator<AccelerometerDataPoint> zComp = new Comparator<AccelerometerDataPoint>() {
        @Override
        public int compare(AccelerometerDataPoint lhs, AccelerometerDataPoint rhs) {
            return Float.compare(lhs.getZ(), rhs.getZ()) ;
        }
    };
    float maxZ = Collections.max(accelerometerDataPoints, xComp).getZ();
    float minZ = Collections.min(accelerometerDataPoints, xComp).getZ();

    return null;
    }


//
//
//    public void train(){}
//
//    public void test(){}
//
//    private void do_cross_validation() {}
//
////    public String calculateAccuracy(){return d0;}
//












}