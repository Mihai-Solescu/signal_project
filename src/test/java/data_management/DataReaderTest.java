package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.cardiogenerator.outputs.FileOutputStrategy;
import com.cardiogenerator.outputs.OutputStrategy;
import com.data_management.DataReader;
import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import com.data_management.PatientRecord;
import com.data_management.TCPDataReader;
import com.data_management.WebSocketDataReader;
import com.cardiogenerator.outputs.TcpOutputStrategy;
import com.cardiogenerator.outputs.WebSocketOutputStrategy;
import java.io.File;
import java.net.InetAddress;
import java.net.URI;
import java.util.List;
import java.util.Random;

/**
 * DataReaderTest
 */
public class DataReaderTest {
  final int patientCount = 5;
  final int DataPointCount = 10;
  final String label = "test";
  DataReader[] readers = new DataReader[3];
  DataStorage dataStorage = DataStorage.getInstance();
  OutputStrategy[] outputs = new OutputStrategy[readers.length];
  Random random = new Random();
  long currentTime = System.currentTimeMillis();
  //test the same way for all readers
  @Test
  public void testReadData() throws Exception {
    {
      File newFile = new File("output/test.txt");
      newFile.delete();
      newFile.createNewFile();
    }
    outputs[0] = new FileOutputStrategy("output/");
    outputs[1] = new TcpOutputStrategy(1234);
    outputs[2] = new WebSocketOutputStrategy(1235);
    readers[0] = new FileDataReader("output/"+label+".txt");
    readers[1] = new TCPDataReader(InetAddress.getByName("localhost"), 1234);
    readers[2] = new WebSocketDataReader(new URI("ws://localhost:1235"));
    try {
      Thread.sleep(100); 
    } catch (Exception e) {
      // TODO: handle exception
    }
    System.out.println("TestReadData started");
    generate();
    for (int i = 0; i < readers.length; i++) {
      readers[i].readData(dataStorage);
    }
    for(int count = 0; count < 10; count++) {
      generate();
      try {
        Thread.sleep(15);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      for (int i = 0; i < readers.length; i++) {
        readers[i].update();
      }
    }
    Thread.sleep(25);
    for (int i = 0; i < readers.length; i++) {
      readers[i].update();
    }
    validate();
    System.out.println("TestReadData finished");
  }

  private void generate() throws Exception{
    //generate data
    for(int i = 0; i < patientCount; i++){
      for(int j = 0; j < DataPointCount; j++){
        currentTime = random.nextLong();
        String data = random.nextDouble()*100 + "";
        for (int k = 0; k < readers.length; k++) {
          outputs[k].output(i+k*patientCount, currentTime, label, data);
        }
        dataStorage.addPatientData(i+readers.length*patientCount,
            Double.parseDouble(data), label, currentTime);
      }
    }
  }

  private void validate() {
    for (int k = 1; k < readers.length+1; k++) {
      for (int i = 0; i < patientCount; i++) {
        List<PatientRecord> r1 = dataStorage.getRecords(i, 0, currentTime);
        List<PatientRecord> r2 = dataStorage.getRecords(
            i+k*patientCount, 0, currentTime);
        assertTrue(equals(r1, r2));
        assertEquals(r1.size(), DataPointCount*11);
      }
    }
  }

  private boolean equals(List<PatientRecord> r1, List<PatientRecord> r2){
    if(r1.size() != r2.size()){
      return false;
    }
    for(int i = 0; i < r1.size(); i++){
      PatientRecord pr1 = r1.get(i);
      PatientRecord pr2 = r2.get(i);
      if(pr1.getMeasurementValue() != pr2.getMeasurementValue() ||
          !pr1.getRecordType().equals(pr2.getRecordType()) ||
          pr1.getTimestamp() != pr2.getTimestamp()){
        return false;
      }
    }
    return true;
  }
}