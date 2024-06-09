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
    /**
     * Loads data since last data into data storage
     * @throws IOException if the input throws on read
     */
    void update() throws IOException;
    /**
     * format data
     * @param String to format
     * @return {patientId, timestamp, label, data}
     */
    String[] format(String data);
    /**
     * decode certain amount of data
     * @param in reader for data input
     * @param dataStorage storage to write to
     * @param lineNumber line to start reading
     * @throws if input reader throws on read
     * @return last line that has been read + 1
     */
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
          //uncomplete last line is fine since it shows that the data hasnt been fully written
          if(line.equals(lines[lines.length-1])) {
            return lines.length-1;
          }
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