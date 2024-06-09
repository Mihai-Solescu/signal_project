package com.alerts.alert;

public class TriggeredAlert extends Alert {
    private String triggeredBy;

    public TriggeredAlert(int patientId, String condition, int measurementValue) {
        super(patientId, condition, measurementValue);
    }
}
