package com.example.c195pa.model;

import com.example.c195pa.controllers.LoginController;
import com.example.c195pa.utils.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Model class customers
 * Contains information relating to the customers
 */
public class Customers {
    //Variables
    private String divisionName;
    private int customerID;
    private String customerName, customerAddress,
            customerPostalCode, customerPhoneNumber;
    private int divisionID;

    /**
     * Constructor
     * @param divisionName The division name
     * @param customerID The customer ID
     * @param customerName The customer name
     * @param customerAddress The customer address
     * @param customerPostalCode The customer postal code
     * @param customerPhoneNumber The customer phone number
     * @param divisionID The division ID
     */
    public Customers(String divisionName, int customerID, String customerName, String customerAddress,
                     String customerPostalCode, String customerPhoneNumber, int divisionID) {
        this.divisionName = divisionName;
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhoneNumber = customerPhoneNumber;
        this.divisionID = divisionID;
    }

    /**
     * Setter
     * @param divisionName The division name
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
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
     * @param customerName The customer name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Setter
     * @param customerAddress The customer address
     */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    /**
     * Setter
     * @param customerPostalCode The customer postal code
     */
    public void setCustomerPostalCode(String customerPostalCode) {
        this.customerPostalCode = customerPostalCode;
    }

    /**
     * Setter
     * @param customerPhoneNumber The customer phone number
     */
    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    /**
     * Setter
     * @param divisionID The division ID
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**
     * Getter
     * @return The division name
     */
    public String getDivisionName() {
        return divisionName;
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
     * @return The customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Getter
     * @return The customer address
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * Getter
     * @return The customer postal code
     */
    public String getCustomerPostalCode() {
        return customerPostalCode;
    }

    /**
     * Getter
     * @return The customer phone number
     */
    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    /**
     * Getter
     * @return The division ID
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * Adds a new customer
     * @param newCustomer The customer to add
     * @throws Exception The exception
     */
    public static void addCustomer(Customers newCustomer) throws Exception {
        int customerID = newCustomer.getCustomerID();
        String customerName = newCustomer.getCustomerName();
        String customerAddress = newCustomer.getCustomerAddress();
        String postalCode = newCustomer.getCustomerPostalCode();
        String customerPhoneNumber = newCustomer.getCustomerPhoneNumber();
        String state = newCustomer.getDivisionName();

        int divisionID = 0;
        for (FirstLevelDivision firstLevelDivision : FirstLevelDivision.getAllDivisionsList()) {
            if (state.equals(firstLevelDivision.getDivisionName())) {
                divisionID = firstLevelDivision.getDivisionID();
            }
        }

        String query = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone," +
                " Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement1 = JDBC.connection.prepareStatement(query);

        preparedStatement1.setInt(1, customerID);
        preparedStatement1.setString(2, customerName);
        preparedStatement1.setString(3, customerAddress);
        preparedStatement1.setString(4, postalCode);
        preparedStatement1.setString(5, customerPhoneNumber);
        preparedStatement1.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        preparedStatement1.setString(7, LoginController.usernameCreatedUpdatedBy);
        preparedStatement1.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        preparedStatement1.setString(9, LoginController.usernameCreatedUpdatedBy);
        preparedStatement1.setInt(10, divisionID);

        preparedStatement1.executeUpdate();
    }

    /**
     * Updates customer
     * @param editCustomer The customer to update
     * @throws Exception The exception
     */
    public static void updateCustomer(Customers editCustomer) throws Exception {
        int customerID = editCustomer.getCustomerID();
        String customerName = editCustomer.getCustomerName();
        String customerAddress = editCustomer.getCustomerAddress();
        String postalCode = editCustomer.getCustomerPostalCode();
        String customerPhoneNumber = editCustomer.getCustomerPhoneNumber();
        String state = editCustomer.getDivisionName();

        int divisionID = 0;
        for (FirstLevelDivision firstLevelDivision : FirstLevelDivision.getAllDivisionsList()) {
            if (state.equals(firstLevelDivision.getDivisionName())) {
                divisionID = firstLevelDivision.getDivisionID();
            }
        }

        String query = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code" +
                " = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? " +
                "WHERE Customer_ID = ?";
        PreparedStatement preparedStatement1 = JDBC.connection.prepareStatement(query);

        preparedStatement1.setString(1, customerName);
        preparedStatement1.setString(2, customerAddress);
        preparedStatement1.setString(3, postalCode);
        preparedStatement1.setString(4, customerPhoneNumber);
        preparedStatement1.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        preparedStatement1.setString(6, LoginController.usernameCreatedUpdatedBy);
        preparedStatement1.setInt(7, divisionID);
        preparedStatement1.setInt(8, customerID);

        preparedStatement1.executeUpdate();
    }

    /**
     * Gets all the customers
     * @return All the customers in an observable list
     */
    public static ObservableList<Customers> getAllCustomersList() {
        ObservableList<Customers> customersOL = FXCollections.observableArrayList();
        try {
            String query = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, " +
                    "customers.Postal_Code, customers.Phone, customers.Division_ID, " +
                    "first_level_divisions.Division FROM customers INNER JOIN " +
                    "first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String division = resultSet.getString("Division");
                int customerID = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                String customerAddress = resultSet.getString("Address");
                String customerPostalCode = resultSet.getString("Postal_Code");
                String customerPhoneNumber = resultSet.getString("Phone");
                int divisionID = resultSet.getInt("Division_ID");

                Customers customers = new Customers( division, customerID, customerName, customerAddress,
                         customerPostalCode, customerPhoneNumber, divisionID);
                customersOL.add(customers);
            }
        } catch (SQLException sqle){
            System.out.println("SQL Error - " + sqle);
        } catch (Exception e){
            System.out.println("Error - " + e);
        }
        return customersOL;
    }

    /**
     * Gets an autogenerated customer ID for adding a customer
     * @return The new autogenerated customer ID
     */
    public static int getAddCustomerID() throws SQLException {
        int customerID = 0;
        int newCustomerID;

        String query = "SELECT MAX(Customer_ID) FROM customers";
        PreparedStatement preparedStatement1 = JDBC.connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement1.executeQuery();

        while (resultSet.next()) {
            customerID = resultSet.getInt("MAX(Customer_ID)");
        }

        newCustomerID = customerID+1;
        return newCustomerID;
    }

}
