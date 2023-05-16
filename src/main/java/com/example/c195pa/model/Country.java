package com.example.c195pa.model;

import com.example.c195pa.utils.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Model class for country
 *
 * @author Aimy Kohli
 */
public class Country {
    //Variables
    private int countryID;
    private String countryName;

    /**
     * Constructor
     * @param countryID The country ID
     * @param countryName The country name
     */
    public Country(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }

    /**
     * Setter
     * @param countryID The country ID
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /**
     * Setter
     * @param countryName The country name
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * Getter
     * @return The country ID
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * Getter
     * @return The country name
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Gets all the countries
     * @return All the countries in an observable list
     */
    public static ObservableList<Country> getAllCountriesList() {
        ObservableList<Country> countriesOL = FXCollections.observableArrayList();
        try {
            String query = "SELECT Country_ID, Country FROM countries";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int countryID = resultSet.getInt("Country_ID");
                String countryName = resultSet.getString("Country");

                Country country = new Country(countryID, countryName);
                countriesOL.add(country);
            }
        } catch (SQLException sqle){
            System.out.println("SQL Error - " + sqle);
        } catch (Exception e){
            System.out.println("Error - " + e);
        }
        return countriesOL;
    }

}
