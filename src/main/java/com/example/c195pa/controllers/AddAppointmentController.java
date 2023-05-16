package com.example.c195pa.controllers;

import com.example.c195pa.model.*;
import com.example.c195pa.utils.UTCTimeConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller class for Add Appointments screen
 *
 * @author Aimy Kohli
 */
public class AddAppointmentController implements Initializable {

    @FXML
    private TextField tfStartTime;

    @FXML
    private TextField tfEndTime;

    @FXML
    private DatePicker dpAppDate;

    @FXML
    private TextField tfAppDescription;

    @FXML
    private TextField tfAppID;

    @FXML
    private TextField tfAppTitle;

    @FXML
    private TextField tfAppLocation;

    @FXML
    private TextField tfAppType;

    @FXML
    private ComboBox<String> cbContactID;

    @FXML
    private ComboBox<String> cbCustomerID;

    @FXML
    private ComboBox<String> cbUserID;

    //Variables
    public static String startDateTimeUTCAdd; //For storing utc start time in database
    public static String endDateTimeUTCAdd; //For storing utc end time in database

    /**
     * Initializes the fields if needed to update
     * Fills in the combo boxes
     * @param url Points to any specified resource such as a file or link
     * @param resourceBundle Locale-specific data from the end user's side
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            //Fills in the combo boxes
            fillCBCustID();
            fillCBUserID();
            fillCBContact();

            int appointmentID = Appointments.getAddAppointmentID();
            tfAppID.setText(String.valueOf(appointmentID));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleDisplayESTHours(ActionEvent event) {
        AlertBox.display("Business Hours", "Please fill out this " +
                "field with the specified 12 hour format.\nExample:\n" +
                "\tValid - 1:00PM\n" +
                "\tInvalid - 13:00PM" +
                "\nOur business hours are from" +
                " 8AM-10PM Eastern Standard Time including weekends.");
    }

    /**
     * Fills the combo box for customer IDs
     */
    public void fillCBCustID(){
        ObservableList<Customers> getAllCustomers = Customers.getAllCustomersList();
        ObservableList<String> customerOL = FXCollections.observableArrayList();

        for(Customers customers: getAllCustomers){
            customerOL.add(String.valueOf(customers.getCustomerID()));
        }
        cbCustomerID.setItems(customerOL);
    }

    /**
     * Fills the combo box for customer IDs
     */
    public void fillCBUserID(){
        ObservableList<User> getAllUsers = User.getAllUsersList();
        ObservableList<String> usersOL = FXCollections.observableArrayList();

        for(User users: getAllUsers){
            usersOL.add(String.valueOf(users.getUserID()));
        }
        cbUserID.setItems(usersOL);
    }

    /**
     * Fills the combo box for contacts
     */
    public void fillCBContact(){
        ObservableList<Contacts> getAllContacts = Contacts.getAllContactsList();
        ObservableList<String> contactsOL = FXCollections.observableArrayList();

        for(Contacts contacts: getAllContacts){
            contactsOL.add(contacts.getContactID() + " - " + contacts.getContactName());
        }
        cbContactID.setItems(contactsOL);
    }

