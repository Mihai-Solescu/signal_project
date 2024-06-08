package com.alerts.strategy;

import com.alerts.AlertGenerator;
import com.alerts.alert.Alert;
import com.alerts.factory.BloodPressureAlertFactory;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

class BloodPressureStrategyTest {

  @Test
  void nullBloodPressureAlert() {
    Patient patient = new Patient(1);
    patient.addRecord(120, "SystolicPressure", 1000);
    patient.addRecord(130, "SystolicPressure", 2000);
    patient.addRecord(120, "SystolicPressure", 3000);
    patient.addRecord(130, "SystolicPressure", 4000);

    AlertGenerator alertGenerator = new AlertGenerator(null);
    alertGenerator.evaluateData(patient);

    BloodPressureStrategy bloodPressureAlertStrategy = new BloodPressureStrategy();
    Alert alert = bloodPressureAlertStrategy.checkAlert(patient);

    assertNull(alert);
  }

  @Test
  void bloodPressureIncreasingTrendAlert() {
    Patient patient = new Patient(1);
    patient.addRecord(120, "SystolicPressure", 1000);
    patient.addRecord(130, "SystolicPressure", 2000);
    patient.addRecord(140, "SystolicPressure", 3000);

    AlertGenerator alertGenerator = new AlertGenerator(null);
    alertGenerator.evaluateData(patient);

    BloodPressureStrategy bloodPressureAlertStrategy = new BloodPressureStrategy();
    Alert alert = bloodPressureAlertStrategy.checkAlert(patient);

    assertEquals("BloodPressureDecreasingTrendAlert", alert.getCondition());
  }
}