package group4.swastikroy.com.heart_rate_monitor_demo.model;

import java.util.List;

import group4.swastikroy.com.heart_rate_monitor_demo.util.FeatureExtractor;

/*
 * This is each data instance while training the machine learning classifier
 */


public class AccelerometerDataInstance {
    private int id;
    private String actionType;
    private List<Float> x;  //Should be 50 for 5 seconds data with 10Hz sampling frequency
    private List<Float> y;  //Should be 50 for 5 seconds data with 10Hz sampling frequency
    private List<Float> z;  //Should be 50 for 5 seconds data with 10Hz sampling frequency





//    //Features
//    //Max FFT
//    // RMS
//    // Mean in frequency domain
//    // Power
//    double rms;

    public AccelerometerDataInstance() {

    }

    public AccelerometerDataInstance(String actionType, List<Float> x, List<Float> y, List<Float> z) {
        this.actionType = actionType;
        this.x = x;
        this.y = y;
        this.z = z;
    }

//    public void generateFeatures(){
//        this.rms = FeatureExtractor.rms(this);
//    }

    public String getActionType() {
        return actionType;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public List<Float> getX() {
        return x;
    }

    public void setX(List<Float> x) {
        this.x = x;
    }

    public List<Float> getY() {
        return y;
    }

    public void setY(List<Float> y) {
        this.y = y;
    }

    public List<Float> getZ() {
        return z;
    }

    public void setZ(List<Float> z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "AccelerometerAction{" +
                "id=" + id +
                ", actionType='" + actionType + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
