package com.cardiogenerator.generators;

import java.util.Random;
import com.cardiogenerator.outputs.OutputStrategy;

public class AlertGenerator implements PatientDataGenerator {

  public static final Random randomGenerator = new Random();
  // former non-constant variable name AlertStates violated lowerCamelCase convention
  private boolean[] alertStates; // false = resolved, true = pressed

  public AlertGenerator(int patientCount) {
    alertStates = new boolean[patientCount + 1];
  }

  @Override
  public void generate(int patientId, OutputStrategy outputStrategy) {
    try {
      if (alertStates[patientId]) {
        if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
          alertStates[patientId] = false;
          // Output the alert
          outputStrategy.output(patientId, System.currentTimeMillis(),"Alert", "resolved");
        }
      } else {
          // former non-constant variable name Lambda violated lowerCamelCase convention
          // Average rate (alerts per period), adjust based on desired frequency
          double lambda = 0.1; 
          double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
          boolean alertTriggered = randomGenerator.nextDouble() < p;

          if (alertTriggered) {
            alertStates[patientId] = true;
            // Output the alert
            outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
          }
      }
    } catch (Exception e) {
      System.err.println("An error occurred while generating alert data for patient " + patientId);
      e.printStackTrace();
    }
  }
}
