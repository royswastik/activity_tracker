package group4.swastikroy.com.heart_rate_monitor_demo.ui;

import android.app.Activity;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import group4.swastikroy.com.heart_rate_monitor_demo.R;
import group4.swastikroy.com.heart_rate_monitor_demo.db.DBHelper;
import group4.swastikroy.com.heart_rate_monitor_demo.model.AccelerometerDataInstance;
import group4.swastikroy.com.heart_rate_monitor_demo.util.ActionBarUtil;
import group4.swastikroy.com.heart_rate_monitor_demo.util.Constants;
import group4.swastikroy.com.heart_rate_monitor_demo.util.FeatureExtractor;
import group4.swastikroy.com.heart_rate_monitor_demo.util.SVMUtil;
import libsvm.*;


import static group4.swastikroy.com.heart_rate_monitor_demo.util.FeatureExtractor.*;

public class ClassificationActivity extends AppCompatActivity {

    SVMUtil svmUtil;
    DBHelper dbHelper;
    ViewHolder holder;


//    Spinner spinner = (Spinner) findViewById(R.id.SVM_type);
//    ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.svc_type, R.layout.spinner_item);
//    spinner.setAdapter(adapter);

    //Few parameters for the model
    public svm sModel = new svm();
    public svm_model svmModel = new svm_model();
    public svm_parameter svmParameter = new svm_parameter();
    public svm_problem svmProblem = new svm_problem();
    public int cross_validation;
    public int nr_fold = 4;
    double accuracy;
//    public FeatureExtractor feat = new FeatureExtractor();

    public static String svmModelName = "SVMModelSave.txt";  //SVM Model name
    static String baseModelDir="/Android/Data/CSE535_ASSIGNMENT3";
    //Path where db to be created
    public static String MFilePath = baseModelDir+"/"+svmModelName;
    String modelFilePath = Environment.getExternalStorageDirectory() + MFilePath;

    private static final double trainToTotalDataRatio = 0.67;

    int totalData ;

    int trainDataLimit ;
    int testDataLimit;

    int featureCount;
    double[] crossValidationTarget;
    double[] vYTrain ;
    svm_node[][] vXTrain ;
    double[] vYTest ;
    svm_node[][] vXTest ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);
        holder = new ViewHolder(this);
        ActionBarUtil.setBackButton(this);
        addListeners();

        dbHelper = new DBHelper(this);
        svmUtil = new SVMUtil(dbHelper, getApplicationContext());

        //to update the text views with default svmParameter
        svmUtil.setHyperParameters(svmParameter);

        //TODO : Put a Toast Here
        holder.GammaVal.setText(Double.toString(svmParameter.gamma));
        holder.coef.setText(Double.toString(svmParameter.coef0));
        holder.costView.setText(Double.toString(svmParameter.C));
        holder.kernel_degree.setText(Double.toString(svmParameter.degree));

    }

    private void addListeners() {

        // need to add a listener to train the model.
        //keep a variable which will help prevent accuracy display if run without training the model.
        holder.trainModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                get_spinner_data();
                train_model();
            }
        });
        // one to test the model.
        //keep a variable to use to prevent run without testing the model.
        holder.testModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               accuracy = test_model();
            }
        });

        holder.calcAccuracy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_Accuracy(accuracy);
            }
        });

    }

   private void create_training_param(){

        List<AccelerometerDataInstance> importedDP = dbHelper.createDataPointList();

//        trainToTotalDataRatio = 0.67;
        totalData = importedDP.size();
        trainDataLimit = (int) Math.floor(trainToTotalDataRatio * totalData);
        testDataLimit = totalData - trainDataLimit;

        //number of feature vectors we are using for the problem

        featureCount = 6;   //
        vYTrain = new double[trainDataLimit];
        vXTrain = new svm_node[trainDataLimit][featureCount];
        vYTest = new double[testDataLimit];
        vXTest = new svm_node[testDataLimit][featureCount];

        //creating the variables for the svm problem
//        svm_node[] featureValueTrain = new svm_node[featureCount];
//        svm_node[] featureValueTest = new svm_node[featureCount];

        int arrayIndex = 0;
        for(AccelerometerDataInstance aDataInstance : importedDP) {

             Double targetValue;



                switch (aDataInstance.getActionType()) {
                    case Constants.ACTIONS.JUMP:
                        targetValue = 1.0;
                        break;
                    case Constants.ACTIONS.RUN:
                        targetValue = 2.0;
                        break;
                    case Constants.ACTIONS.WALK:
                        targetValue = 3.0;
                        break;
                    default:
                        targetValue = 0.0;
                        break;
                }

            int featureIndex ;
            double featureVal;
            if (arrayIndex < trainDataLimit ) {

                featureIndex = 1;

                while (featureIndex < featureCount+1) {
                    svm_node featureValueTrain = new svm_node();
                    featureValueTrain.index = featureIndex;
                    switch(featureIndex) {
                        case 1:
                            featureVal = variance(aDataInstance.getX());
                            break;
                        case 2:
                            featureVal = variance(aDataInstance.getY());
                            break;
                        case 3:
                            featureVal = variance(aDataInstance.getZ());
                            break;
                        case 4:
                            featureVal = peak(aDataInstance.getX());
                            break;
                        case 5:
                            featureVal = peak(aDataInstance.getY());
                            break;
                        case 6:
                            featureVal = peak(aDataInstance.getZ());
                            break;
                        default:
                            featureVal = 0.0;
                            break;

                    }
                    featureValueTrain.value = featureVal;
                    vYTrain[arrayIndex] = targetValue;
                    vXTrain[arrayIndex][featureIndex - 1] = featureValueTrain;
                    featureIndex++;
                }

                arrayIndex++;

            } else {

                int arrayIndex2 = arrayIndex - trainDataLimit ;

                if(arrayIndex2 < testDataLimit) {

                    featureIndex = 1;

                    while (featureIndex < featureCount + 1) {
                        svm_node featureValueTest = new svm_node();
                        featureValueTest.index = featureIndex;

                        switch (featureIndex) {
                            case 1:
                                featureVal = variance(aDataInstance.getX());
                                break;
                            case 2:
                                featureVal = variance(aDataInstance.getY());
                                break;
                            case 3:
                                featureVal = variance(aDataInstance.getZ());
                                break;
                            case 4:
                                featureVal = peak(aDataInstance.getX());
                                break;
                            case 5:
                                featureVal = peak(aDataInstance.getY());
                                break;
                            case 6:
                                featureVal = peak(aDataInstance.getZ());
                                break;
                            default:
                                featureVal = 0.0;
                                break;

                        }

                        featureValueTest.value = featureVal;
                        vYTest[arrayIndex2] = targetValue;
                        vXTest[arrayIndex2][featureIndex - 1] = featureValueTest;
                        featureIndex++;
                    }

                    arrayIndex++;
                }
            }

        }


        //Create the problem
            svmProblem.y = vYTrain;
            svmProblem.x = vXTrain;
            svmProblem.l = vYTrain.length;

    }

