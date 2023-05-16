package com.example.c195pa.controllers;

import com.example.c195pa.model.*;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Controller class for the Reports Screen
 *
 * @author Aimy Kohli
 */

public class ReportsController implements Initializable {

    //Consultant Tab
    @FXML
    private TableView<Appointments> tvConsultant;

    @FXML
    private TableColumn<Appointments, String> colContactID;

    @FXML
    private TableColumn<Appointments, Integer> colCustomerID;

    @FXML
    private TableColumn<Appointments, Integer> colAppID;

    @FXML
    private TableColumn<Appointments, String> colTitle;

    @FXML
    private TableColumn<Appointments, String> colDescription;

    @FXML
    private TableColumn<Appointments, String> colType;

    @FXML
    private TableColumn<Appointments, LocalDateTime> colAppStart;

    @FXML
    private TableColumn<Appointments, LocalDateTime> colAppEnd;

    //Appointments by month tab
    @FXML
    private TableView<ReportsTypeTotal> tvAppTypeTotal;

    @FXML
    private TableColumn<ReportsTypeTotal, String> colAppType;

    @FXML
    private TableColumn<ReportsTypeTotal, Integer> colAppTypeTotal;

    @FXML
    private TableView<ReportsMonthTotal> tvAppMonthTotal;

    @FXML
    private TableColumn<ReportsMonthTotal, String> colAppByMonth;

    @FXML
    private TableColumn<ReportsMonthTotal, Integer> colAppByMonthTotal;

    @FXML
    private TableView<Months> tvNoAppMonthTotal;

    @FXML
    private TableColumn<Months, String> colNoAppByMonth;

    @FXML
    private TableColumn<Months, Integer> colNoAppByMonthTotal;

    //Customer appointment total tab
    @FXML
    private TableView<ReportsCustomerAppTotal> tvCustAppTotal;

    @FXML
    private TableColumn<ReportsCustomerAppTotal, Integer> colAppTotal;

    @FXML
    private TableColumn<ReportsCustomerAppTotal, String> colCustomerNameAppTotal;

    @FXML
    private TableColumn<ReportsCustomerAppTotal, Integer> colCustomerIDAppTotal;