    /**
     * Save button action
     * @param event Save the new appointment
     */
    @FXML
    void handleSaveAction(ActionEvent event) throws IOException{
        try{
            int appointmentID = Integer.parseInt(tfAppID.getText());
            String appointmentTitle = tfAppTitle.getText();
            String appointmentDescription = tfAppDescription.getText();
            String appointmentLocation = tfAppLocation.getText();
            String appointmentType = tfAppType.getText();
            LocalDate date = LocalDate.parse(String.valueOf(dpAppDate.getValue()));

            DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("h:mma")
                    .toFormatter(Locale.ENGLISH);
            String startTimeValue = tfStartTime.getText();
            LocalTime startTime = LocalTime.parse(startTimeValue, timeFormatter);

            String endTimeValue = tfEndTime.getText();
            LocalTime endTime = LocalTime.parse(endTimeValue, timeFormatter);

            int customerID = Integer.parseInt(cbCustomerID.getValue());
            int userID = Integer.parseInt(cbUserID.getValue());
            String contact = cbContactID.getValue();

            LocalDateTime start = LocalDateTime.of(date, startTime);
            String appointmentStartDate = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String appointmentStartTime = startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            startDateTimeUTCAdd = UTCTimeConverter.convertToUTC(appointmentStartDate + " " + appointmentStartTime);//database utc time

            LocalDateTime end = LocalDateTime.of(date, endTime);
            String appointmentEndDate = end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String appointmentEndTime = endTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            endDateTimeUTCAdd = UTCTimeConverter.convertToUTC(appointmentEndDate + " " + appointmentEndTime);//Database utc time

            String errorMessage = new String();
            errorMessage = appointmentValidation(appointmentID,
                    start, end, customerID, errorMessage);

            String errorMessage1 = new String();
            errorMessage1 = appointmentTimeValidation(start, end, date, errorMessage1);

            if(errorMessage.length() > 0){
                AlertBox.display("Error!", errorMessage);
                errorMessage = "";
            }
            else if(errorMessage1.length() > 0){
                AlertBox.display("Error!", errorMessage1);
                errorMessage = "";
            }
            else {
                Appointments newAppointment = new Appointments(appointmentID, appointmentTitle, appointmentDescription,
                        appointmentLocation, appointmentType,
                        start, end, customerID, userID,
                        contact);

                Appointments.addAppointment(newAppointment);

                Parent root = FXMLLoader.load(getClass().getResource("/views/AppointmentsScreen.fxml"));
                Scene scene = new Scene(root);
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        }catch (Exception e){
            System.out.println(e);
            AlertBox.display("Error!", "The form contains empty fields" +
                    " or invalid data. Please make sure that each field includes the appropriate values.\n" +
                    "1. Appointment ID - valid ID - INTEGERS ONLY \n" +
                    "2. Appointment Title - valid title\n" +
                    "3. Appointment Location - valid location\n" +
                    "4. Appointment Type - valid type\n" +
                    "5. Start - proper 12 hour format\n"+
                    "6. End - proper 12 hour format\n" +
                    "7. Customer ID - valid selection\n" +
                    "8. User ID - valid selection\n" +
                    "9. Contact ID - valid selection\n");
        }
    }

    /**
     * Appointment validation
     * Checks to make sure that the start and end times are not equal to each other
     * Checks to make sure that the start time is not after the end time
     * Checks to make sure the appointment does not overlap with another
     * @param appointmentID The appointment ID
     * @param start The start datetime
     * @param end The end datetime
     * @param customerID The customer ID
     * @param eMessage The error message
     * @return The error message
     * @throws SQLException The Exception
     */
    public static String appointmentValidation(int appointmentID, LocalDateTime start, LocalDateTime end,
                                               int customerID, String eMessage ) throws SQLException {

        ObservableList<Appointments> getAllAppointments = Appointments.getAllAppointmentsList();

        //The start and end times are the same
        if(start.equals(end)) {
            eMessage += eMessage + "The start and end times are the same.\n" +
                    "Please select different times.\n";
        }
        //The start time is after the end time
        if(start.isAfter(end)) {
            eMessage += eMessage + "The start time is after the end time.\n" +
                    "Please select different times.\n";
        }

        for(Appointments appointments: getAllAppointments){
            //Times to check
            LocalDateTime checkStartTime = appointments.getStart();
            LocalDateTime checkEndTime = appointments.getEnd();

            //Customer ID is used to confirm that it is the same customer's appointments
            //Appointment ID is used to confirm that the same appointment is not being re-checked
            //Check the start and end times
            if(customerID == appointments.getCustomerID() && appointmentID != appointments.getAppointmentID()
                    && start.isBefore(checkStartTime) && end.isAfter(checkEndTime)) {
                eMessage += eMessage + "Appointment date and time is overlapping with another appointment.\n" +
                        "Please select a different date and time.\n";
            }
            if(customerID == appointments.getCustomerID() && appointmentID != appointments.getAppointmentID()
                    && start.equals(checkStartTime) && end.equals(checkEndTime)) {
                eMessage += eMessage + "Appointment date and time is overlapping with another appointment.\n" +
                        "Please select a different date and time.\n";
            }
        }

        return eMessage;
    }

    /**
     * Appointment validation for start and end times
     * Checks to make sure the start and end times are not
     * outside of business hours
     * @param start The start datetime
     * @param end The end datetime
     * @param date The appointment date
     * @param eMessage1 The error message
     * @return The error message
     * @throws SQLException The Exception
     */
    public static String appointmentTimeValidation(LocalDateTime start, LocalDateTime end,
                                                   LocalDate date, String eMessage1 ) throws SQLException {
        DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("h:mma")
                .toFormatter(Locale.ENGLISH);
        LocalTime earliestTime = LocalTime.parse("8:00AM", timeFormatter);
        LocalTime latestTime = LocalTime.parse("10:00PM", timeFormatter);

        //Checks to make sure the start time is in business hours
        ZonedDateTime localZoneDateTimeStart = ZonedDateTime.of(start, ZoneId.systemDefault());//Get the default zone date time
        ZonedDateTime estZoneDateTimeStart = localZoneDateTimeStart.withZoneSameInstant(ZoneId.of("America/New_York"));// Convert to the EST zone date time
        LocalDateTime estLocalDateTimeStart = estZoneDateTimeStart.toLocalDateTime();

        LocalDateTime startLDTCheckEarly = LocalDateTime.of(date, earliestTime);
        LocalDateTime startLDTCheckLate = LocalDateTime.of(date, latestTime);

        if (estLocalDateTimeStart.isBefore(startLDTCheckEarly)||estLocalDateTimeStart.isAfter(startLDTCheckLate)) {
            eMessage1 += eMessage1 + "Appointment start time is outside of business hours.\n" +
                    "Please select a time.";
        }

        //Checks to make sure the end time is in business hours
        ZonedDateTime localZoneDateTimeEnd = ZonedDateTime.of(end, ZoneId.systemDefault());//Get the default zone date time
        ZonedDateTime estEndZoneDateTime = localZoneDateTimeEnd.withZoneSameInstant(ZoneId.of("America/New_York"));// Convert to the EST zone date time
        LocalDateTime estEndLocalDateTime = estEndZoneDateTime.toLocalDateTime();

        LocalDateTime endLDTCheckEarly = LocalDateTime.of(date, earliestTime);
        LocalDateTime endLDTCheckLate = LocalDateTime.of(date, latestTime);

        if (estEndLocalDateTime.isBefore(endLDTCheckEarly)||estEndLocalDateTime.isAfter(endLDTCheckLate)) {
            eMessage1 += eMessage1 + "Appointment end time is outside of business hours.\n" +
                    "Please select a time.";
        }

        return eMessage1;
    }

    /**
     * Cancel button action
     * @param event Go back to the Appointments screen
     */
    @FXML
    void handleCancelAction(ActionEvent event) throws IOException {
        boolean userChoice = ConfirmBox.display("Confirm Cancellation", "Are you sure you would like to cancel " +
                "adding a new appointment?");

        if(userChoice){
            Parent root = FXMLLoader.load(getClass().getResource("/views/AppointmentsScreen.fxml"));
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        else {
            System.out.println("The user chose not to confirm the cancellation.");
        }
    }

}
