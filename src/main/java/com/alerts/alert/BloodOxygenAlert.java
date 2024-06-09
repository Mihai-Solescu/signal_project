package com.alerts.alert;

public class BloodOxygenAlert extends Alert {
    private double bloodOxygenLevel;

    public BloodOxygenAlert(int patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
}
