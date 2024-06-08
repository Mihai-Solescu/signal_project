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
    //format data
    //patientId, timestamp, label, data
    String[] format(String data);
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
      if (lines.length == 0) {
        return 0;
      }
      if (lines.length == 1 && lines[0].equals("")) {
        return 0;
      }
      for (String line : lines) {
        String[] parts = format(line);
        if (parts.length != 4) {
          throw new IOException("Invalid data format to many properties");
        }
        try {
          int patientId = Integer.parseInt(parts[0]);
          long timestamp = Long.parseLong(parts[1]);
          String Label = parts[2];
          String DataFull = parts[3];
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