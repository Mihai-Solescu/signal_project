package com.alerts.strategy;

import com.alerts.alert.Alert;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OxygenSaturationStrategyTest {

  @Test
  void nullOxygenSaturationAlert() {
    Patient patient = new Patient(1);
    patient.addRecord(93, "Saturation", 1000);
    patient.addRecord(94, "Saturation", 2000);

    OxygenSaturationStrategy oxygenSaturationStrategy = new OxygenSaturationStrategy();
    assertNull(oxygenSaturationStrategy.checkAlert(patient));
  }

  @Test
  void lowOxygenSaturationAlert() {
    Patient patient = new Patient(1);
    patient.addRecord(92, "Saturation", 1000);
    patient.addRecord(91, "Saturation", 2000);

    OxygenSaturationStrategy oxygenSaturationStrategy = new OxygenSaturationStrategy();
    Alert alert = oxygenSaturationStrategy.checkAlert(patient);
    assertEquals("LowSaturation", alert.getCondition());
  }

  @Test
  void falseRapidSaturationDropAlert() {
    Patient patient = new Patient(1);
    patient.addRecord(92, "Saturation", System.currentTimeMillis() - 5000);
    patient.addRecord(94, "Saturation", System.currentTimeMillis() - 4000);
    patient.addRecord(95, "Saturation", System.currentTimeMillis() - 3000);
    patient.addRecord(96, "Saturation", System.currentTimeMillis() - 2000);
    patient.addRecord(97, "Saturation", System.currentTimeMillis() - 1000);

    OxygenSaturationStrategy oxygenSaturationStrategy = new OxygenSaturationStrategy();
    assertNull(oxygenSaturationStrategy.checkAlert(patient));
  }

  @Test
  void rapidSaturationDropAlert() {
    Patient patient = new Patient(1);
    patient.addRecord(98, "Saturation", System.currentTimeMillis() - 5000);
    patient.addRecord(94, "Saturation", System.currentTimeMillis() - 4000);
    patient.addRecord(95, "Saturation", System.currentTimeMillis() - 3000);
    patient.addRecord(96, "Saturation", System.currentTimeMillis() - 2000);
    patient.addRecord(92, "Saturation", System.currentTimeMillis() - 1000);

    OxygenSaturationStrategy oxygenSaturationStrategy = new OxygenSaturationStrategy();
    Alert alert = oxygenSaturationStrategy.checkAlert(patient);
    assertEquals("RapidSaturationDrop", alert.getCondition());
  }
}