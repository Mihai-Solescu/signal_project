package com.alerts.alert;

public class HeartRateAlert extends Alert {
    private double heartRate;

    public HeartRateAlert(int patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }

    public double getHeartRate() {
        return heartRate;
    }
}
