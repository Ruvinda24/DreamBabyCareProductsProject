module lk.ijse.finalProject {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires lombok;

    opens lk.ijse.finalProject.dto.tm to javafx.base;
    opens lk.ijse.finalProject to javafx.graphics;
    opens lk.ijse.finalProject.controller to javafx.fxml;


    exports lk.ijse.finalProject;
    exports lk.ijse.finalProject.controller;

}