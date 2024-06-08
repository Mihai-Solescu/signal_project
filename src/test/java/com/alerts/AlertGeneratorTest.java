package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AlertGeneratorTest {

  @Test
  void negativeBloodPressureTrendAlert() {
    Patient patient = new Patient(1);
    patient.addRecord(120, "SystolicPressure", 1000);
    patient.addRecord(130, "SystolicPressure", 2000);
    patient.addRecord(120, "SystolicPressure", 3000);
    patient.addRecord(130, "SystolicPressure", 4000);

    AlertGenerator alertGenerator = new AlertGenerator(null);
    alertGenerator.evaluateData(patient);


  }
}