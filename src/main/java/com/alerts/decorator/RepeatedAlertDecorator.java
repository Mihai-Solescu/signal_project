package com.alerts.decorator;

import com.alerts.alert.Alert;

public class RepeatedAlertDecorator extends AlertDecorator {
    private final Alert alert;
    private final int repeatCount;

    public RepeatedAlertDecorator(Alert alert, int repeatCount) {
        super(alert);
        this.alert = alert;
        this.repeatCount = repeatCount;
    }

    @Override
    public String getCondition() {
        return repeatCount + "x" + alert.getCondition();
    }
}
