package com.example.c195pa.controllers;

import com.example.c195pa.model.Appointments;
import com.example.c195pa.model.Contacts;
import com.example.c195pa.model.User;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *  Controller class for the Login Screen
 *  Validate user login info, write login logs to txt file,
 *  Display alerts for appointments with start times for
 *  the next 15 minutes
 *
 * @author Aimy Kohli
 */
public class LoginController implements Initializable {
    @FXML
    private Button btnExit;

    @FXML
    private Button btnLogin;

    @FXML
    private TextField tfPassword;

    @FXML
    private TextField tfUserID;

    @FXML
    private Label lblPassword;

    @FXML
    private Label lblTitleLogin;

    @FXML
    private Label lblUserID;

    @FXML
    private Label lblActualLocation;

    @FXML
    private Label lblLocationTextOnly;

    //Variables
    ObservableList<Appointments> appointmentsReminderOL = FXCollections.observableArrayList();
    ZoneId zoneId= ZoneId.systemDefault();
    public static String usernameCreatedUpdatedBy;

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        try{
            Locale locale = Locale.getDefault();
            Locale.setDefault(locale);

            lblActualLocation.setText(String.valueOf(zoneId));

            resourceBundle = ResourceBundle.getBundle("language_property/login", Locale.getDefault());
            if(Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")) {
               lblTitleLogin.setText(resourceBundle.getString("title"));
               lblUserID.setText(resourceBundle.getString("userid"));
               lblPassword.setText(resourceBundle.getString("password"));
               lblLocationTextOnly.setText(resourceBundle.getString("location"));
               btnLogin.setText(resourceBundle.getString("signin"));
               btnExit.setText(resourceBundle.getString("exit"));
           }
        }catch (MissingResourceException e){
            System.out.println("Error! Resource file missing: " + e);
        }
        catch (Exception e){
            System.out.println("Error! " + e);
        }
    }

    /**
     * Login button action
     * @param event The login is button is clicked
     */
    @FXML
    void handleLoginAction(ActionEvent event) {
        try {
            int userID = Integer.parseInt(tfUserID.getText());
            String password = tfPassword.getText();
            String username = getUserName(userID);
            User user = new User();

            if ((tfUserID.getText().length() == 0) || (tfPassword.getText().length() == 0)) {
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    AlertBox.display("Erreur!", "Les champs ID utilisateur et mot " +
                            "de passe ne peuvent pas \nêtre vides. " +
                            "Veuillez remplir ces champs correctement " +
                            "afin de continuer.");
                    userLoginEmptyFailureLogs();
                } else {
                    AlertBox.display("Error!", "The user ID and password fields" +
                            " cannot \nbe empty. Please correctly " +
                            "fill in these fields in order to continue.");
                    userLoginEmptyFailureLogs();
                }
            } else if (validatePassword(userID, password)) {
                user.setUserID(userID);
                user.setUsername(username);
                userLoginLogs(user.getUserName());//Login log file

                usernameCreatedUpdatedBy = user.getUserName();//Will be used in the Customers and Appointments models

                Parent root = FXMLLoader.load(getClass().getResource("/views/HomeScreen.fxml"));
                Scene scene = new Scene(root);
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                primaryStage.setScene(scene);
                primaryStage.show();

                appointmentAlert();

            } else {
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    AlertBox.display("Erreur!", "Combinaison d'ID utilisateur et de mot de " +
                            "passe non valide. \nRéessayer.");
                    userLoginFailureLogs(userID);

                } else {
                    AlertBox.display("Error!", "Incorrect user ID and password " +
                            " combination. \nPlease try again.");
                    userLoginFailureLogs(userID);

                }
            }
        } catch (Exception e){
            if (Locale.getDefault().getLanguage().equals("fr")) {
                AlertBox.display("Erreur!", "Les champs contiennent des valeurs vides ou incorrectes. " +
                        "\nVous devez entrer des valeurs pour les deux champs pour continuer. " +
                        "\nUn nombre entier doit être saisi pour l'ID utilisateur. " +
                        "\nRéessayer");
                userLoginEmptyFailureLogs();
            } else {
                AlertBox.display("Error!", "The fields contain empty or incorrect values." +
                        "\nYou must enter in values for both fields in order to continue." +
                        "\nAn Integer must be entered for the User ID.\nPlease try again.");
                userLoginEmptyFailureLogs();
            }
        }
    }

    /**
     * Creates a log file assuming one does not already exist
     *  The login information is entered in the log file
     *  This method will log successful logins
     */
    private void userLoginLogs(String user){
        try{
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("login_activity.txt", true));
            bufferedWriter.append(("User: " + user + " successfully logged in at: "
                    + Timestamp.valueOf(LocalDateTime.now()) + "\n"));
            System.out.println("Logged new login record successfully in file.");
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a log file assuming one does not already exist
     *  The login information is entered in the log file
     *  This method will log unsuccessful logins
     *  Specifically incorrect credentials
     */
    private void userLoginFailureLogs(int userID){
        try{
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("login_activity.txt", true));
            bufferedWriter.append(("Invalid Credentials! ID entered: " + userID + ". Login failure logged in at: "
                    + Timestamp.valueOf(LocalDateTime.now()) + "\n"));
            System.out.println("Logged new login failure record successfully in file.");
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a log file assuming one does not already exist
     *  The login information is entered in the log file
     *  This method will log unsuccessful logins
     *  Specifically missing credentials
     */
    private void userLoginEmptyFailureLogs(){
        try{
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("login_activity.txt", true));
            bufferedWriter.append(("User is missing credentials. Login failure logged in at: "
                    + Timestamp.valueOf(LocalDateTime.now()) + "\n"));
            System.out.println("Logged new login failure record successfully in file.");
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays any appointments in the next 15 minutes
     */
    private void appointmentAlert() throws SQLException {
        int appointmentID = 0;
        String appointmentTitle = "";
        String appointmentDescription = "";
        String appointmentLocation = "";
        String appointmentType = "";
        LocalDateTime start = null;
        LocalDateTime startTime = null;
        LocalDateTime end = null;
        String contactID = "";
        int userID = 0, customerID = 0;

        LocalDateTime nowPlus15 = LocalDateTime.now().plusMinutes(15);
        LocalDateTime now = LocalDateTime.now();

        for (Appointments appointment : Appointments.getAllAppointmentsList()) {
            start = appointment.getStart();
            if ((start.isAfter(now) && start.isBefore(nowPlus15))
                    || ( start.isEqual(now) && (start.isEqual(nowPlus15)))) {
                appointmentID = appointment.getAppointmentID();
                appointmentTitle = appointment.getAppointmentTitle();
                appointmentDescription = appointment.getAppointmentDescription();
                appointmentLocation = appointment.getAppointmentLocation();
                appointmentType = appointment.getAppointmentType();
                startTime = appointment.getStart();
                end = appointment.getEnd();
                contactID = appointment.getContact();
                userID = appointment.getUserID();
                customerID = appointment.getCustomerID();

                String contactName = "";

                //Get the contact name associated with the contact ID
                ObservableList<Contacts> getAllContacts = Contacts.getAllContactsList();
                for (Contacts contacts : getAllContacts) {
                    if (contactID.equals(contacts.getContactID())) {
                        contactName = contacts.getContactName();
                    }
                }

                Appointments appointments = new Appointments(appointmentID, appointmentTitle, appointmentDescription,
                        appointmentLocation, appointmentType, startTime, end, customerID, userID, contactName);
                appointmentsReminderOL.add(appointments);

            }

        }

        if (appointmentsReminderOL.isEmpty()) {
            AlertBox.display("Alert", "No upcoming appointments in the next 15 minutes.");
        } else {
            for (Appointments appointment : appointmentsReminderOL) {
                int curAppointmentID = appointment.getAppointmentID();
                LocalDateTime curStart = appointment.getStart();
                LocalDateTime curEnd = appointment.getEnd();
                AlertBox.display("Appointment Alert", "Upcoming appointments in the next 15 minutes.\n"
                        + "Appointment ID: " + curAppointmentID +
                        "\nStart Date and Time: " + curStart
                        + "\nEnd Date and Time: " + curEnd + "\n");
            }
        }
    }

    /**
     * Validates the password provided by checking to see if the userID and password match
     * @param userID The userID used for checking
     * @param password The password to check
     * @return true If the password matches, else return false
     * @throws SQLException Exception thrown
     */
    private boolean validatePassword(int userID, String password) {
        try {
            Statement statement = JDBC.connection.createStatement();
            String query = "SELECT password FROM Users WHERE User_ID = '" + userID + "'";
            ;
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                if (resultSet.getString("password").equals(password)) {
                    return true;
                }
            }
        }catch(SQLException sqle){
            System.out.println("SQL Error - " + sqle);
        } catch(Exception e){
            System.out.println("Error - " + e);
        }
        return false;
    }

    /**
     * Gets the current user's id
     * @param userID The id for checking
     * @return The username
     * @throws SQLException Exception thrown
     */
    private String getUserName(int userID) {
        String username = "";
        try {
            Statement statement = JDBC.connection.createStatement();
            String query = "SELECT User_Name FROM Users WHERE User_ID = '" + userID + "'";
            ;
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                username = resultSet.getString("User_Name");
            }

        }catch(SQLException sqle){
            System.out.println("SQL Error - " + sqle);
        } catch(Exception e){
            System.out.println("Error - " + e);
        }
        return username;
    }

    /**
     * Exit button action
     * @param event If the user clicks on the exit button
     */
    @FXML
    void handleExitAction(ActionEvent event) {
        boolean userChoice = false;

        if(Locale.getDefault().getLanguage().equals("fr")) {
            userChoice = ConfirmBox.display("Confirmer la sortie", "Êtes-vous sûr " +
                    "de vouloir quitter l’application?");
        }
        else {
            userChoice = ConfirmBox.display("Confirm Exit", "Are you sure you want to exit" +
                    " the application?");
        }

        //If the user clicks the 'Yes' button
        if(userChoice){
            System.exit(0);
        }
        //If the user clicks the 'No' button
        else {
            //Ignore
        }
    }


}
