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
import group4.swastikroy.com.heart_rate_monitor_demo.model.AccelerometerDataInstance;
import group4.swastikroy.com.heart_rate_monitor_demo.model.ActionLabel;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import libsvm.*;

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
        param.kernel_type = svm_parameter.POLY;
        param.degree = 3;
        param.gamma = 0.02;    // 1/num_features
        param.coef0 = 0;
        param.nu = 0.5;
        param.cache_size = 100;
        param.C = 5;
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
        else{
            predictedLabel.setLabel(Constants.ACTIONS.UNKNOWN);
        }
       return predictedLabel;
    }

    public static svm_node[] getFeatures(AccelerometerDataInstance data){


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