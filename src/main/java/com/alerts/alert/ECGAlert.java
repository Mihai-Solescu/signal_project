package com.alerts.alert;

public class ECGAlert extends Alert {
    private double ecgValue;

    public ECGAlert(int patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
}
