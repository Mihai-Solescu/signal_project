package com.alerts.factory;

import com.alerts.alert.Alert;
import com.alerts.alert.BloodPressureAlert;

public class BloodPressureAlertFactory extends AlertFactory {
    @Override
    public Alert createAlert(int patientId, String condition, long timestamp) {
        return new BloodPressureAlert(patientId, condition, timestamp);
    }
}
