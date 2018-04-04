package group4.swastikroy.com.heart_rate_monitor_demo.ui;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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


    //Few parameters for the model
    public svm sModel = new svm();
    public svm_model svmModel = new svm_model();
    public svm_parameter svmParameter = new svm_parameter();
    public svm_problem svmProblem = new svm_problem();
    public int cross_validation;
    public int nr_fold = 4;
    public double accuracy;
    public FeatureExtractor feat = new FeatureExtractor();

    double trainToTotalDataRatio = 0.67;

    int totalData ;
    int trainDataLimit ;
    int testDataLimit;

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
    }

    private void addListeners() {

        // need to add a listener to train the model.
        //keep a variable which will help prevent accuracy display if run without training the model.
        holder.trainModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                train_model();
            }
        });
        // one to test the model.
        //keep a variable to use to prevent run without testing the model.
        holder.testModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test_model();
            }
        });

        holder.calcAccuracy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_Accuracy();
            }
        });

    }

    private void create_training_param(){

        List<AccelerometerDataInstance> importedDP = dbHelper.createDataPointList();

        trainToTotalDataRatio = 0.67;
        totalData = importedDP.size();
        trainDataLimit = (int) Math.floor(trainToTotalDataRatio * totalData);
        testDataLimit = totalData - trainDataLimit;

        vYTrain = new double[trainDataLimit];
        vXTrain = new svm_node[trainDataLimit][1];
        vYTest = new double[testDataLimit];
        vXTest = new svm_node[testDataLimit][1];


        svm_node[] featureValueTrain = new svm_node[trainDataLimit];
        svm_node[] featureValueTest = new svm_node[trainDataLimit];
        int arrayIndex = 0;
        for(AccelerometerDataInstance aDataInstance : importedDP) {

             Double targetValue;


                int featureIndex = 1;
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


            if (arrayIndex < trainDataLimit ) {
                featureValueTrain[arrayIndex] = new svm_node();
                featureValueTrain[arrayIndex].index = featureIndex;
                featureValueTrain[arrayIndex].value = VarianceX(aDataInstance);
                vYTrain[arrayIndex] = targetValue;
                vXTrain[arrayIndex][0] = featureValueTrain[arrayIndex];
                arrayIndex++;

            } else {

                int arrayIndex2 = arrayIndex - trainDataLimit ;

                    featureValueTest[arrayIndex2] = new svm_node();
                    featureValueTest[arrayIndex2].index = featureIndex;
                    featureValueTest[arrayIndex2].value = VarianceX(aDataInstance);
                    vYTest[arrayIndex2] = targetValue;
                    vXTest[arrayIndex2][0] = featureValueTest[arrayIndex2];

                arrayIndex++;

            }

        }


        //Create the problem
            svmProblem.y = vYTrain;
            svmProblem.x = vXTrain;
            svmProblem.l = vYTrain.length;

//
//        svm.svm_predict()
    }

    private void create_testing_param(){}

    private void create_input_features() {

        //X
        //each feature_set will be of size 60 X 1

        //in svm_node format.




        //Y - target class ...60 points one array.

        //l
        //length of training data.
    }


    private void create_classifier_model() {
    }

    private void get_Accuracy() {

        //update accuracy at the text view.
    }

    private void train_model() {

        create_training_param();

        //call the SVMUtil train and associated methods
        svmUtil.setHyperParameters(svmParameter);

        svmModel = sModel.svm_train(svmProblem, svmParameter);

        double[] targetCrossValidation = new double[60];
        svm.svm_cross_validation(svmProblem, svmParameter, nr_fold, targetCrossValidation);
    }

    private void test_model() {


        int correctCount = 0;
        int totalCount = vYTest.length;
        for(int k = 0; k <totalCount; k++){
            double predictedClass = svm.svm_predict(svmModel,vXTest[k]);
            if(vYTest[k] == predictedClass){
                correctCount++;
            }
        }

        accuracy = (correctCount/totalCount)*100;

    }

    static class ViewHolder {
        Button calcAccuracy, trainModel, testModel;
        TextView AccuracyValue;

        ViewHolder(Activity activity) {
            calcAccuracy = (Button) activity.findViewById(R.id.Calculate_accuracy);
            trainModel = (Button) activity.findViewById(R.id.train_model);
            testModel = (Button) activity.findViewById(R.id.test_model);
            AccuracyValue = (TextView) activity.findViewById(R.id.Accuracy);
        }


    }

}