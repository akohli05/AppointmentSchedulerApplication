package com.example.c195pa.main;

import com.example.c195pa.controllers.ConfirmBox;
import com.example.c195pa.utils.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Locale;

/**
 *  <h3>Inventory Management System Main Class </h3>
 *
 *  <h5>FUTURE ENHANCEMENTS:</h5>
 *   <ol>
 *       <li>Add a database to store the data provided by the Parts, Products, and Inventory. It is
 *       best use databases for CRUD applications since it can enhance security and allow for
 *       better data management.</li>
 *       <li>Add more appealing colors and designs to application. It is good to avoid bright
 *       colors and too many images in applications. However, adding some more styling and colors
 *       to the applications allows for a better user experience.</li>
 *   </ol>
 *
 *   <p>The JavaDoc files are located in the following path:
 *   <b>C482PA\src\main\java\com\example\c482pa\javadoc</b></p>
 *
 * @author Aimy Kohli
 *
 */
public class Main extends Application {

    /**
     * The start() method from the Application class
     * @param primaryStage The main stage
     * @throws Exception Required exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/views/LoginScreen.fxml"))));
        primaryStage.setTitle("Appointments");
        if(Locale.getDefault().getLanguage().equals("fr")){
            primaryStage.setTitle("Nominations");
        }
        primaryStage.show();

        //Confirmation box will be displayed if a user wants to exit the application
        primaryStage.setOnCloseRequest( evt -> {
            boolean userChoice = false;

            if(Locale.getDefault().getLanguage().equals("fr")) {
                userChoice = ConfirmBox.display("Confirmer la sortie", "Êtes-vous sûr " +
                        "de vouloir quitter l’application?");
            }
            else{
                userChoice = ConfirmBox.display("Confirm Exit", "Are you sure you want to exit" +
                        " the application?");
            }

            //If the user clicks the 'Yes' button
            if(userChoice){
                System.exit(0);
            }
            //If the user clicks the 'No' button
            else {
                evt.consume();
            }
        });
    }

    /**
     *
     * @param args CMD arguments
     */
    public static void main(String[] args) throws Exception {
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}
