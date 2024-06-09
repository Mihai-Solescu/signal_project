package com.alerts.factory;

import com.alerts.alert.Alert;
import com.alerts.alert.HeartRateAlert;

public class HeartRateAlertFactory extends AlertFactory {

    @Override
    public Alert createAlert(int patientId, String condition, long timestamp) {
        return new HeartRateAlert(patientId, condition, timestamp);
    }
}
