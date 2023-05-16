package com.example.c195pa.controllers;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

/**
 * The class functions as a simple alert box.
 *
 * @author Aimy Kohli
 */
public class AlertBox {

    /**
     *
     * @param title The title for the window
     * @param message The message to be displayed
     */
    public static void display(String title, String message){
        Stage window = new Stage();

        //Can't do anything w/ previous window until this one is closed
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(400);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Close the window");
        closeButton.setOnAction(event -> {
            window.close();
        });

        //Layout
        VBox layout = new VBox();
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

    }
}
