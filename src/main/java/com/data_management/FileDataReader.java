package com.data_management;

import java.io.IOException;
import java.io.FileReader;
import com.observer.IObserver;

/**
 * FileDataReader
 */
public class FileDataReader implements DataReader, IObserver {
  private String File = null;
  private DataStorage dataStorage = null;
  private int lineNumber = 0;

  public FileDataReader(String Filename) {
    File = Filename;
  }
  
  public String[] format(String data) {
    String[] parts = data.split(",");
    for (int i = 0; i < parts.length; i++) {
      parts[i] = parts[i].split(":")[1].trim();
    }
    return parts;
  }

  @Override
  public void readData(DataStorage dataStorage) throws IOException {
    this.dataStorage = dataStorage;
    //read from file to string
    FileReader reader = new FileReader(File);
    lineNumber = decodeData(reader, dataStorage, 0);
  }

  public void update() throws IOException{
    //assuming that the file is only appended to
    //start a new string at the last read line
    FileReader reader = new FileReader(File);
    lineNumber = decodeData(reader, dataStorage, lineNumber);
  }
}