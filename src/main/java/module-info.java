module com.example.c195pa {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;

    opens com.example.c195pa.main to javafx.graphics;
    opens com.example.c195pa.controllers to javafx.fxml;
    opens com.example.c195pa.model to javafx.base;
    opens com.example.c195pa.utils to javafx.graphics;

}
