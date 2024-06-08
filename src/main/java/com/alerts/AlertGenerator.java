package com.alerts;

import com.alerts.alert.Alert;
import com.alerts.strategy.AlertStrategy;
import com.alerts.strategy.BloodPressureStrategy;
import com.alerts.strategy.ECGStrategy;
import com.alerts.strategy.HypotensiveHypoxemiaStrategy;
import com.alerts.strategy.OxygenSaturationStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 * It takes a list of {@link AlertStrategy} objects that define the conditions
 * to be checked and allows for adding new strategies dynamically.
 */
public class AlertGenerator {
    private final DataStorage dataStorage;
    private final List<AlertStrategy> alertStrategies;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.alertStrategies = new ArrayList<>();
        this.alertStrategies.add(new BloodPressureStrategy());
        this.alertStrategies.add(new OxygenSaturationStrategy());
        this.alertStrategies.add(new HypotensiveHypoxemiaStrategy());
        this.alertStrategies.add(new ECGStrategy());
    }

    public AlertGenerator(DataStorage dataStorage, AlertStrategy... alertStrategies) {
        this.dataStorage = dataStorage;
        this.alertStrategies = new ArrayList<>();
        Collections.addAll(this.alertStrategies, alertStrategies);
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        for (AlertStrategy strategy : alertStrategies) {
            Alert alert = strategy.checkAlert(patient);
            if (alert != null) {
                triggerAlert(alert);
            }
        }
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        // Implementation might involve logging the alert or notifying staff
        System.out.println("ALERT: Patient " + alert.getPatientId() + " - " + alert.getCondition());
    }

    public void addAlertStrategy(AlertStrategy alertStrategy) {
        alertStrategies.add(alertStrategy);
    }
}
