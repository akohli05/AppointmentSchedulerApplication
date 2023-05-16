package com.example.c195pa.model;

import com.example.c195pa.controllers.AddAppointmentController;
import com.example.c195pa.controllers.UpdateAppointmentController;
import com.example.c195pa.utils.JDBC;
import com.example.c195pa.utils.UTCTimeConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Model class for Appointments
 * Contains information relating to the appointments
 * @author Aimy Kohli
 */
public class Appointments {
    //Variables
    private int appointmentID;
    private String appointmentTitle, appointmentDescription,
            appointmentLocation, appointmentType;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerID, userID;
    String contact;

    /**
     * Constructor
     * @param appointmentID The appointment ID
     * @param appointmentTitle The appointment title
     * @param appointmentDescription The appointment description
     * @param appointmentLocation The appointment location
     * @param appointmentType The appointment type
     * @param start The start date time
     * @param end The end date time
     * @param customerID The customer ID
     * @param userID The user ID
     * @param contact The contact ID
     */
    public Appointments(int appointmentID, String appointmentTitle, String appointmentDescription,
                        String appointmentLocation, String appointmentType, LocalDateTime start,
                        LocalDateTime end, int customerID, int userID, String contact) {
        this.appointmentID = appointmentID;
        this.appointmentTitle = appointmentTitle;
        this.appointmentDescription = appointmentDescription;
        this.appointmentLocation = appointmentLocation;
        this.appointmentType = appointmentType;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
        this.contact = contact;
    }

