package com.data_management;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a patient and manages their medical records.
 * This class stores patient-specific data, allowing for the addition and
 * retrieval
 * of medical records based on specified criteria.
 */
public class Patient {
  private int patientId;
  private List<PatientRecord> patientRecords;

  /**
   * Constructs a new Patient with a specified ID.
   * Initializes an empty list of patient records.
   *
   * @param patientId the unique identifier for the patient
   */
  public Patient(int patientId) {
    this.patientId = patientId;
    this.patientRecords = new ArrayList<>();
  }

  /**
   * Adds a new record to this patient's list of medical records.
   * The record is created with the specified measurement value, record type, and
   * timestamp.
   *
   * @param measurementValue the measurement value to store in the record
   * @param recordType       the type of record, e.g., "HeartRate",
   *                         "BloodPressure"
   * @param timestamp        the time at which the measurement was taken, in
   *                         milliseconds since UNIX epoch
   */
  public void addRecord(
      double measurementValue, String recordType, long timestamp) {
    PatientRecord record = new PatientRecord(
        this.patientId, measurementValue, recordType, timestamp);
    //search for correct insertion point
    //linear search from the end of the list
    for (int i = patientRecords.size() - 1; i > -1; i--) {
      if (patientRecords.get(i).getTimestamp() <= timestamp) {
        patientRecords.add(i, record);
        return;
      }
    }
  }

  /**
   * Retrieves a list of PatientRecord objects for this patient that fall within a
   * specified time range.
   * The method filters records based on the start and end times provided.
   *
   * @param startTime the start of the time range, in milliseconds since UNIX
   *                  epoch
   * @param endTime   the end of the time range, in milliseconds since UNIX epoch
   * @return a list of PatientRecord objects that fall within the specified time
   *         range
   */
  public List<PatientRecord> getRecords(long startTime, long endTime) {
    //binary search for timestamp start time
    int current = (patientRecords.size() / 2)-1;
    while(true) {
      if (current + 1 >= patientRecords.size() || current < 0) {
        return new ArrayList<>();
      }
      PatientRecord left = patientRecords.get(current);
      PatientRecord right = patientRecords.get(current + 1);
      if (left.getTimestamp() > startTime) {
        current /= 2;
      } else if (right.getTimestamp() < startTime) {
        current += (patientRecords.size() - current) / 2;
      } else {
        break;
      }
    }
    //has to be in bound because of check above
    int start = current + 1;
    PatientRecord currentRecord = patientRecords.get(start);
    List<PatientRecord> records = new ArrayList<>();
    while(currentRecord.getTimestamp() <= endTime &&
        start < patientRecords.size()) {
      records.add(currentRecord);
      start++;
      if (start >= patientRecords.size()) {
        break;
      }
      currentRecord = patientRecords.get(start);
    }
    return records;
  }
}