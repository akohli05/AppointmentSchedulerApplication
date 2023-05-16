package com.example.c195pa.controllers;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

/**
 * This class functions as a confirmation box.
 *
 * @author Aimy Kohli
 */
public class ConfirmBox {

    //Boolean to store answer
    static boolean answer;

    /**
     *
     * @param title The title for the window
     * @param message The message to be displayed
     * @return Returns a boolean true if the yes button is clicked, if no button is clicked than false
     */
    public static boolean display(String title, String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(400);
        Label label = new Label();
        label.setText(message);

        //Create two buttons
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        //If the yes button is clicked
        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });

        //if the no button is clicked
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        //layout
        VBox layout = new VBox();
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