    /**
     * Setter
     * @param appointmentID The appointment ID
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * Setter
     * @param appointmentTitle The appointment title
     */
    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }

    /**
     * Setter
     * @param appointmentDescription The appointment description
     */
    public void setAppointmentDescription(String appointmentDescription) {
        this.appointmentDescription = appointmentDescription;
    }

    /**
     * Setter
     * @param appointmentLocation The appointment location
     */
    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
    }

    /**
     * Setter
     * @param appointmentType The appointment type
     */
    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    /**
     * Setter
     * @param start The start date time
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * Setter
     * @param end The end date time
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**
     * Setter
     * @param customerID The customer ID
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * Setter
     * @param userID The user ID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Setter
     * @param contact The contact ID
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * Getter
     * @return The appointment ID
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * Getter
     * @return The appointment title
     */
    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    /**
     * Getter
     * @return The appointment description
     */
    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    /**
     * Getter
     * @return The appointment location
     */
    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    /**
     * Getter
     * @return The appointment type
     */
    public String getAppointmentType() {
        return appointmentType;
    }

    /**
     * Getter
     * @return The start date time
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Getter
     * @return The end date time
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Getter
     * @return The customer ID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Getter
     * @return The user ID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Getter
     * @return The contact ID
     */
    public String getContact() {
        return contact;
    }

    /**
     * Gets all the appointments
     * @return appointmentsOL An observable list of all appointments
     * @throws SQLException The Exception
     */
    public static ObservableList<Appointments> getAllAppointmentsList() throws SQLException {
        ObservableList<Appointments> appointmentsOL = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM Appointments";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int appointmentID = resultSet.getInt("Appointment_ID");
                String appointmentTitle = resultSet.getString("Title");
                String appointmentDescription = resultSet.getString("Description");
                String appointmentLocation = resultSet.getString("Location");
                String appointmentType = resultSet.getString("Type");
                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
                int customerID = resultSet.getInt("Customer_ID");
                int user_id = resultSet.getInt("User_ID");
                int contactID = resultSet.getInt("Contact_ID");
                String contact = "";

                for(Contacts contacts: Contacts.getAllContactsList()){
                    if(contacts.getContactID() == contactID){
                        contact = contacts.getContactName();
                    }
                }

                Appointments appointments = new Appointments(appointmentID, appointmentTitle, appointmentDescription,
                        appointmentLocation, appointmentType, start, end, customerID, user_id, contact);
                appointmentsOL.add(appointments);
            }
        } catch (SQLException sqle){
            System.out.println("SQL Error - " + sqle);
        } catch (Exception e){
            System.out.println("Error - " + e);
        }
            return appointmentsOL;
        }

    /**
     * Adds a new appointment
     * @param newAppointment The appointment to add
     * @throws Exception The exception
     */
    public static void addAppointment(Appointments newAppointment) throws Exception {
        int appointmentID = newAppointment.getAppointmentID();
        String appointmentTitle = newAppointment.getAppointmentTitle();
        String appointmentDescription = newAppointment.getAppointmentDescription();
        String appointmentLocation = newAppointment.getAppointmentLocation();
        String appointmentType = newAppointment.getAppointmentType();
        LocalDateTime start = newAppointment.getStart();
        LocalDateTime end = newAppointment.getEnd();
        String createdUpdatedBy = "ADMIN";
        int customerID = newAppointment.getCustomerID();
        int userID = newAppointment.getUserID();
        String contact = newAppointment.getContact();
        int contactID = Integer.parseInt(contact.substring(0, 1)); //get the contact id

        for(User users: User.getAllUsersList()){
                if(userID == users.getUserID()){
                    createdUpdatedBy = users.getUserName();
                }
            }

       String query = "INSERT INTO appointments (Appointment_ID, Title, Description, Location,"+
               "Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID,User_ID, Contact_ID)"+
                "VALUES (?, ?, ?,  ?, ?, ?, ?, ?, ?, ?, ?,  ?,  ?, ? )";
       PreparedStatement preparedStatement1 = JDBC.connection.prepareStatement(query);

       preparedStatement1.setInt(1, appointmentID);
       preparedStatement1.setString(2, appointmentTitle);
       preparedStatement1.setString(3, appointmentDescription);
       preparedStatement1.setString(4, appointmentLocation);
       preparedStatement1.setString(5, appointmentType);
       preparedStatement1.setTimestamp(6, Timestamp.valueOf(start));
       preparedStatement1.setTimestamp(7, Timestamp.valueOf(end));
       preparedStatement1.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
       preparedStatement1.setString(9, createdUpdatedBy);
       preparedStatement1.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
       preparedStatement1.setString(11, createdUpdatedBy);
       preparedStatement1.setInt(12, customerID);
       preparedStatement1.setInt(13, userID);
       preparedStatement1.setInt(14, contactID);

       preparedStatement1.executeUpdate();
        }

    /**
     * Updates an appointment
     * @param editAppointment The appointment to update
     * @throws Exception The exception
     */
    public static void updateAppointment(Appointments editAppointment) throws Exception {
        int appointmentID = editAppointment.getAppointmentID();
        String appointmentTitle = editAppointment.getAppointmentTitle();
        String appointmentDescription = editAppointment.getAppointmentDescription();
        String appointmentLocation = editAppointment.getAppointmentLocation();
        String appointmentType = editAppointment.getAppointmentType();
        LocalDateTime start = editAppointment.getStart();
        LocalDateTime end = editAppointment.getEnd();
        String createdUpdatedBy = "ADMIN";
        int customerID = editAppointment.getCustomerID();
        int userID = editAppointment.getUserID();
        String contact = editAppointment.getContact();
        int contactID = Integer.parseInt(contact.substring(0, 1)); //get the contact id

        for(User users: User.getAllUsersList()){
            if(userID == users.getUserID()){
                createdUpdatedBy = users.getUserName();
            }
        }

        String query = "UPDATE appointments SET Title = ?, Description = ?, Location" +
                " = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, " +
                "User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement preparedStatement1 = JDBC.connection.prepareStatement(query);

        preparedStatement1.setString(1, appointmentTitle);
        preparedStatement1.setString(2, appointmentDescription);
        preparedStatement1.setString(3, appointmentLocation);
        preparedStatement1.setString(4, appointmentType);
        preparedStatement1.setTimestamp(5, Timestamp.valueOf(start));
        preparedStatement1.setTimestamp(6, Timestamp.valueOf(end));
        preparedStatement1.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        preparedStatement1.setString(8, createdUpdatedBy);
        preparedStatement1.setInt(9, customerID);
        preparedStatement1.setInt(10, userID);
        preparedStatement1.setInt(11, contactID);
        preparedStatement1.setInt(12, appointmentID);

        preparedStatement1.executeUpdate();
    }

    /**
     * Gets an autogenerated appointment ID for adding an appointment
     * @return The new autogenerated appointment ID
     */
    public static int getAddAppointmentID() throws SQLException {
        int appointmentID = 0;
        int newAppointmentID;

        String query = "SELECT MAX(Appointment_ID) FROM appointments";
        PreparedStatement preparedStatement1 = JDBC.connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement1.executeQuery();

        while (resultSet.next()) {
            appointmentID = resultSet.getInt("MAX(Appointment_ID)");
        }

        newAppointmentID = appointmentID+1;
        return newAppointmentID;
    }

}
