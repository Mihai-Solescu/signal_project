package com.data_management;

import java.io.IOException;
import java.io.Reader;

public interface DataReader{
    /**
     * Reads data from a specified source and stores it in the data storage.
     * 
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    void readData(DataStorage dataStorage) throws IOException;
    default void decodeData(
        Reader in, DataStorage dataStorage) throws IOException{
    int length = in.read();
    char[] buffer = new char[length];
    in.read(buffer);
    in.close();
    String dataString = new String(buffer);
    //parse string to data
    String[] lines = dataString.split("\n");
    for (String line : lines) {
      String[] parts = line.split(",");
      if (parts.length != 4) {
        throw new IOException("Invalid data format");
      }
      try {
        int patientId = Integer.parseInt(parts[0].split(":")[1]);
        long timestamp = Long.parseLong(parts[1].split(":")[1]);
        String Label = parts[2].split(":")[1];
        String DataFull = parts[3].split(":")[1];
        double data = 0;
        if(DataFull.contains("%")) {
          data = Double.parseDouble(DataFull.substring(0, DataFull.length()-1));
          data /= 100;
        } else {
          data = Double.parseDouble(DataFull);
        }
        dataStorage.addPatientData(patientId, data, Label, timestamp);
      } catch (NumberFormatException e) {
        throw new IOException("Invalid data format");
      }
    }
  }
}