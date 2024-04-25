package com.cardiogenerator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * The TcpOutputStrategy class writes alert data to a TCP socket.
 */
public class TcpOutputStrategy implements OutputStrategy {

  private ServerSocket serverSocket;
  private Socket clientSocket;
  private PrintWriter out;

  /**
   * Constructor for the TcpOutputStrategy class.
   *
   * @param port The port to listen on.
   */
  public TcpOutputStrategy(int port) {
    try {
      serverSocket = new ServerSocket(port);
      System.out.println("TCP Server started on port " + port);

      // Accept clients in a new thread to not block the main thread
      Executors.newSingleThreadExecutor().submit(() -> {
        try {
          clientSocket = serverSocket.accept();
          out = new PrintWriter(clientSocket.getOutputStream(), true);
          System.out.println("Client connected: "
              + clientSocket.getInetAddress());
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Write the alert data to the TCP socket.
   *
   * @param patientId The ID of the patient.
   * @param timestamp The timestamp of the alert.
   * @param label The label of the alert.
   * @param data The data of the alert.
   */
  @Override
  public void output(int patientId, long timestamp, String label, String data) {
    if (out != null) {
      String message = String.format(
          "%d,%d,%s,%s", patientId, timestamp, label, data);
      out.println(message);
    }
  }
}