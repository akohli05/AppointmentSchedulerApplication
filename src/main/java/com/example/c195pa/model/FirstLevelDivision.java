package com.example.c195pa.model;

import com.example.c195pa.utils.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 *
 * @author Aimy Kohli
 */
public class FirstLevelDivision {
    //Variables
    private int divisionID;
    private String divisionName;
    public int countryID;

    /**
     * Constructor
     * @param divisionID The division ID
     * @param divisionName The division name
     * @param countryID The country ID
     */
    public FirstLevelDivision(int divisionID, String divisionName, int countryID) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryID = countryID;
    }

    /**
     * Setter
     * @param divisionID The division ID
     */
    public void setDivisionID(int divisionID) {
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
     * @param countryID The country ID
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /**
     * Getter
     * @return The division ID
     */
    public int getDivisionID() {
        return divisionID;
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
     * @return The country ID
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * Gets all the first level divisions
     * @return All the first level divisions in an observable list
     */
    public static ObservableList<FirstLevelDivision> getAllDivisionsList() {
        ObservableList<FirstLevelDivision> firstLevelDivisionsOL = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM first_level_divisions";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int divisionID = resultSet.getInt("Division_ID");
                String divisionName = resultSet.getString("Division");
                int countryID = resultSet.getInt("Country_ID");

                FirstLevelDivision firstLevelDivision = new FirstLevelDivision(divisionID, divisionName, countryID);
                firstLevelDivisionsOL.add(firstLevelDivision);
            }
        } catch (SQLException sqle){
            System.out.println("SQL Error - " + sqle);
        } catch (Exception e){
            System.out.println("Non SQL Related Error - " + e);
        }
        return firstLevelDivisionsOL;
    }
}
