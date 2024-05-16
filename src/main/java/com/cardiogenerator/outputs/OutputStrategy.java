package com.cardiogenerator.outputs;

/**
 * The OutputStrategy interface defines the output method for alert data.
 */
public interface OutputStrategy {
  void output(int patientId, long timestamp, String label, String data);
}