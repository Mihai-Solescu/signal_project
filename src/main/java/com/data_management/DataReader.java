package com.data_management;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

public interface DataReader {
    /**
     * Reads data from a specified source and stores it in the data storage.
     * 
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    void readData(DataStorage dataStorage) throws IOException;
    void update() throws IOException;
    default int decodeData(
        Reader in, DataStorage dataStorage, int lineNumber) throws IOException{
      int charVal;
      String dataString = "";
      while ((charVal = in.read()) != -1) {
          dataString += (char) charVal;
      }
      in.close();
      //parse string to data
      String[] lines = dataString.split("\n");
      lines = Arrays.copyOfRange(lines, lineNumber, lines.length);
      for (String line : lines) {
        String[] parts = line.split(",");
        if (parts.length != 4) {
          throw new IOException("Invalid data format to many properties");
        }
        try {
          int patientId = Integer.parseInt(
              parts[0].split(":")[1].trim());
          long timestamp = Long.parseLong(
              parts[1].split(":")[1].trim());
          String Label = 
            parts[2].split(":")[1].trim();
          String DataFull = 
            parts[3].split(":")[1].trim();
          double data = 0;
          if(DataFull.contains("%")) {
            data = Double.parseDouble(DataFull.substring(0, DataFull.length()-1));
            data /= 100;
          } else {
            data = Double.parseDouble(DataFull);
          }
          dataStorage.addPatientData(patientId, data, Label, timestamp);
        } catch (NumberFormatException e) {
          throw new IOException("Invalid data format no proper number conversion possible");
        }
      }
      return lines.length;
  }
}