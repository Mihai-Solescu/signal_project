package com.alerts.strategy;

import com.alerts.alert.Alert;
import com.alerts.factory.ECGAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.ArrayList;
import java.util.List;

import static com.alerts.AlertConstants.ECG_MOVING_AVERAGE_WINDOW;
import static com.alerts.AlertConstants.ECG_PEAK_THRESHOLD;
import static com.alerts.AlertConstants.SATURATION_DROP_THRESHOLD;

public class ECGStrategy implements AlertStrategy {
  private static final ECGAlertFactory alertFactory = new ECGAlertFactory();

    @Override
    public Alert checkAlert(Patient patient) {
        // check ECG peak
        List<PatientRecord> lastTenMinRecords = patient.getRecords(System.currentTimeMillis() - 10*60*1000, System.currentTimeMillis(), "ECG");
        List<PatientRecord> movingAverage;
        for (int i = 0; i < lastTenMinRecords.size(); i++) {
          movingAverage = patient.getRecords(lastTenMinRecords.get(i).getTimestamp() - ECG_MOVING_AVERAGE_WINDOW, lastTenMinRecords.get(i).getTimestamp(), "ECG");
          if (movingAverage.get(movingAverage.size()-1).getMeasurementValue() - average(movingAverage) > ECG_PEAK_THRESHOLD) {
            return alertFactory.createAlert(patient.getPatientId(), "ECGPeak", System.currentTimeMillis());
          }
        }
        return null;
    }

    private double average(List<PatientRecord> records) {
      double sum = 0;
      for (PatientRecord record : records) {
        sum += record.getMeasurementValue();
      }
      return sum / records.size();
    }
}
