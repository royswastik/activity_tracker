package swastikroy.com.mobile_computing_assignment1.models;

/**
 * Created by Swastik on 1/22/2018.
 */

public class HealthDatum {
    private double x;
    private double y;

    public HealthDatum(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
