// former package name com.cardio_generator violated no underscores package naming convention
package com.cardiogenerator.outputs;

// linewrapped to 80 cause we needed a 100 but i like 80 more so wololo

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The FileOutputStrategy class writes alert data to a file.
 */
// former class name fileOutputStrategy violated UpperCamelCase naming 
// convention
public class FileOutputStrategy implements OutputStrategy {

  // former non-constant variable name BaseDirectory violated
  // lowerCamelCase convention
  private String baseDirectory;

  // former non-constant variable name file_map violated
  // lowerCamelCase convention
  public final ConcurrentHashMap<String, String> fileMap
    = new ConcurrentHashMap<>();

  /**
   * Constructor for the FileOutputStrategy class.
   * set the base directory for the file output
   * @param baseDirectory The base directory for the file output.
   */
  public FileOutputStrategy(String baseDirectory) {
    this.baseDirectory = baseDirectory;
  }

  /**
   * Append the alert data to a file.
   *
   * @param patientId The ID of the patient.
   * @param timestamp The timestamp of the alert.
   * @param label The label of the alert.
   * @param data The data of the alert.
   */
  @Override
  public void output(int patientId, long timestamp, String label, String data) {
    try {
      // Create the directory
      // added whitespace after closing parenthesis
      Files.createDirectories(Paths.get(baseDirectory) );
    } catch (IOException e) {
      System.err.println("Error creating base directory: " + e.getMessage() );
      return;
    }
    // Set the filePath variable
    // former non-constant variable name FilePath violated
    // lowerCamelCase convention
    // added whitespace after closing parenthesis
    String filePath = fileMap.computeIfAbsent(
        label, k -> Paths.get(baseDirectory, label + ".txt").toString() );

    // Write the data to the file
    try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(
        Paths.get(filePath), StandardOpenOption.CREATE,
        // added whitespace after closing parenthesis
        StandardOpenOption.APPEND) ) ) {

      out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", 
          patientId, timestamp, label, data);

    } catch (Exception e) {
      // added whitespace after closing parenthesis
      System.err.println(
          "Error writing to file " + filePath + ": " + e.getMessage() );
    }
  }
}