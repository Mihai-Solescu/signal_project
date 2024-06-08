package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import com.data_management.PatientRecord;
import java.util.List;

/**
 * FileDataReader
 */
class FileDataReaderTest {
  @Test
  public void testReadData() throws Exception {
    FileDataReader fileDataReader = new FileDataReader("output/test.txt");
    DataStorage dataStorage = new DataStorage();
    fileDataReader.readData(dataStorage);
    List<PatientRecord> record = dataStorage.getRecords(52, 0, 1712822479140l);
    assertEquals(7, record.size());
    record = dataStorage.getRecords(70, 1712822468144L, 1712822468144L);
    assertEquals(1, record.size());
  }
}