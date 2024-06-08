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
    if (patientRecords.size() == 0) {
      patientRecords.add(record);
      return;
    }
    //search for correct insertion point
    //linear search from the end of the list
    for (int i = patientRecords.size() - 1; i > -1; i--) {
      if (patientRecords.get(i).getTimestamp() == timestamp && patientRecords.get(i).getRecordType().equals(recordType)) {
        return;
      }
      if (patientRecords.get(i).getTimestamp() <= timestamp) {
        patientRecords.add(i + 1, record);
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
      if(startTime < patientRecords.get(0).getTimestamp()) {
        current = 0;
        break;
      }
      if (current + 1 >= patientRecords.size() || current < 0) {
        return new ArrayList<>();
      }
      PatientRecord left = patientRecords.get(current);
      PatientRecord right = patientRecords.get(current + 1);
      if (left.getTimestamp() > startTime) {
        current /= 2;
      } else if (right.getTimestamp() < startTime) {
        current += (patientRecords.size() - current) / 2;
      } else if (right.getTimestamp() == startTime) {
        if(left.getTimestamp() != startTime) {
          current++;
        }
        break;
      }else {
        break;
      }
    }
    //has to be in bound because of check above
    int start = current;
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

  /**
   * Retrieves a list of PatientRecord objects for this patient that fall within a
   * specified time range and have a specified record type.
   * The method filters records based on the start and end times provided, as well
   * as the record type.
   *
   * @param startTime  the start of the time range, in milliseconds since UNIX
   *                   epoch
   * @param endTime    the end of the time range, in milliseconds since UNIX epoch
   * @param recordType the type of record to retrieve, e.g., "HeartRate",
   *                   "BloodPressure"
   * @return a list of PatientRecord objects that fall within the specified time
   *         range and have the specified record type
   */
  public List<PatientRecord> getRecords(long startTime, long endTime, String recordType) {
    List<PatientRecord> records = getRecords(startTime, endTime);
    List<PatientRecord> filteredRecords = new ArrayList<>();
    for (PatientRecord record : records) {
      if (record.getRecordType().equals(recordType)) {
        filteredRecords.add(record);
      }
    }
    return filteredRecords;
  }

  /**
   * Retrieves the last n PatientRecord objects for this patient.
   * The method returns the most recent n records in the patient's record list.
   *
   * @param number the number of records to retrieve
   * @return a list of the last n PatientRecord objects for this patient
   */
  public List<PatientRecord> getLastRecords(int number, String recordType) {
    List<PatientRecord> records = new ArrayList<>();
    for (int i = patientRecords.size() - 1; i > -1; i--) {
      if (patientRecords.get(i).getRecordType().equals(recordType)) {
        records.add(patientRecords.get(i));
      }
      if (records.size() == number) {
        break;
      }
    }

    List<PatientRecord> reversed = new ArrayList<>();
    for (int i = records.size() - 1; i > -1; i--) {
      reversed.add(records.get(i));
    }

    return reversed;
  }

  /**
   * Retrieves the last PatientRecord object of a specified type for this patient.
   * The method returns the most recent record of the specified type in the patient's
   * record list.
   *
   * @param recordType the type of record to retrieve, e.g., "HeartRate",
   *                   "BloodPressure"
   * @return the last PatientRecord object of the specified type for this patient
   */
  public PatientRecord getLastRecord(String recordType) {
    for (int i = patientRecords.size() - 1; i > -1; i--) {
      if (patientRecords.get(i).getRecordType().equals(recordType)) {
        return patientRecords.get(i);
      }
    }
    return null;
  }

  public int getPatientId() {
    return patientId;
  }

  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj.getClass() != this.getClass()) {
      return false;
    }
    Patient other = (Patient) obj;
    if (other.getPatientId() != this.getPatientId()) {
      return false;
    }
    for (int i = 0; i < patientRecords.size(); i++) {
      if (!patientRecords.get(i).equals(other.patientRecords.get(i))) {
        return false;
      }
    }
    return true;
  }
}