//    private void create_testing_param(){}
//
//    private void create_input_features() {
//
//        //X
//        //each feature_set will be of size 60 X 1
//
//        //in svm_node format.
//
//
//
//
//        //Y - target class ...60 points one array.
//
//        //l
//        //length of training data.
//    }


//    private void create_classifier_model() {
//    }

    private void get_Accuracy( double d) {

        //update accuracy at the text view.
        holder.AccuracyValue.setText(String.valueOf(d));

    }
    private void get_spinner_data(){

        String svm_type_input = holder.SVM_type.getSelectedItem().toString();
        String kernel_type_input = holder.kernel_type.getSelectedItem().toString();
        String cv_select_input = holder.cross_validation.getSelectedItem().toString();
        if(cv_select_input.equals("Yes")){
            cross_validation = 1;
        }else{
            cross_validation= 0;
        }

    }
    private void train_model()  {

        try {
            //create the problem with data from the collected source
            create_training_param();

            //set the HyperParameters as given as input by the user
            svmUtil.setHyperParameters(svmParameter);

            //TODO get the Hyper parameters as given as input by user, also get the crossValidation Yes/No from user and update the variable in the function

            if(cross_validation == 1){
                crossValidationTarget = new double[svmProblem.l];
                svm.svm_cross_validation(svmProblem,svmParameter,nr_fold,crossValidationTarget);
                //update the values in the textviews, they are already updated in the param values by the function above.

                //TODO get the new Hyperparameter values after cross validation and update in the display
                //TODO add a Toast There!!!
                holder.GammaVal.setText(Double.toString(svmParameter.gamma));
                holder.coef.setText(Double.toString(svmParameter.coef0));
                holder.costView.setText(Double.toString(svmParameter.C));
                holder.kernel_degree.setText(Double.toString(svmParameter.degree));

                //Now train the system again.(as the parametes are updated and saved in svmParameter)
                svmModel = svm.svm_train(svmProblem, svmParameter);




            }else{
                //cross validation = false/0

//                train the  model based on the Problem and parameters defined
                svmModel = svm.svm_train(svmProblem, svmParameter);

            }


//


            //save the model as a file

            svm.svm_save_model(modelFilePath, svmModel);



        }
        catch (IOException ioe){
            Log.v("error", "Training");
        }


    }

    private void load_model() throws IOException {
         svmModel = svm.svm_load_model(modelFilePath);

    }

    private double test_model() {
        double acc;
        if (cross_validation == 0) {

            int correctCount = 0;
            int totalCount = vYTest.length;
            for (int k = 0; k < totalCount; k++) {
                double predictedClass = svm.svm_predict(svmModel, vXTest[k]);
                if (vYTest[k] == predictedClass) {
                    correctCount++;
                }
            }

            acc = (double) correctCount * 100 / (double) totalCount;

       }else {

            int correctCount = 0;
            int totalCount = vYTest.length;
            for (int k = 0; k < totalCount; k++) {
                double predictedClass = svm.svm_predict(svmModel, vXTest[k]);
                if (vYTest[k] == predictedClass) {
                    correctCount++;
                }
            }

            acc = (double) correctCount * 100 / (double) totalCount;

//            double[] targetCrossValidation = new double[60];
//            svm.svm_cross_validation(svmProblem, svmParameter, nr_fold, targetCrossValidation);

        }

        return acc;
    }

    static class ViewHolder {
        Button calcAccuracy, trainModel, testModel;
        TextView AccuracyValue;
        Spinner SVM_type,kernel_type,cross_validation;
        TextView GammaVal,costView,coef,kernel_degree;

        ViewHolder(Activity activity) {
            calcAccuracy = (Button) activity.findViewById(R.id.Calculate_accuracy);
            trainModel = (Button) activity.findViewById(R.id.train_model);
            testModel = (Button) activity.findViewById(R.id.test_model);

            SVM_type = (Spinner) activity.findViewById(R.id.SVM_type);
            kernel_type = (Spinner) activity.findViewById(R.id.kernel_type);
            cross_validation = (Spinner) activity.findViewById(R.id.CrossValidationSelect);

            GammaVal = (TextView) activity.findViewById(R.id.GammaVal);
            costView = (TextView) activity.findViewById(R.id.costView);
            coef = (TextView) activity.findViewById(R.id.coef);
            kernel_degree = (TextView) activity.findViewById(R.id.kernel_degree);
            AccuracyValue = (TextView) activity.findViewById(R.id.Accuracy);

        }


    }

}