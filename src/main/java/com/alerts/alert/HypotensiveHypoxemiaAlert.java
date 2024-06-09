package com.alerts.alert;

public class HypotensiveHypoxemiaAlert extends Alert {
    private double systolicBloodPressure;
    private double oxygenSaturation;

    public HypotensiveHypoxemiaAlert(int patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
}
