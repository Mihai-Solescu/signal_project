package com.alerts.decorator;

import com.alerts.alert.Alert;

public class PriorityAlertDecorator extends AlertDecorator {
    private final Alert alert;
    private final int priority;

    public PriorityAlertDecorator(Alert alert, int priority) {
      super(alert);
      this.alert = alert;
      this.priority = priority;
    }

    @Override
    public String  getCondition() {
      return "Priority" + priority + alert.getCondition();
    }
}
