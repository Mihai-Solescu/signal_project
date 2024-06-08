package com.alerts.strategy;

import com.alerts.alert.Alert;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ECGStrategyTest {

  @Test
  void nullECGAlert() {
    Patient patient = new Patient(1);
    patient.addRecord(20, "ECG", System.currentTimeMillis()-200);
    patient.addRecord(30, "ECG", System.currentTimeMillis()-190);
    patient.addRecord(40, "ECG", System.currentTimeMillis()-180);
    patient.addRecord(50, "ECG", System.currentTimeMillis()-170);
    patient.addRecord(40, "ECG", System.currentTimeMillis()-160);
    patient.addRecord(10, "ECG", System.currentTimeMillis()-150);
    patient.addRecord(20, "ECG", System.currentTimeMillis()-140);
    patient.addRecord(40, "ECG", System.currentTimeMillis()-130);
    patient.addRecord(40, "ECG", System.currentTimeMillis()-120);
    patient.addRecord(50, "ECG", System.currentTimeMillis()-110);
    patient.addRecord(30, "ECG", System.currentTimeMillis()-100);
    patient.addRecord(40, "ECG", System.currentTimeMillis()-90);

    ECGStrategy ecgStrategy = new ECGStrategy();
    Alert alert = ecgStrategy.checkAlert(patient);
    assertNull(alert);
  }

  @Test
  void ECGAlert() {
    Patient patient = new Patient(1);
    patient.addRecord(20, "ECG", System.currentTimeMillis()-200);
    patient.addRecord(30, "ECG", System.currentTimeMillis()-190);
    patient.addRecord(40, "ECG", System.currentTimeMillis()-180);
    patient.addRecord(50, "ECG", System.currentTimeMillis()-170);
    patient.addRecord(40, "ECG", System.currentTimeMillis()-160);
    patient.addRecord(10, "ECG", System.currentTimeMillis()-150);
    patient.addRecord(20, "ECG", System.currentTimeMillis()-140);
    patient.addRecord(40, "ECG", System.currentTimeMillis()-130);
    patient.addRecord(40, "ECG", System.currentTimeMillis()-120);
    patient.addRecord(50, "ECG", System.currentTimeMillis()-110);
    patient.addRecord(30, "ECG", System.currentTimeMillis()-100);
    patient.addRecord(180, "ECG", System.currentTimeMillis()-90);
    patient.addRecord(110, "ECG", System.currentTimeMillis()-80);

    ECGStrategy ecgStrategy = new ECGStrategy();
    Alert alert = ecgStrategy.checkAlert(patient);
    assertNotNull(alert);
  }
}