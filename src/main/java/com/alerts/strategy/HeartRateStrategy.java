package com.alerts.strategy;

import com.alerts.alert.Alert;
import com.alerts.factory.HeartRateAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import static com.alerts.AlertConstants.HIGH_HEART_RATE_THRESHOLD;

public class HeartRateStrategy implements AlertStrategy {
    private static final HeartRateAlertFactory alertFactory = new HeartRateAlertFactory();

    @Override
    public Alert checkAlert(Patient patient) {
        PatientRecord lastHeartRateRecord = patient.getLastRecord("HeartRate");
        if (lastHeartRateRecord != null && lastHeartRateRecord.getMeasurementValue() > HIGH_HEART_RATE_THRESHOLD) {
            return alertFactory.createAlert(patient.getPatientId(), "HighHeartRate", System.currentTimeMillis());
        }
        return null;
    }
}
