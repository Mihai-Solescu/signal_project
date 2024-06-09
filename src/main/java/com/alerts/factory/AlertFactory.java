package com.alerts.factory;

import com.alerts.alert.Alert;

public abstract class AlertFactory {
    public abstract Alert createAlert(int patientId, String condition, long timestamp);
}
