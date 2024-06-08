package com.alerts.decorator;

import com.alerts.alert.Alert;

public abstract class AlertDecorator extends Alert {
    protected Alert alert;

    public AlertDecorator(Alert alert) {
        super(alert.getPatientId(), alert.getCondition(), alert.getTimestamp());
        this.alert = alert;
    }
}
