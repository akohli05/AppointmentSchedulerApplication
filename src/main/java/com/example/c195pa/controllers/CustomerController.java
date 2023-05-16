package com.example.c195pa.controllers;

import com.example.c195pa.model.Appointments;
import com.example.c195pa.model.Country;
import com.example.c195pa.model.Customers;
import com.example.c195pa.model.FirstLevelDivision;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Controller class for the Customers Screen
 *
 * @author Aimy Kohli
 */
public class CustomerController implements Initializable {

    @FXML
    private ComboBox<String> cbCountry;

    @FXML
    private ComboBox<String> cbStateProvince;

    @FXML
    private TableColumn<Customers, String> colAddress;

    @FXML
    private TableColumn<Customers, Integer> colID;

    @FXML
    private TableColumn<Customers, String> colName;

    @FXML
    private TableColumn<Customers, String> colPhone;

    @FXML
    private TableColumn<Customers, String> colPostalCode;

    @FXML
    private TableColumn<Customers, String> colDivisionName;

    @FXML
    private TextField tfAddress;

    @FXML
    private TextField tfID;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfPhone;

    @FXML
    private TextField tfPostalCode;

    @FXML
    private TableView<Customers> tvCustomers;

    //Variables
    private static int customerModifyID;
    ObservableList<String> countryCBOptions = FXCollections.observableArrayList();
    ObservableList<String> stateProvinceUSOptions = FXCollections.observableArrayList();
    ObservableList<String> stateProvinceUKOptions = FXCollections.observableArrayList();
    ObservableList<String> stateProvinceCAOptions = FXCollections.observableArrayList();

    /**
     * Returns a specific customer ID that will be used for modifying the correct part
     * @return The customer ID
     */
    public static int getCustomerModifyID(){
        return customerModifyID;
    }

    /**
     * Initializes the table view
     * @param url Points to any specified resource such as a file or link
     * @param resourceBundle Locale-specific data from the end user's side
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Customers> customersOL = Customers.getAllCustomersList();

            colID.setCellValueFactory(new PropertyValueFactory<Customers, Integer>("customerID"));
            colName.setCellValueFactory(new PropertyValueFactory<Customers, String>("customerName"));
            colPhone.setCellValueFactory(new PropertyValueFactory<Customers, String>("customerPhoneNumber"));
            colAddress.setCellValueFactory(new PropertyValueFactory<Customers, String>("customerAddress"));
            colPostalCode.setCellValueFactory(new PropertyValueFactory<Customers, String>("customerPostalCode"));
            colDivisionName.setCellValueFactory(new PropertyValueFactory<Customers, String>("divisionName"));

            tvCustomers.setItems(customersOL);

            fillCountryCB();
            fillStateProvince();

            //Fills in the auto generated customer ID
            int customerID =  Customers.getAddCustomerID();
            tfID.setText(String.valueOf(customerID));

        } catch (Exception e) {
            System.out.println("Error - " + e);
        }
    }

    /**
     * Fills in the country combo box
     * @throws SQLException The SQL Exception
     * @throws Exception The Exception
     */
    public void fillCountryCB() throws SQLException, Exception {

        String query = "SELECT Country FROM countries";
        Statement statement = JDBC.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        //Result set
        while (resultSet.next()) {
            String countryName = resultSet.getString("Country");
            countryCBOptions.add(countryName);
        }
        cbCountry.setItems(countryCBOptions);
        statement.close();
        resultSet.close();
    }

    /**
     * Fills in the state province combo box
     * @throws SQLException The SQL Exception
     * @throws Exception The Exception
     */
    public void fillStateProvince() throws SQLException, Exception, NullPointerException {
        String selectedCountry = cbCountry.getSelectionModel().getSelectedItem();
        String query = new String();
        if(selectedCountry != null) {
            if (selectedCountry.equals("U.S")) {

                query = "SELECT Division FROM first_level_divisions\n" +
                        "WHERE Country_ID = 1";
                Statement statement = JDBC.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                //Result set
                while (resultSet.next()) {
                    String countryName = resultSet.getString("Division");
                    stateProvinceUSOptions.add(countryName);
                }
                cbStateProvince.setItems(stateProvinceUSOptions);
                statement.close();
                resultSet.close();
            }
            if (selectedCountry.equals("UK")) {
                query = "SELECT Division FROM first_level_divisions\n" +
                        "WHERE Country_ID = 2";
                Statement statement = JDBC.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                //Result set
                while (resultSet.next()) {
                    String countryName = resultSet.getString("Division");
                    stateProvinceUKOptions.add(countryName);
                }
                cbStateProvince.setItems(stateProvinceUKOptions);
                statement.close();
                resultSet.close();
            }
            if (selectedCountry.equals("Canada")) {
                query = "SELECT Division FROM first_level_divisions\n" +
                        "WHERE Country_ID = 3";
                Statement statement = JDBC.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                //Result set
                while (resultSet.next()) {
                    String countryName = resultSet.getString("Division");
                    stateProvinceCAOptions.add(countryName);
                }
                cbStateProvince.setItems(stateProvinceCAOptions);
                statement.close();
                resultSet.close();
            }
        }
        else {
            //Default
            ObservableList<String> defaultOL = FXCollections.observableArrayList();
            defaultOL.add("Please select a country first");
            cbStateProvince.setItems(defaultOL);
            cbStateProvince.setValue(defaultOL.get(0));
        }
    }

