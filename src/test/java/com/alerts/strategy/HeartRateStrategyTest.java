package com.alerts.strategy;

import com.alerts.alert.Alert;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeartRateStrategyTest {

  @Test
  void nullHeartRateAlert() {
    Patient patient = new Patient(1);
    patient.addRecord(60, "HeartRate",  System.currentTimeMillis());
    patient.addRecord(70, "HeartRate",  System.currentTimeMillis());
    patient.addRecord(80, "HeartRate",  System.currentTimeMillis());

    HeartRateStrategy heartRateStrategy = new HeartRateStrategy();
    Alert alert = heartRateStrategy.checkAlert(patient);

    assertNull(alert);
  }

  @Test
  void highHeartRateAlert() {
    Patient patient = new Patient(1);
    patient.addRecord(60, "HeartRate",  System.currentTimeMillis() - 40);
    patient.addRecord(70, "HeartRate",  System.currentTimeMillis() - 30);
    patient.addRecord(140, "HeartRate",  System.currentTimeMillis() - 20);

    HeartRateStrategy heartRateStrategy = new HeartRateStrategy();
    Alert alert = heartRateStrategy.checkAlert(patient);

    assertNotNull(alert);
    assertEquals("HighHeartRate", alert.getCondition());
  }

}