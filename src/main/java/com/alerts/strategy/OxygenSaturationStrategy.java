package com.alerts.strategy;

import com.alerts.alert.Alert;
import com.alerts.factory.BloodOxygenAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;

public class OxygenSaturationStrategy implements AlertStrategy {
    private static final BloodOxygenAlertFactory alertFactory = new BloodOxygenAlertFactory();
    private static final double SATURATION_LEVEL_THRESHOLD = 92.0;
    private static final double SATURATION_DROP_THRESHOLD = 5.0;

    @Override
    public Alert checkAlert(Patient patient) {
      // check low saturation
      PatientRecord lastSaturationRecord = patient.getLastRecord("Saturation");
      if (lastSaturationRecord != null && lastSaturationRecord.getMeasurementValue() < SATURATION_LEVEL_THRESHOLD) {
        return alertFactory.createAlert(patient.getPatientId(), "LowSaturation", System.currentTimeMillis());
      }

      // check rapid drop //TODO: find better complexity algorithm for checking rapid drop
      List<PatientRecord> lastTenMinRecords = patient.getRecords(System.currentTimeMillis() - 10*60*1000, System.currentTimeMillis());
      for (int i = 0; i < lastTenMinRecords.size(); i++) {
        for (int j = i + 1; j < lastTenMinRecords.size(); j++)
          if (lastTenMinRecords.get(i).getMeasurementValue() - lastTenMinRecords.get(j).getMeasurementValue() > SATURATION_DROP_THRESHOLD) {
            return alertFactory.createAlert(patient.getPatientId(), "RapidSaturationDrop", System.currentTimeMillis());
          }
      }

      return null;
    }
}
