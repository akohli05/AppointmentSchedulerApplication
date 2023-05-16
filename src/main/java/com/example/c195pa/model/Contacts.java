package com.example.c195pa.model;

import com.example.c195pa.utils.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Model class for contacts
 * Contact related information
 *
 * @author Aimy Kohli
 */
public class Contacts {
    //Variables
    public int contactID;
    public String contactName;
    public String contactEmail;

    /**
     * Constructor
     * @param contactID The contact ID
     * @param contactName The contact name
     * @param contactEmail The contact email
     */
    public Contacts(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**
     * Setter
     * @param contactID The contact ID
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * Setter
     * @param contactName The contact name
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Setter
     * @param contactEmail The contact email
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     * Getter
     * @return The contact ID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Getter
     * @return The contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Getter
     * @return The contact email
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * Gets all the contacts
     * @return All the contacts in an observable list
     */
    public static ObservableList<Contacts> getAllContactsList() {
        ObservableList<Contacts> contactsOL = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM contacts";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int contactID = resultSet.getInt("Contact_ID");
                String contactName = resultSet.getString("Contact_Name");
                String email = resultSet.getString("Email");

                Contacts contacts = new Contacts(contactID, contactName, email);
                contactsOL.add(contacts);
            }
        } catch (SQLException sqle){
            System.out.println("SQL Error - " + sqle);
        } catch (Exception e){
            System.out.println("Error - " + e);
        }
        return contactsOL;
    }
}
