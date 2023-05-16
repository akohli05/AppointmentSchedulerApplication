package com.example.c195pa.controllers;

import com.example.c195pa.model.Appointments;
import com.example.c195pa.utils.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Controller class for the Appointments Screen
 *
 * @author Aimy Kohli
 */
public class AppointmentsController implements Initializable {

    @FXML
    private TableColumn<Appointments, String> colAppID;

    @FXML
    private TableColumn<Appointments, String> colTitle;

    @FXML
    private TableColumn<Appointments, String> colDescription;

    @FXML
    private TableColumn<Appointments, String> colLocation;

    @FXML
    private TableColumn<Appointments, String> colType;

    @FXML
    private TableColumn<Appointments, String> colStart;

    @FXML
    private TableColumn<Appointments, String> colEnd;

    @FXML
    private TableColumn<Appointments, String> colCustomerID;

    @FXML
    private TableColumn<Appointments, String> colUserID;

    @FXML
    private TableColumn<Appointments, String> colContactID;

    @FXML
    private TableView<Appointments> tvAppointments;

    //Variables
    private static int appointmentModifyID;

    /**
     * Returns a specific appointment ID that will be used for modifying the correct appointment
     * @return The appointment ID
     */
    public static int getAppointmentModifyID(){
        return appointmentModifyID;
    }

    /**
     * Initializes the table view
     * @param url Points to any specified resource such as a file or link
     * @param resourceBundle Locale-specific data from the end user's side
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Appointments> appointmentsOL = Appointments.getAllAppointmentsList();

            colAppID.setCellValueFactory(new PropertyValueFactory<Appointments, String>("appointmentID"));
            colTitle.setCellValueFactory(new PropertyValueFactory<Appointments, String>("appointmentTitle"));
            colDescription.setCellValueFactory(new PropertyValueFactory<Appointments, String>("appointmentDescription"));
            colLocation.setCellValueFactory(new PropertyValueFactory<Appointments, String>("appointmentLocation"));
            colContactID.setCellValueFactory(new PropertyValueFactory<Appointments, String>("contact"));
            colType.setCellValueFactory(new PropertyValueFactory<Appointments, String>("appointmentType"));
            colStart.setCellValueFactory(new PropertyValueFactory<Appointments, String>("start"));
            colEnd.setCellValueFactory(new PropertyValueFactory<Appointments, String>("end"));
            colCustomerID.setCellValueFactory(new PropertyValueFactory<Appointments, String>("customerID"));
            colUserID.setCellValueFactory(new PropertyValueFactory<Appointments, String>("userID"));

            tvAppointments.setItems(appointmentsOL);
        } catch (SQLException e) {
            System.out.println("Error - " + e);
        }
    }

    /**
     * If the 'All' radio button is selected
     * @param event Sets the table view with all appointments
     */
    @FXML
    void selectedRBAll(ActionEvent event) {
        try {
            ObservableList<Appointments> getAllAppointmentsOL = Appointments.getAllAppointmentsList();

            if (getAllAppointmentsOL != null)
                for (Appointments appointment : getAllAppointmentsOL) {
                    tvAppointments.setItems(getAllAppointmentsOL);
                }
        } catch (Exception e) {
            System.out.println("Error - " + e);
        }
    }

    /**
     * If the 'current week' radio button is selected
     * @param event Sets the table view with all appointments by the current week
     *
     * Lambda expression is used to add all appointments between the given LocalDateTimes
     * If the appointment meets the given criteria, it will be added to an observable array list
     * The observable arraylist will be used to display all appointments for the current month.
     */
    @FXML
    void selectedRBMonth(ActionEvent event) {
        try {
            //Get all appointments
            ObservableList<Appointments> getAllAppointmentsOL = Appointments.getAllAppointmentsList();
            ObservableList<Appointments> appointmentsByMonth = FXCollections.observableArrayList();

            //Get the current month start and end
            LocalDateTime curMonthStart = LocalDateTime.now().minusMonths(1);
            LocalDateTime curMonthEnd = LocalDateTime.now().plusMonths(1);

            //Makes sure the list is not null
            if (getAllAppointmentsOL != null)
                //Lambda
                getAllAppointmentsOL.forEach(appointments -> {
                    if (appointments.getStart().isAfter(curMonthStart) && appointments.getEnd().isBefore(curMonthEnd)) {
                        appointmentsByMonth.add(appointments);
                    }
                    tvAppointments.setItems(appointmentsByMonth);
                });
        } catch (Exception e) {
            System.out.println("Error - " + e);
        }
    }

