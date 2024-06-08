package com.data_management;

import java.net.InetAddress;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.io.StringReader;

/**
 * TCPDataReader
 */
public class TCPDataReader implements DataReader {
  private Socket socket;
  private DataStorage dataStorage;
  String datacontent = "";
  int lineNumber = 0;
  Lock lock = new ReentrantLock();
  boolean running = true;

  public TCPDataReader(InetAddress address, int port) throws IOException {
    socket = new Socket(address, port);
    Thread thread = new Thread(this::listen);
    thread.start();
  }

  public String[] format(String data) {
    return data.split(",");
  }

  private void listen() {
    char[] buffer = new char[1024];
    try {
      InputStream in = socket.getInputStream();
      InputStreamReader reader = new InputStreamReader(in);
      while (running) {
        int read = reader.read(buffer);
        lock.lock();
        try {
          if (read > 0) {
            datacontent += new String(buffer, 0, read);
          }
        } finally {
          lock.unlock();
        }
        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public void readData(DataStorage dataStorage) throws IOException {
    lock.lock();
    try {
      decodeData(new StringReader(datacontent), dataStorage, 0);
      datacontent = "";
    } finally {
      lock.unlock();
    }
    this.dataStorage = dataStorage;
  }

  public void update() throws IOException {
    lock.lock();
    try {
      lineNumber = decodeData(
          new StringReader(datacontent), dataStorage, lineNumber);
    } finally {
      lock.unlock();
    }
  }

  public void finalize() {
    try {
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      running = false;
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }
}