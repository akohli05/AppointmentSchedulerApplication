package com.example.c195pa.controllers;

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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Controller class for the Customer Update Screen
 *
 * @author Aimy Kohli
 */

public class CustomerUpdateController implements Initializable {

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private ComboBox<String> cbCountry;

    @FXML
    private ComboBox<String> cbStateProvince;

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

    //Variables
    ObservableList<String> countryCBOptions = FXCollections.observableArrayList();
    ObservableList<String> stateProvinceUSOptions = FXCollections.observableArrayList();
    ObservableList<String> stateProvinceUKOptions = FXCollections.observableArrayList();
    ObservableList<String> stateProvinceCAOptions = FXCollections.observableArrayList();

    /**
     * Initializes the table view
     * @param url Points to any specified resource such as a file or link
     * @param resourceBundle Locale-specific data from the end user's side
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            int customerID = CustomerController.getCustomerModifyID();
            String divisionName = "";
            String customerName = "";
            String address = "";
            String postalCode = "";
            String phone = "";
            String countryName = "";
            int divisionID = 0;

            for (Customers customers : Customers.getAllCustomersList()) {
                if (customerID == customers.getCustomerID()) {
                    divisionName = customers.getDivisionName();
                    customerName = customers.getCustomerName();
                    address = customers.getCustomerAddress();
                    postalCode = customers.getCustomerPostalCode();
                    phone = customers.getCustomerPhoneNumber();
                    divisionID = customers.getDivisionID();

                }
            }

            ObservableList<Country> getAllCountries = Country.getAllCountriesList();
            ObservableList<FirstLevelDivision> getAllDivisionNames = FirstLevelDivision.getAllDivisionsList();
            ObservableList<String> allDivisionNamesOL = FXCollections.observableArrayList();

            for (FirstLevelDivision firstLevelDivision: getAllDivisionNames) {
                allDivisionNamesOL.add(firstLevelDivision.getDivisionName());
                int countryID = firstLevelDivision.getCountryID();

                if (firstLevelDivision.getDivisionID() == divisionID) {
                    divisionName = firstLevelDivision.getDivisionName();

                    for (Country country: getAllCountries) {
                        if (country.getCountryID() == countryID) {
                            countryName = country.getCountryName();
                        }
                    }
                }
            }

            cbStateProvince.setValue(divisionName);
            tfID.setText(String.valueOf(customerID));
            tfName.setText(customerName);
            tfAddress.setText(address);
            tfPhone.setText(phone);
            tfPostalCode.setText(postalCode);
            cbCountry.setValue(countryName);

            fillCountryCB();
            fillStateProvince();

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
     * Updates a customer
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

            Customers editCustomer = new Customers( divisionName,  customerID, customerName, address,
                     postalCode,phone, divisionID);

            Customers.updateCustomer(editCustomer);

            AlertBox.display("Success!", "The customer has been updated.");

            Parent root = FXMLLoader.load(getClass().getResource("/views/CustomerScreen.fxml"));
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e){
            AlertBox.display("Error!", "The form contains empty fields" +
                    " or invalid data. Please make sure that each field includes the appropriate values.\n" +
                    "1. Customer ID - valid ID - INTEGERS ONLY \n" +
                    "2. Full Name - valid name\n" +
                    "3. Address - valid address\n" +
                    "4. Postal Code - valid postal code\n" +
                    "5. Country - valid selection\n"+
                    "6. State/Province - valid selection\n");
        }
    }

    /**
     * Goes back to the main Customers screen
     * @param event If the cancel button is clicked
     * @throws IOException The exception
     */
    @FXML
    void handleCancelAction(ActionEvent event) throws IOException {
        boolean userChoice = ConfirmBox.display("Confirm Cancellation", "Are you sure you would like to cancel " +
                "updating this customer?");

        if(userChoice){
            Parent root = FXMLLoader.load(getClass().getResource("/views/CustomerScreen.fxml"));
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
