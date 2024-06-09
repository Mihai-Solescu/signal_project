package com.data_management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alerts.AlertGenerator;
import com.cardiogenerator.outputs.TcpOutputStrategy;
import java.net.InetAddress;
import java.io.IOException;
import java.net.URI;

/**
 * Manages storage and retrieval of patient data within a healthcare monitoring
 * system.
 * This class serves as a repository for all patient records, organized by
 * patient IDs.
 */
public class DataStorage {
    private static DataStorage dataStorage;

    private Map<Integer, Patient> patientMap; // Stores patient objects indexed by their unique patient ID.

    /**
     * Constructs a new instance of DataStorage, initializing the underlying storage
     * structure.
     */
    private DataStorage() {
        this.patientMap = new HashMap<>();
    }

    public static DataStorage getInstance() {
        if (dataStorage == null)
            dataStorage = new DataStorage();
        return dataStorage;
    }

    /**
     * Adds or updates patient data in the storage.
     * If the patient does not exist, a new Patient object is created and added to
     * the storage.
     * Otherwise, the new data is added to the existing patient's records.
     *
     * @param patientId        the unique identifier of the patient
     * @param measurementValue the value of the health metric being recorded
     * @param recordType       the type of record, e.g., "HeartRate",
     *                         "BloodPressure"
     * @param timestamp        the time at which the measurement was taken, in
     *                         milliseconds since the Unix epoch
     */
    public void addPatientData(int patientId, double measurementValue, String recordType, long timestamp) {
        Patient patient = patientMap.get(patientId);
        if (patient == null) {
            patient = new Patient(patientId);
            patientMap.put(patientId, patient);
        }
        patient.addRecord(measurementValue, recordType, timestamp);
    }

    /**
     * Retrieves a list of PatientRecord objects for a specific patient, filtered by
     * a time range.
     *
     * @param patientId the unique identifier of the patient whose records are to be
     *                  retrieved
     * @param startTime the start of the time range, in milliseconds since the Unix
     *                  epoch
     * @param endTime   the end of the time range, in milliseconds since the Unix
     *                  epoch
     * @return a list of PatientRecord objects that fall within the specified time
     *         range
     */
    public List<PatientRecord> getRecords(int patientId, long startTime, long endTime) {
        Patient patient = patientMap.get(patientId);
        if (patient != null) {
            return patient.getRecords(startTime, endTime);
        }
        return new ArrayList<>(); // return an empty list if no patient is found
    }

    /**
     * Retrieves a collection of all patients stored in the data storage.
     *
     * @return a list of all patients
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientMap.values());
    }

    /**
     * The main method for the DataStorage class.
     * Initializes the system, reads data into storage, and continuously monitors
     * and evaluates patient data.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) throws Exception{
        // DataReader is not defined in this scope, should be initialized appropriately.
        DataStorage storage = DataStorage.getInstance();
        DataReader[] readers = parseArguments(args);
        for (DataReader reader : readers) {
          reader.readData(storage);
        }

        // Initialize the AlertGenerator with the storage
        AlertGenerator alertGenerator = new AlertGenerator(storage);

        while (true) {
            for (DataReader reader : readers) {
              reader.update();
            }
            try {
                Thread.sleep(1000);
                // Evaluate all patients' data to check for conditions that may trigger alerts
                for (Patient patient : storage.getAllPatients()) {
                  alertGenerator.evaluateData(patient);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Example of using DataStorage to retrieve and print records for a patient
        List<PatientRecord> records = storage.getRecords(1, 1700000000000L, 1800000000000L);
        for (PatientRecord record : records) {
            System.out.println(
                "Record for Patient ID: " + record.getPatientId() +
                    ", Type: " + record.getRecordType() +
                    ", Data: " + record.getMeasurementValue() +
                    ", Timestamp: " + record.getTimestamp());
        }


    }

    private static DataReader[] parseArguments(String[] args) {
      ArrayList<DataReader> reader = new ArrayList<>();
      for (int i = 1; i < args.length; i++) {
       switch (args[i]) {
        case "-h":
          printHelp();
          break;
        case "--input":
          if (i + 2 < args.length) {
            switch (args[i + 1]) {
              case "tcp":
                try {
                  String[] address = args[i + 2].split(":");
                  int port = Integer.parseInt(address[1]);
                  reader.add(new TCPDataReader(
                      InetAddress.getByName(address[0]), port));
                } catch (IOException e) {
                  e.printStackTrace();
                }
                break;
              case "ws":
                try {
                  reader.add(new WebSocketDataReader(new URI(args[i + 2])));
                } catch (Exception e) {
                  e.printStackTrace();
                }
                break;
              case "file":
                reader.add(new FileDataReader(args[i + 2]));
                break;
              default:
                break;
            }
          }else{
            System.err.println("Error: Invalid input.");
            return reader.toArray(new DataReader[0]);
          }
          i += 3;
          break;
        default:
          System.err.println("Error: Invalid argument.");
          i++;
          break;
        } 
      }
      return reader.toArray(new DataReader[0]);
    }
    private static void printHelp() {
      System.out.println("Usage: DataStorage [options]");
      System.out.println("Options:");
      System.out.println("  -h\t\t\tPrint this help message");
      System.out.println("  --input <type> <source>\tSpecify the input type and source (e.g., tcp localhost:1234) this can be repeated to get multiple files for example --input tcp localhost:1234 --input file data.txt  --input file data2.txt");
    } 
}