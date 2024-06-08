package com.alerts.strategy;

import com.alerts.alert.Alert;
import com.data_management.Patient;

public interface AlertStrategy {
    public Alert checkAlert(Patient patient);
}