    /**
     * If the 'cur week' radio button is selected
     * @param event Sets the table view with all appointments by the current week
     *
     * Lambda expression is used to add all appointments between the given LocalDateTimes
     * If the appointment meets the given criteria, it will be added to an observable array list.
     * The observable arraylist will be used to display all appointments for the current week.
     */
    @FXML
    void selectedRBWeek(ActionEvent event) {
        try {
            //Get all appointments
            ObservableList<Appointments> getAllAppointmentsOL = Appointments.getAllAppointmentsList();
            ObservableList<Appointments> appointmentsByWeek = FXCollections.observableArrayList();

            //Get the current week- start and end
            LocalDateTime curWeekStart = LocalDateTime.now().minusWeeks(1);
            LocalDateTime curWeekEnd = LocalDateTime.now().plusWeeks(1);

            //Makes sure the list is not null
            if (getAllAppointmentsOL != null)
                //Lambda for adding in appointments by the week
                getAllAppointmentsOL.forEach(appointments -> {
                    if (appointments.getStart().isAfter(curWeekStart) && appointments.getEnd().isBefore(curWeekEnd)) {
                        appointmentsByWeek.add(appointments);
                    }
                    tvAppointments.setItems(appointmentsByWeek);
                });
        } catch (Exception e) {
            System.out.println("Error - " + e);
        }
    }

    /**
     * Add Appointment button action
     * @param event Opens the add appointments screen
     */
    @FXML
    void handleAddAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/AddAppointmentScreen.fxml"));
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Update appointment button action
     * @param event Opens the update appointment screen
     */
    @FXML
    void handleUpdateAction(ActionEvent event) throws IOException {
        if(tvAppointments.getSelectionModel().getSelectedItem() == null){
            AlertBox.display("Error!", "Please select an appointment to update.");
        }
        else{
            appointmentModifyID = tvAppointments.getSelectionModel().getSelectedItem().getAppointmentID();
            Parent root = FXMLLoader.load(getClass().getResource("/views/UpdateAppointmentScreen.fxml"));
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    /**
     * Delete button action
     * @param event Deletes an appointment
     */
    @FXML
    void handleDeleteAction(ActionEvent event) throws SQLException, Exception {
        if(tvAppointments.getSelectionModel().getSelectedItem() == null){
            AlertBox.display("Error!", "Please select an appointment to delete.");
        }
        else{
            boolean userChoice = ConfirmBox.display("Confirm Deletion", "Are you sure you would like to " +
                    "delete this appointment?");

            if(userChoice){
                int appointmentID = tvAppointments.getSelectionModel().getSelectedItem().getAppointmentID();
                String appointmentType = tvAppointments.getSelectionModel().getSelectedItem().getAppointmentType();
                String query = "DELETE FROM Appointments WHERE Appointment_ID = ?";
                PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
                preparedStatement.setInt(1, appointmentID);
                preparedStatement.executeUpdate();

                //Refresh the table view
                tvAppointments.setItems(Appointments.getAllAppointmentsList());

                AlertBox.display("Delete Successful","The appointment with the following values:\n" +
                        "   ID - " + appointmentID +
                        "\n   Type - " + appointmentType
                        + "\nhas been deleted successfully.");
            }
            else {
                System.out.println("The user chose not to confirm the delete.");
            }
        }
    }

    /**
     * Back button action
     * @param event Opens the home screen
     */
    @FXML
    void handleBackAction(ActionEvent event) throws IOException {
        boolean userChoice = ConfirmBox.display("Confirm Exit", "Are you sure you would like to go " +
                "back to the home page?");

        if(userChoice){
            Parent root = FXMLLoader.load(getClass().getResource("/views/HomeScreen.fxml"));
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        else {
            System.out.println("The user chose not to confirm the exit.");
        }
    }
}
