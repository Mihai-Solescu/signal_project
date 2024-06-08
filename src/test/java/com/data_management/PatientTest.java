package com.data_management;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

  @Test
  void getRecords() {
    Patient patient = new Patient(1);
    patient.addRecord(120, "SystolicPressure", 1000);
    patient.addRecord(130, "SystolicPressure", 2000);
    patient.addRecord(120, "SystolicPressure", 3000);
    patient.addRecord(140, "SystolicPressure", 3001);

    List<PatientRecord> recs = patient.getRecords(2000, 3000);
    assertEquals(2, recs.size());

    assertEquals(130, recs.get(0).getMeasurementValue());
    assertEquals(120, recs.get(1).getMeasurementValue());
  }

  @Test
  void getLastRecords() {
    Patient patient = new Patient(1);
    patient.addRecord(120, "SystolicPressure", 1000);
    patient.addRecord(130, "SystolicPressure", 2000);
    patient.addRecord(120, "SystolicPressure", 3000);
    patient.addRecord(140, "SystolicPressure", 4000);

    List<PatientRecord> recs = patient.getLastRecords(3, "SystolicPressure");
    assertEquals(3, recs.size());

    assertEquals(140, recs.get(0).getMeasurementValue());
    assertEquals(120, recs.get(1).getMeasurementValue());
    assertEquals(130, recs.get(2).getMeasurementValue());
  }

  @Test
  void getLastRecord() {
    Patient patient = new Patient(1);
    patient.addRecord(120, "SystolicPressure", 1000);
    patient.addRecord(130, "SystolicPressure", 2000);
    patient.addRecord(120, "SystolicPressure", 3000);
    patient.addRecord(140, "SystolicPressure", 4000);

    PatientRecord rec = patient.getLastRecord("SystolicPressure");
    assertEquals(140, rec.getMeasurementValue());
  }
}