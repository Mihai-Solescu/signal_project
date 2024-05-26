package com.data_management;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import com.cardiogenerator.outputs.WebSocketOutputStrategy;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.server.WebSocketServer;

public class WebSocketDataReader extends WebSocketClient implements DataReader {

  public WebSocketDataReader(URI serverUri, Draft draft) {
    super(serverUri, draft);
  }

  public WebSocketDataReader(URI serverUri) {
    super(serverUri);
  }

  @Override
  public void readData(DataStorage dataStorage) {
    connect();
  }

  @Override
  public void onOpen(ServerHandshake handshakedata) {
    send("Hello, it is me. Mario :)");
    System.out.println("new connection opened");
  }

  @Override
  public void onClose(int code, String reason, boolean remote) {
    System.out.println("closed with exit code " + code + " additional info: " + reason);
  }

  @Override
  public void onMessage(String message) {
    System.out.println("received message: " + message);
  }

  @Override
  public void onMessage(ByteBuffer message) {
    System.out.println("received ByteBuffer");
  }

  @Override
  public void onError(Exception ex) {
    System.err.println("an error occurred:" + ex);
  }

  public static void main(String[] args) throws URISyntaxException {
    var out = new WebSocketOutputStrategy(8887);

    WebSocketClient client = new WebSocketDataReader(new URI("ws://localhost:8887"));
    client.connect();
  }
}
