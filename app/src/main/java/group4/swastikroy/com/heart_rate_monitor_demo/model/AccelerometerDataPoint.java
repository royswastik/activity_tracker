package group4.swastikroy.com.heart_rate_monitor_demo.model;

public class AccelerometerDataPoint {
    private float x;
    private float y;
    private float z;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public AccelerometerDataPoint() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public AccelerometerDataPoint(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return x + "," + y + "," + z + ";";
    }

}