    /**
     * Saves a new customer
     * @param event If the save button is clicked
     */
    @FXML
    void handleSaveAction(ActionEvent event) {
        try{
            String divisionName = (String) cbStateProvince.getValue();
            int customerID = Integer.parseInt(tfID.getText());
            String customerName = tfName.getText();
            String address = tfAddress.getText();
            String postalCode = tfPostalCode.getText();
            String phone = tfPhone.getText();
            String country = (String) cbCountry.getValue();

            int divisionID = 0;
            for (FirstLevelDivision firstLevelDivision : FirstLevelDivision.getAllDivisionsList()) {
                if (divisionName.equals(firstLevelDivision.getDivisionName())) {
                    divisionID = firstLevelDivision.getDivisionID();
                }
            }

            Customers newCustomer = new Customers( divisionName,  customerID, customerName, address,
                    postalCode,phone, divisionID);

            Customers.addCustomer(newCustomer);

            //Clear the text fields
            tfName.clear();
            tfID.clear();
            tfAddress.clear();
            tfPostalCode.clear();
            tfPhone.clear();
            cbCountry.setValue("");
            cbStateProvince.setValue("");

            //Refresh the table view
            tvCustomers.setItems(Customers.getAllCustomersList());

            AlertBox.display("Success!", "The new customer has been added.");

            //Fills in the auto generated customer ID
            int customerIDAutoFill =  Customers.getAddCustomerID();
            tfID.setText(String.valueOf(customerIDAutoFill));

        }catch (Exception e){
            AlertBox.display("Error!", "The form contains empty fields" +
                    " or invalid data. Please make sure that each field includes the appropriate values.\n" +
                    "1. Customer ID - valid and unique ID - INTEGERS ONLY \n" +
                    "2. Full Name - valid name\n" +
                    "3. Address - valid address\n" +
                    "4. Postal Code - valid postal code\n" +
                    "5. Country - valid selection\n"+
                    "6. State/Province - valid selection\n");
        }
    }

    /**
     * Opens up the update customer screen
     * @param event If the update button is clicked
     * @throws IOException The Exception
     */
    @FXML
    void handleUpdateAction(ActionEvent event) throws IOException {
        if(tvCustomers.getSelectionModel().getSelectedItem() == null){
            AlertBox.display("Error!", "Please select a customer to update.");
        }
        else{
            customerModifyID = tvCustomers.getSelectionModel().getSelectedItem().getCustomerID();
            Parent root = FXMLLoader.load(getClass().getResource("/views/CustomerUpdate.fxml"));
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    /**
     * Deletes a customer
     * @param event If the delete button is clicked
     * @throws Exception The exception
     */
    @FXML
    void handleDeleteAction(ActionEvent event) throws Exception {
        if(tvCustomers.getSelectionModel().getSelectedItem() == null){
            AlertBox.display("Error!", "Please select a customer to delete.");
        }
        else{
            boolean userChoice = ConfirmBox.display("Confirm Deletion", "Are you sure you would like to " +
                    "delete this customer and all their related appointments?");

            if(userChoice){
                ObservableList<Appointments> getAllAppointmentsList = Appointments.getAllAppointmentsList();

                //Gets the customer ID to delete
                int customerID = tvCustomers.getSelectionModel().getSelectedItem().getCustomerID();

                //Deletes all related appointments
                for(Appointments appointments : getAllAppointmentsList) {
                    String query1 = "DELETE FROM Appointments WHERE Customer_ID = ?";
                    PreparedStatement preparedStatement1 = JDBC.connection.prepareStatement(query1);
                    preparedStatement1.setInt(1, customerID);
                    preparedStatement1.executeUpdate();
                }
                //Deletes the customer
                String query = "DELETE FROM Customers WHERE Customer_ID = ?";
                PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
                preparedStatement.setInt(1, customerID);
                preparedStatement.executeUpdate();

                //Refresh the table view
                tvCustomers.setItems(Customers.getAllCustomersList());
                AlertBox.display("Delete Successful","The customer with the ID - " + customerID + " and their related appointments" +
                        " have been deleted successfully.");

                //Fills in the auto generated customer ID
                int customerIDAutoFill =  Customers.getAddCustomerID();
                tfID.setText(String.valueOf(customerIDAutoFill));
            }
            else {
                System.out.println("The user chose not to confirm the delete.");
            }
        }
    }

    /**
     * Goes back to the home screen
     * @param event If the back button is clicked
     * @throws IOException The exception
     */
    @FXML
    void handleBackAction(ActionEvent event) throws IOException {
        boolean userChoice = ConfirmBox.display("Confirm Exit", "Are you sure you would like to go back " +
                "to the Home page?\nIf you did not click the 'Save' button, then the entered information for " +
                "a new customer will be lost.");

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
