package com.cardiogenerator.generators;

import java.util.Random;
import com.cardiogenerator.outputs.OutputStrategy;

// linewrapped to 80 cause we needed a 100 but i like 80 more so wololo

/**
 * The AlertGenerator class generates alert data for patients.
 */
public class AlertGenerator implements PatientDataGenerator {

  public static final Random randomGenerator = new Random();
  // former non-constant variable name AlertStates violated lowerCamelCase
  // convention
  private boolean[] alertStates; // false = resolved, true = pressed

  /**
   * Constructor for the AlertGenerator class.
   *
   * @param patientCount The number of patients.
   */
  public AlertGenerator(int patientCount) {
    alertStates = new boolean[patientCount + 1];
  }

  /**
   * Generate alert data for a patient.
   * randomaly generates alerts for the patient if the patient already has an 
   * alert, there is a 90% chance that the alert will be resolved
   * and a small chance that a new alert will be triggered
   *
   * @param patientId The ID of the patient.
   * @param outputStrategy The output strategy to use.
   */
  @Override
  public void generate(int patientId, OutputStrategy outputStrategy) {
    try {
      if (alertStates[patientId]) {
        if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
          alertStates[patientId] = false;
          // Output the alert
          outputStrategy.output(patientId, System.currentTimeMillis(), "Alert",
              "resolved");
        }
      } else {
        // former non-constant variable name
        // Lambda violated lowerCamelCase convention
        // Average rate (alerts per period), adjust based on desired frequency
        double lambda = 0.1;
        // Probability of at least one alert in the period
        double p = -Math.expm1(-lambda);
        boolean alertTriggered = randomGenerator.nextDouble() < p;

        if (alertTriggered) {
          alertStates[patientId] = true;
          // Output the alert
          outputStrategy.output(
              patientId, System.currentTimeMillis(), "Alert", "triggered");
        }
      }
    } catch (Exception e) {
      System.err.println(
          "An error occurred while generating alert data for patient "
          + patientId);

      e.printStackTrace();
    }
  }
}