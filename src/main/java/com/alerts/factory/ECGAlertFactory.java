package com.alerts.factory;

import com.alerts.alert.Alert;
import com.alerts.alert.ECGAlert;

public class ECGAlertFactory extends AlertFactory {
    @Override
    public Alert createAlert(int patientId, String condition, long timestamp) {
        return new ECGAlert(patientId, condition, timestamp);
    }
}
