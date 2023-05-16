package com.example.c195pa.model;

import com.example.c195pa.utils.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The User class
 *
 * @author Aimy Kohli
 */
public class User {

    //Variables
    public int userID;
    public static String userName;
    public String userPassword;

    /**
     * No arg constructor for User class
     */
    public User(){
        this.userID = userID;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    /**
     * Constructor
     * @param userID The user ID
     * @param userName The userName
     * @param userPassword The user password
     */
    public User(int userID, String userName, String userPassword){
        this.userID = userID;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    /**
     * Setter for the userID
     * @param userID The user ID
     */
    public void setUserID(int userID){
        this.userID = userID;
    }

    /**
     * Setter for the userName
     * @param username The username
     */
    public void setUsername(String username){
        this.userName = username;
    }

    /**
     * Setter for the userPassword
     * @param userPassword The user password
     */
    public void setUserPassword(int userPassword){
        this.userID = userID;
    }

    /**
     * Getter
     * @return The userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Getter
     * @return The username
     */
    public static String getUserName() {
        return userName;
    }

    /**
     * Getter
     * @return The user password
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * Gets all the users
     * @return All the users in an observable list
     */
    public static ObservableList<User> getAllUsersList() {
        ObservableList<User> usersOL = FXCollections.observableArrayList();
        try {
            String query = "SELECT User_ID, User_Name, Password FROM users";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int userID = resultSet.getInt("User_ID");
                String userName = resultSet.getString("User_Name");
                String password = resultSet.getString("Password");

                User users = new User(userID, userName, password);
                usersOL.add(users);
            }
        } catch (SQLException sqle){
            System.out.println("SQL Error - " + sqle);
        } catch (Exception e){
            System.out.println("Error - " + e);
        }
        return usersOL;
    }

}
