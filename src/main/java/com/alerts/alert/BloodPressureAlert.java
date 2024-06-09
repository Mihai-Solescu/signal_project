package com.alerts.alert;

public class BloodPressureAlert extends Alert {
    private double systolicBloodPressure;
    private double diastolicBloodPressure;

    public BloodPressureAlert(int patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
}
