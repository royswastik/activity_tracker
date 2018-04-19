package group4.swastikroy.com.heart_rate_monitor_demo.model;

import java.util.List;

/*
 * This is each data instance while training the machine learning classifier
 */


public class ActionLabel {
   String label;
    Double confidenceScore;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(Double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }
}
