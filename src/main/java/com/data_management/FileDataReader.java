package com.data_management;

import java.io.IOException;
import java.io.FileReader;

/**
 * FileDataReader
 */
public class FileDataReader implements DataReader {
  private String File = null;
  private DataStorage dataStorage = null;

  FileDataReader(String Filename) {
    File = Filename;
  }

  @Override
  public void readData(DataStorage dataStorage) throws IOException {
    this.dataStorage = dataStorage;
    //read from file to string
    FileReader reader = new FileReader(File);
    int length = reader.read();
    char[] buffer = new char[length];
    reader.read(buffer);
    reader.close();
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