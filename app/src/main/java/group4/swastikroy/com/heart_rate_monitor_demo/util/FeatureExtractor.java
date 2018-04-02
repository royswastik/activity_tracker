package group4.swastikroy.com.heart_rate_monitor_demo.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Size;
import android.util.SizeF;

import group4.swastikroy.com.heart_rate_monitor_demo.model.AccelerometerDataInstance;

/**
 * Created by sroy41 on 3/29/2018.
 */

public class FeatureExtractor {
    public static double rms(AccelerometerDataInstance dataInstance){

        double rms_val = 0.0;
        for(int i = 0; i < (dataInstance.getX().size()) ; i++){


            rms_val = rms_val + Math.sqrt((Math.pow(dataInstance.getX().get(i),2)+ Math.pow(dataInstance.getY().get(i),2) + Math.pow(dataInstance.getZ().get(i),2))/3);

            rms_val = rms_val/50; //normalise
        }
        return rms_val;
    }

    public static double meanX (AccelerometerDataInstance dataInstance){

        double mean = 0.0;

        for(int i = 0; i< (dataInstance.getX().size()); i++){

            mean = mean + dataInstance.getX().get(i);

        }

        return mean/(dataInstance.getX().size());
    }

    public static double VarianceX(AccelerometerDataInstance dataInstance){

        double mean = meanX(dataInstance);
        double temp = 0;

        for(int i = 0; i< (dataInstance.getX().size()); i++){

            temp = temp + Math.pow(dataInstance.getX().get(i) - mean, 2);

        }

        return temp/(dataInstance.getX().size()-1);
    }


// can add Mean Y, Mean Z, and Variance Y, Variance Z

    public static double max_fft(AccelerometerDataInstance dataInstance){
        return 0;
    }

    public static double power(AccelerometerDataInstance dataInstance){
        return 0;
    }
}
