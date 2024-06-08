package com.alerts.strategy;

import com.data_management.Patient;
import com.alerts.alert.Alert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HypotensiveHypoxemiaStrategyTest {

  @Test
  void nullHypotensiveHypoxemiaAlert() {
    Patient patient = new Patient(1);
    patient.addRecord(90, "SystolicPressure", System.currentTimeMillis()-200);
    patient.addRecord(100, "SystolicPressure", System.currentTimeMillis()-190);
    patient.addRecord(80, "Saturation", System.currentTimeMillis()-180);

    HypotensiveHypoxemiaStrategy hypotensiveHypoxemiaStrategy = new HypotensiveHypoxemiaStrategy();
    Alert alert = hypotensiveHypoxemiaStrategy.checkAlert(patient);
    assertNull(alert);
  }

  @Test
  void HypotensiveHypoxemiaAlert() {
    Patient patient = new Patient(1);
    patient.addRecord(90, "SystolicPressure", System.currentTimeMillis()-200);
    patient.addRecord(100, "Saturation", System.currentTimeMillis()-190);
    patient.addRecord(50, "SystolicPressure", System.currentTimeMillis()-190);
    patient.addRecord(90, "Saturation", System.currentTimeMillis()-180);

    HypotensiveHypoxemiaStrategy hypotensiveHypoxemiaStrategy = new HypotensiveHypoxemiaStrategy();
    Alert alert = hypotensiveHypoxemiaStrategy.checkAlert(patient);
    assertNotNull(alert);
  }
}