    /**
     * Initializes the table view
     * @param url Points to any specified resource such as a file or link
     * @param resourceBundle Locale-specific data from the end user's side
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            //Data for Consultant tableview
            ObservableList<Appointments> appointmentsOL = Appointments.getAllAppointmentsList();

            colContactID.setCellValueFactory(new PropertyValueFactory<Appointments, String>("contact"));
            colAppID.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("appointmentID"));
            colTitle.setCellValueFactory(new PropertyValueFactory<Appointments, String>("appointmentTitle"));
            colType.setCellValueFactory(new PropertyValueFactory<Appointments, String>("appointmentType"));
            colDescription.setCellValueFactory(new PropertyValueFactory<Appointments, String>("appointmentDescription"));
            colAppStart.setCellValueFactory(new PropertyValueFactory<Appointments, LocalDateTime>("start"));
            colAppEnd.setCellValueFactory(new PropertyValueFactory<Appointments, LocalDateTime>("end"));
            colCustomerID.setCellValueFactory(new PropertyValueFactory<Appointments, Integer>("customerID"));
            tvConsultant.setItems(appointmentsOL);

            //Data for Appointment type by month tableview
            tvAppTypeTotal.setPlaceholder(new Label("No appointment types for the current month."));//placeholder text for the tableview
            ObservableList<ReportsTypeTotal> reportsTypeTotalOL = countAppointmentsByType();

            colAppType.setCellValueFactory(new PropertyValueFactory<ReportsTypeTotal, String>("appointmentType"));
            colAppTypeTotal.setCellValueFactory(new PropertyValueFactory<ReportsTypeTotal, Integer>("appointmentTypeTotal"));
            tvAppTypeTotal.setItems(reportsTypeTotalOL);

            //Data for Appointment total by month tableview
            ObservableList<ReportsMonthTotal> monthWAppOL = countExistingAppointmentsByMonth();

            colAppByMonth.setCellValueFactory(new PropertyValueFactory<ReportsMonthTotal, String >("appointmentMonth"));
            colAppByMonthTotal.setCellValueFactory(new PropertyValueFactory<ReportsMonthTotal, Integer>("appointmentMonthTotal"));
            tvAppMonthTotal.setItems(monthWAppOL);

            ObservableList<Months> monthWNoAppOL = countAppointmentsByMonth();

            colNoAppByMonth.setCellValueFactory(new PropertyValueFactory<Months, String >("monthName"));
            colNoAppByMonthTotal.setCellValueFactory(new PropertyValueFactory<Months, Integer >("noAppointmentTotal"));
            tvNoAppMonthTotal.setItems(monthWNoAppOL);

            //Data for total number of appointments per customer tableview
            ObservableList<ReportsCustomerAppTotal> reportsCustomerAppTotalsOL = countAppointmentsByCustomerID();

            colCustomerIDAppTotal.setCellValueFactory(new PropertyValueFactory<ReportsCustomerAppTotal, Integer>("customerID"));
            colCustomerNameAppTotal.setCellValueFactory(new PropertyValueFactory<ReportsCustomerAppTotal, String>("customerName"));
            colAppTotal.setCellValueFactory(new PropertyValueFactory<ReportsCustomerAppTotal, Integer>("appointmentTotal"));

            tvCustAppTotal.setItems(reportsCustomerAppTotalsOL);

        } catch (Exception e) {
            System.out.println("Error - " + e);
        }
    }

    /**
     * Gets all the appointment types and totals ONLY for the current month
     * @return reportsTypeTotalOL An observable list containg the appointmen type and total
     * @throws Exception The exception
     */
    public ObservableList<ReportsTypeTotal> countAppointmentsByType() throws Exception {
        ObservableList<ReportsTypeTotal> reportsTypeTotalOL = FXCollections.observableArrayList();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMonthMinus = now.minusMonths(1);
        LocalDateTime oneMonthPlus = now.plusMonths(1);
        Statement statement = JDBC.connection.createStatement();

        String query = "SELECT Type, COUNT(*) AS Total " +
                "FROM appointments WHERE (Start BETWEEN '" + oneMonthMinus +
                "' AND '" + oneMonthPlus +
                "') GROUP BY Type";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            String appointmentType = resultSet.getString("Type");
            int typeCount = resultSet.getInt("Total");

            ReportsTypeTotal reportsTypeTotal = new ReportsTypeTotal(appointmentType, typeCount);
            reportsTypeTotalOL.add(reportsTypeTotal);
        }
        statement.close();
        return reportsTypeTotalOL;
    }

    /**
     * Gets all the months that have 1 or more appointments
     * @return The appointment total per month
     * @throws SQLException The SQL Exception
     */
    public ObservableList<ReportsMonthTotal> countExistingAppointmentsByMonth() throws SQLException{
        ObservableList<ReportsMonthTotal> reportsMonthTotalsOL = FXCollections.observableArrayList();

        Statement statement = JDBC.connection.createStatement();
        String query = "SELECT MONTHNAME(start) AS 'Month_Name', COUNT( MONTHNAME(start)) AS 'Appointment_Total'\n" +
                "FROM appointments\n" +
                "GROUP BY YEAR(start), MONTH(start);";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            String monthName = resultSet.getString("Month_Name");
            int appointmentTotal = resultSet.getInt("Appointment_Total");

            ReportsMonthTotal reportsCustomerAppTotal = new ReportsMonthTotal(monthName, appointmentTotal);
            reportsMonthTotalsOL.add(reportsCustomerAppTotal);
        }
        statement.close();
        return reportsMonthTotalsOL;
    }

    /**
     * Gets all the months that have no appointments
     * @return The appointment total per month
     * @throws SQLException The SQL Exception
     */
    public ObservableList<Months> countAppointmentsByMonth() throws SQLException{
        ObservableList<Months> monthsOL = FXCollections.observableArrayList();
        ObservableList<Months> monthsWNoAppOL = FXCollections.observableArrayList();

        String january = "", february = "", march = "", april = "", may = "", june = "",
        july = "", august = "", september = "", october = "", november = "", december = "";

        Statement statement = JDBC.connection.createStatement();
        String query = "SELECT MONTHNAME(start) AS 'Month_Name', COUNT( MONTHNAME(start)) AS 'Appointment_Total'\n" +
                "FROM appointments\n" +
                "GROUP BY YEAR(start), MONTH(start);";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            String monthName = resultSet.getString("Month_Name");
            int appointmentTotal = resultSet.getInt("Appointment_Total");

            Months months = new Months(monthName, appointmentTotal);
            monthsOL.add(months);
        }

        for (int i = 0; i < monthsOL.size(); i++){
            String monthName = monthsOL.get(i).getMonthName();

            if(monthName.equals("January")){
                january = monthName;
            }
            if(monthName.equals("February")){
                february = monthName;
            }
            if(monthName.equals("March")){
                march = monthName;
            }
            if(monthName.equals("April")){
                april = monthName;
            }
            if(monthName.equals("May")){
                may = monthName;
            }
            if(monthName.equals("June")){
                june = monthName;
            }
            if(monthName.equals("July")){
                july = monthName;
            }
            if(monthName.equals("August")){
                august = monthName;
            }
            if(monthName.equals("September")){
                september = monthName;
            }
            if(monthName.equals("October")){
                october = monthName;
            }
            if(monthName.equals("November")){
                november = monthName;
            }
            if(monthName.equals("December")){
                december = monthName;
            }
        }
        //If month is empty then add a value to it and add it to the OL
        if(january.isEmpty()){
            january = "January";
            Months months = new Months(january, 0);
            monthsWNoAppOL.add(months);
        }
        if(february.isEmpty()){
            february = "February";
            Months months = new Months(february, 0);
            monthsWNoAppOL.add(months);
        }
        if(march.isEmpty()){
            march = "March";
            Months months = new Months(march, 0);
            monthsWNoAppOL.add(months);
        }
        if(april.isEmpty()){
            april = "April";
            Months months = new Months(april, 0);
            monthsWNoAppOL.add(months);
        }
        if(may.isEmpty()){
            may = "May";
            Months months = new Months(may, 0);
            monthsWNoAppOL.add(months);
        }
        if(june.isEmpty()){
            june = "June";
            Months months = new Months(june, 0);
            monthsWNoAppOL.add(months);
        }
        if(july.isEmpty()){
            july = "July";
            Months months = new Months(july, 0);
            monthsWNoAppOL.add(months);
        }
        if(august.isEmpty()){
            august = "August";
            Months months = new Months(august, 0);
            monthsWNoAppOL.add(months);
        }
        if(september.isEmpty()){
            september = "September";
            Months months = new Months(september, 0);
            monthsWNoAppOL.add(months);
        }
        if(october.isEmpty()){
            october = "October";
            Months months = new Months(october, 0);
            monthsWNoAppOL.add(months);
        }
        if(november.isEmpty()){
            november = "November";
            Months months = new Months(november, 0);
            monthsWNoAppOL.add(months);
        }
        if(december.isEmpty()){
            december = "December";
            Months months = new Months(december, 0);
            monthsWNoAppOL.add(months);
        }

        statement.close();
        return monthsWNoAppOL;
    }

    /**
     * Counts the total number of appointments that each customer has
     * @throws SQLException The SQL Exception
     */
    public ObservableList<ReportsCustomerAppTotal> countAppointmentsByCustomerID() throws SQLException {
        ObservableList<ReportsCustomerAppTotal> reportsCustAppTotalOL = FXCollections.observableArrayList();

        Statement statement = JDBC.connection.createStatement();
        String query = "SELECT customers.Customer_ID, customers.Customer_Name, COUNT(appointments.Customer_ID) \n" +
                "AS total_appointments\n" +
                "FROM appointments JOIN customers ON appointments.customer_id = customers.customer_iD\n" +
                "GROUP BY customer_name \n" +
                "ORDER BY customer_name;";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int customerID = resultSet.getInt("Customer_ID");
            String customerName = resultSet.getString("Customer_Name");
            int appointmentTotal = resultSet.getInt("total_appointments");

            ReportsCustomerAppTotal reportsCustomerAppTotal = new ReportsCustomerAppTotal(customerID, customerName, appointmentTotal);
            reportsCustAppTotalOL.add(reportsCustomerAppTotal);
        }
        statement.close();
        return reportsCustAppTotalOL;
    }

    /**
     * Goes back to the home screen
     * @param event If the back button is clicked
     * @throws IOException The exception
     */
    @FXML
    void handleBackButton(ActionEvent event) throws IOException {
        boolean userChoice = ConfirmBox.display("Confirm Cancellation", "Are you sure you would like to go back " +
                "to the Home page?");

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
