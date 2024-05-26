package com.data_management;

import java.net.InetAddress;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * TCPDataReader
 */
public class TCPDataReader implements DataReader {
  private Socket socket; 

  TCPDataReader(InetAddress address, int port) throws IOException {
    socket = new Socket(address, port);
  }
  
  public void readData(DataStorage dataStorage) throws IOException {
    InputStream in = socket.getInputStream();
    InputStreamReader reader = new InputStreamReader(in);
    decodeData(reader, dataStorage);
  }

  public void finalize() {
    try {
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}