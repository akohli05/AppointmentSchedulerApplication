package com.example.c195pa.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller class for the Home Screen
 * Opens up the Appointments, Customers,
 * and Reports screens
 *
 * @author Aimy Kohli
 */

public class HomeController {

    @FXML
    private Button btnAppointment;

    @FXML
    private Button btnCustomer;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnReports;

    @FXML
    private Label lblTitleHome;

    /**
     * Appointments button action
     * @param event Opens the Appointments Screen
     * @throws IOException The Exception
     */
    @FXML
    void handleAppointmentAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/AppointmentsScreen.fxml"));
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Customers button action
     * @param event Opens the Customers Screen
     * @throws IOException The Exception
     */
    @FXML
    void handleCustomerAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/CustomerScreen.fxml"));
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Reports button action
     * @param event Opens the Reports Screen
     * @throws IOException The Exception
     */
    @FXML
    void handleReportsAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/ReportsScreen.fxml"));
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * If the back button is clicked
     * @param event Closes the application
     */
    @FXML
    void handleExitAction(ActionEvent event) {
        boolean userChoice = ConfirmBox.display("Confirm Exit", "Are you sure you would like to exit " +
                "the application?");

        if(userChoice){
            System.exit(0);
        }
        else {
            System.out.println("The user chose not to confirm the exit.");
        }
    }

}
