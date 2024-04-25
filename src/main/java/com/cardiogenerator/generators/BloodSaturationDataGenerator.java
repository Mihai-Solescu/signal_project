package com.cardiogenerator.generators;

import java.util.Random;

import com.cardiogenerator.outputs.OutputStrategy;

/**
 * The BloodSaturationDataGenerator class generates
 * blood saturation data for patients.
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {
  private static final Random random = new Random();
  private int[] lastSaturationValues;

  /**
   * Constructor for the BloodSaturationDataGenerator class.
   *
   * @param patientCount The number of patients.
   */
  public BloodSaturationDataGenerator(int patientCount) {
    lastSaturationValues = new int[patientCount + 1];

    // Initialize with baseline saturation values for each patient
    for (int i = 1; i <= patientCount; i++) {
      // Initializes with a value between 95 and 100
      lastSaturationValues[i] = 95 + random.nextInt(6);
    }
  }

  /**
   * Generate blood saturation data for a patient.
   *
   * @param patientId The ID of the patient.
   * @param outputStrategy The output strategy to use.
   */
  @Override
  public void generate(int patientId, OutputStrategy outputStrategy) {
    try {
      // Simulate blood saturation values
      // -1, 0, or 1 to simulate small fluctuations
      int variation = random.nextInt(3) - 1;
      int newSaturationValue = lastSaturationValues[patientId] + variation;

      // Ensure the saturation stays within a realistic and healthy range
      newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
      lastSaturationValues[patientId] = newSaturationValue;
      outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
              Double.toString(newSaturationValue) + "%");
    } catch (Exception e) {
      System.err.println("An error occurred while generating blood" +
          " saturation data for patient " + patientId);
      // This will print the stack trace to help identify where
      // the error occurred.
      e.printStackTrace();
    }
  }
}