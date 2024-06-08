package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.cardiogenerator.generators.BloodLevelsDataGenerator;
import com.cardiogenerator.outputs.FileOutputStrategy;
import com.cardiogenerator.outputs.OutputStrategy;
import com.data_management.DataReader;
import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import com.data_management.Patient;
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
  DataStorage[] dataStorage = new DataStorage[readers.length];
  DataStorage reference = new DataStorage();
  OutputStrategy[] outputs = new OutputStrategy[readers.length];
  Random random = new Random();
  double[][] data = new double[patientCount][DataPointCount];
  long[][] timestamps = new long[patientCount][1+DataPointCount/5];
  long currentTime = System.currentTimeMillis();
  //test the same way for all readers
  @Test
  public void testReadData() throws Exception {
    for (int i = 0; i < dataStorage.length; i++) {
      dataStorage[i] = new DataStorage();
    }
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
    readers[2] = new WebSocketDataReader(new URI("localhost:1235"));
    System.out.println("TestReadData started");
    generate();
    for (int i = 0; i < readers.length; i++) {
      readers[i].readData(dataStorage[i]);
    }
    for (int i = 0; i < readers.length; i++) {
      validate(dataStorage[i]);
    }
    for(int count = 0; count < 10; count++) {
      for (int i = 0; i < readers.length; i++) {
        readers[i].update();
      }
      for (int i = 0; i < readers.length; i++) {
        for(int j = 0; j < readers.length; j++) {
          if(i == j) {
            continue;
          }
          if(readers[i].equals(readers[j])) {
            continue;
          }
          throw new Exception("Readers are not equal");
        }
      }
    }

  }

  private void generate() throws Exception{
    //generate data
    data = new double[patientCount][DataPointCount];

    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[i].length; j++) {
        data[i][j] = random.nextDouble() * 100;
        currentTime += 10 * random.nextInt(10);
        if(j % 5 == 0) {
          timestamps[i][j/5] = currentTime;
        }
        reference.addPatientData(
            i, data[i][j], label, currentTime);
      }
      timestamps[i][timestamps[i].length-1] = Long.MAX_VALUE;
      currentTime = System.currentTimeMillis();
    }

    for(int i = 0; i < readers.length; i++) {
      for (int j = 0; j < data.length; j++) {
        for (int k = 0; k < data[j].length; k++) {
          outputs[i].output(j, timestamps[j][k/5]+k%5,
              label, Double.toString(data[j][k]));
        }
      }
    }
  }

  private void validate(DataStorage storage){
    for (int i = 0; i < data.length; i++) {
      //segment data into different timeframes
      for(int index = 0; index < timestamps[i].length; index++) {
        List<PatientRecord> record = storage.getRecords(
            i, timestamps[i][index], timestamps[i][index+1]-1);
        validateRecord(record, i, index);
      }
    }
  }

  private void validateRecord(List<PatientRecord> record, int patientIndex,
      int timeIndex){
    int dataIndex = timeIndex * 5;
    for (int i = 0; i < record.size(); i++) {
      assertEquals(timestamps[patientIndex][timeIndex]+i,
          record.get(i).getTimestamp());
      assertEquals(data[patientIndex][dataIndex+i],
          record.get(i).getMeasurementValue());
      dataIndex++;
    } 
  }
}