package com.alerts.strategy;

import com.alerts.alert.Alert;
import com.alerts.factory.AlertFactory;
import com.alerts.factory.BloodPressureAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;

public class BloodPressureStrategy implements AlertStrategy {
    private static final BloodPressureAlertFactory alertFactory = new BloodPressureAlertFactory();
    private static final int CHANGE_THRESHOLD = 10;
    private static final int HIGH_SYSTOLIC_THRESHOLD = 90;
    private static final int LOW_SYSTOLIC_THRESHOLD = 60;
    private static final int HIGH_DIASTOLIC_THRESHOLD = 180;
    private static final int LOW_DIASTOLIC_THRESHOLD = 90;

    @Override
    public Alert checkAlert(Patient patient) {
      // check trend
      List<PatientRecord> lastThreeRecords = patient.getLastRecords(3, "SystolicPressure");
      if (!(lastThreeRecords.size() < 3)) {
        if (lastThreeRecords.get(0).getMeasurementValue() - lastThreeRecords.get(1).getMeasurementValue() > CHANGE_THRESHOLD
            && lastThreeRecords.get(1).getMeasurementValue() - lastThreeRecords.get(2).getMeasurementValue() > CHANGE_THRESHOLD) {
          return alertFactory.createAlert(patient.getPatientId(), "BloodPressureIncreasingTrendAlert", lastThreeRecords.get(0).getTimestamp());
        }
        if (lastThreeRecords.get(0).getMeasurementValue() - lastThreeRecords.get(1).getMeasurementValue() < -CHANGE_THRESHOLD
            && lastThreeRecords.get(1).getMeasurementValue() - lastThreeRecords.get(2).getMeasurementValue() < -CHANGE_THRESHOLD) {
          return alertFactory.createAlert(patient.getPatientId(), "BloodPressureDecreasingTrendAlert", lastThreeRecords.get(0).getTimestamp());
        }
      }

      // check threshold
      PatientRecord lastRecord = patient.getLastRecord("SystolicPressure");
      if (lastRecord != null) {
        if (lastRecord.getMeasurementValue() > HIGH_SYSTOLIC_THRESHOLD) {
          return alertFactory.createAlert(patient.getPatientId(), "BloodPressureOverThresholdAlert",
              lastRecord.getTimestamp());
        }
        if (lastRecord.getMeasurementValue() < LOW_SYSTOLIC_THRESHOLD) {
          return alertFactory.createAlert(patient.getPatientId(),
              "BloodPressureUnderThresholdAlert", lastRecord.getTimestamp());
        }
      }


      lastRecord = patient.getLastRecord("DiastolicPressure");
      if (lastRecord != null) {
        if (lastRecord.getMeasurementValue() > HIGH_DIASTOLIC_THRESHOLD) {
          return alertFactory.createAlert(patient.getPatientId(), "BloodPressureOverThresholdAlert",
              lastRecord.getTimestamp());
        }
        if (lastRecord.getMeasurementValue() < LOW_DIASTOLIC_THRESHOLD) {
          return alertFactory.createAlert(patient.getPatientId(),
              "BloodPressureUnderThresholdAlert", lastRecord.getTimestamp());
        }
      }

      return null;
    }
}
