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

  public FileDataReader(String Filename) {
    File = Filename;
  }

  @Override
  public void readData(DataStorage dataStorage) throws IOException {
    this.dataStorage = dataStorage;
    //read from file to string
    FileReader reader = new FileReader(File);
    decodeData(reader, dataStorage);
  }

  public void update() throws IOException{
    readData(dataStorage);
  }
}