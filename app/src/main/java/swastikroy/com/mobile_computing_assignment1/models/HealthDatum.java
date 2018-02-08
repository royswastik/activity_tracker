package swastikroy.com.mobile_computing_assignment1.models;

/**
 * Created by Swastik on 1/22/2018.
 */

public class HealthDatum {
    private double time;
    private double value;
    Patient patient;

    public HealthDatum(double time, double value) {
        this.time = time;
        this.value = value;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }


}
