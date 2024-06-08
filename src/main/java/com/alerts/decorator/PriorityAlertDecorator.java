package com.alerts.decorator;

import com.alerts.alert.Alert;

public class PriorityAlertDecorator extends AlertDecorator {
    private Alert alert;
    private int priority;

    public PriorityAlertDecorator(Alert alert, int priority) {
        super(alert);
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
