package com.alerts.decorator;

import com.alerts.alert.Alert;

public class RepeatedAlertDecorator extends AlertDecorator {
    private Alert alert;
    private int repeatCount;

    public RepeatedAlertDecorator(Alert alert, int repeatCount) {
        super(alert);
        this.repeatCount = repeatCount;
    }

    public int getRepeatCount() {
        return repeatCount;
    }
}
