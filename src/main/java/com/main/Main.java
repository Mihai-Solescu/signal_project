package com.main;

import com.cardiogenerator.HealthDataSimulator;
import com.data_management.DataStorage;

public class Main {
  public static void main(String[] args) throws Exception{
    if (args.length > 0 && args[0].equals("DataStorage")) {
      DataStorage.main(new String[]{});
    } else {
      try {
        HealthDataSimulator.getInstance(args);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}