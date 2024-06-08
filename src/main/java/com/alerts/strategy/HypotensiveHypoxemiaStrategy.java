package com.alerts.strategy;

import com.alerts.alert.Alert;
import com.alerts.factory.HypotensiveHypoxemiaAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import static com.alerts.AlertConstants.LOW_SYSTOLIC_THRESHOLD;
import static com.alerts.AlertConstants.SATURATION_LEVEL_THRESHOLD;

public class HypotensiveHypoxemiaStrategy implements AlertStrategy {
    private static final HypotensiveHypoxemiaAlertFactory alertFactory = new HypotensiveHypoxemiaAlertFactory();

    @Override
    public Alert checkAlert(Patient patient) {
      boolean hypotensive = false;
      boolean hypoxemic = false;

      // check low systolic pressure
      PatientRecord lastRecord = patient.getLastRecord("SystolicPressure");
      if (lastRecord != null) {
        if (lastRecord.getMeasurementValue() < LOW_SYSTOLIC_THRESHOLD) {
          hypotensive = true;
        }
      }

      // check low saturation
      PatientRecord lastSaturationRecord = patient.getLastRecord("Saturation");
      if (lastSaturationRecord != null && lastSaturationRecord.getMeasurementValue() < SATURATION_LEVEL_THRESHOLD) {
        hypoxemic = true;
      }

      if (hypotensive && hypoxemic){
        return alertFactory.createAlert(patient.getPatientId(), "HypotensiveHypoxemia", System.currentTimeMillis());
      }
      return null;
    }
}
