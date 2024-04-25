// former package name com.cardio_generator violated no underscores package naming convention
package com.cardiogenerator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

// former class name fileOutputStrategy violated UpperCamelCase naming convention
public class FileOutputStrategy implements OutputStrategy {

  // former non-constant variable name BaseDirectory violated lowerCamelCase convention
  private String baseDirectory;

  // former non-constant variable name file_map violated lowerCamelCase convention
  public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

  public FileOutputStrategy(String baseDirectory) {
    this.baseDirectory = baseDirectory;
  }

  @Override
  public void output(int patientId, long timestamp, String label, String data) {
    try {
      // Create the directory
      Files.createDirectories(Paths.get(baseDirectory) );
    } catch (IOException e) {
      System.err.println("Error creating base directory: " + e.getMessage() );
      return;
    }
    // Set the filePath variable
    // former non-constant variable name FilePath violated lowerCamelCase convention
    String filePath = fileMap.computeIfAbsent(
        label, k -> Paths.get(baseDirectory, label + ".txt").toString() );

    // Write the data to the file
    try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(
        Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND) ) ) {

      out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", 
          patientId, timestamp, label, data);

    } catch (Exception e) {
      System.err.println("Error writing to file " + filePath + ": " + e.getMessage() );
    }
  }
}
