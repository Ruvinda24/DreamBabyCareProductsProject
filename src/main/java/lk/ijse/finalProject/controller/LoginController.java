package lk.ijse.finalProject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.finalProject.util.CrudUtil;
import javafx.application.Platform;


import java.sql.ResultSet;

public class LoginController {

    public TextField userNameField;
    public PasswordField passwordField;
    public Button loginButton;
    @FXML
    private AnchorPane loginAnc;

    private final String userNamePattern = "^[A-Za-z0-9_]{3,}$";
    private final String passwordPattern = "^[A-Za-z0-9@#$%^&+=]{6,}$";


    public void initialize() {
        // Add listeners to validate fields in real-time
        userNameField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());

        // Disable login button initially
        loginButton.setDisable(true);
    }

    private void validateFields() {
        boolean isValidUsername = userNameField.getText().matches(userNamePattern);
        boolean isValidPassword = passwordField.getText().matches(passwordPattern);

        // Reset styles
        userNameField.setStyle("-fx-border-color: #7367F0; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        passwordField.setStyle("-fx-border-color: #7367F0; -fx-border-radius: 12px; -fx-background-radius: 12px;");

        // Apply red border if invalid
        if (!isValidUsername) userNameField.setStyle("-fx-border-color: red; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        if (!isValidPassword) passwordField.setStyle("-fx-border-color: red; -fx-border-radius: 12px; -fx-background-radius: 12px;");

        // Enable or disable login button
        loginButton.setDisable(!(isValidUsername && isValidPassword));
    }

    public void visitDashboardOnAction(ActionEvent actionEvent) {
        String inputUserName = userNameField.getText();
        String inputPassword = passwordField.getText();
        // Here you would typically check the credentials against a database or other data source

        if(inputUserName.isEmpty() || inputPassword.isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Please enter username and password").show();
            return;
        }

        try{
            ResultSet resultSet = CrudUtil.execute(
                    "SELECT * FROM user WHERE user_name = ? AND password = ?",
                    inputUserName, inputPassword);
            if(resultSet.next()){
                try {
                    /*Parent dashBoardRoot = FXMLLoader.load(getClass().getResource("/view/DashBoardView.fxml"));
                    Stage dashBoardStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    Stage dashBoardStage = (Stage) this.loginAnc.getScene().getWindow();
                    dashBoardStage.setScene(new Scene(dashBoardRoot));
                    dashBoardStage.setTitle("Dream Baby Care Products");
                    dashBoardStage.setResizable(true);
                    // Fix: Delay maximization until after rendering
                    Platform.runLater(() -> {
                        dashBoardStage.setMaximized(true);
                    });
                    dashBoardStage.show();*/
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardView.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
                    stage.setScene(scene);
                    stage.setMaximized(true);
                    stage.setTitle("Dream Baby Care Products");
                    stage.show();
                }catch (Exception e){
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Failed to load to dash board").show();
                }
            }else {
                new Alert(Alert.AlertType.ERROR, "Invalid username or password").show();
            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Login failed. Database Connection Error").show();

        }
    }

    public void visitSignUpPageOnAction(ActionEvent actionEvent) {
        try {
            Parent dashBoardRoot = FXMLLoader.load(getClass().getResource("/view/SignUpView.fxml"));
            Stage dashBoardStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            dashBoardStage.setScene(new Scene(dashBoardRoot));
            dashBoardStage.setTitle("Dream Baby Care Products");
            dashBoardStage.setResizable(true);
            dashBoardStage.setMaximized(true); // Maximizes the window with window controls
            dashBoardStage.show();
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load to SignUp page").show();
        }
    }
}
