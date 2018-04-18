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


    public static double peakX(AccelerometerDataInstance dataInstance){

        double peak_val = 0.0;
        for(int i = 0; i < (dataInstance.getX().size()) ; i++){

            if(dataInstance.getX().get(i) > peak_val){

                peak_val = dataInstance.getX().get(i);

            }

        }
        return peak_val;
    }
    public static double peakY(AccelerometerDataInstance dataInstance){

        double peak_val = 0.0;
        for(int i = 0; i < (dataInstance.getY().size()) ; i++){

            if(dataInstance.getY().get(i) > peak_val){

                peak_val = dataInstance.getY().get(i);

            }

        }
        return peak_val;
    }
    public static double peakZ(AccelerometerDataInstance dataInstance){

        double peak_val = 0.0;
        for(int i = 0; i < (dataInstance.getZ().size()) ; i++){

            if(dataInstance.getZ().get(i) > peak_val){

                peak_val = dataInstance.getZ().get(i);

            }

        }
        return peak_val;
    }

    public static double meanX (AccelerometerDataInstance dataInstance){

        double mean = 0.0;

        for(int i = 0; i< (dataInstance.getX().size()); i++){

            mean = mean + dataInstance.getX().get(i);

        }

        return mean/(dataInstance.getX().size());
    }

    public static double meanY (AccelerometerDataInstance dataInstance){

        double mean = 0.0;

        for(int i = 0; i< (dataInstance.getY().size()); i++){

            mean = mean + dataInstance.getY().get(i);

        }

        return mean/(dataInstance.getY().size());
    }

    public static double meanZ (AccelerometerDataInstance dataInstance){

        double mean = 0.0;

        for(int i = 0; i< (dataInstance.getZ().size()); i++){

            mean = mean + dataInstance.getZ().get(i);

        }

        return mean/(dataInstance.getX().size());
    }

    public static double varianceX(AccelerometerDataInstance dataInstance){

        double mean = meanX(dataInstance);
        double temp = 0;

        for(int i = 0; i< (dataInstance.getX().size()); i++){

            temp = temp + Math.pow(dataInstance.getX().get(i) - mean, 2);

        }

        return temp/(dataInstance.getX().size()-1);
    }

    public static double varianceY(AccelerometerDataInstance dataInstance){

        double mean = meanX(dataInstance);
        double temp = 0;

        for(int i = 0; i< (dataInstance.getY().size()); i++){

            temp = temp + Math.pow(dataInstance.getY().get(i) - mean, 2);

        }

        return temp/(dataInstance.getY().size()-1);
    }
    public static double varianceZ(AccelerometerDataInstance dataInstance){

        double mean = meanX(dataInstance);
        double temp = 0;

        for(int i = 0; i< (dataInstance.getZ().size()); i++){

            temp = temp + Math.pow(dataInstance.getZ().get(i) - mean, 2);

        }

        return temp/(dataInstance.getZ().size()-1);
    }


// can add Mean Y, Mean Z, and Variance Y, Variance Z
    //  fft is and power will be complicated owing to unavailability of predefined api's

    public static double max_fft(AccelerometerDataInstance dataInstance){
        return 0;
    }

    public static double power(AccelerometerDataInstance dataInstance){
        return 0;
    }
}
