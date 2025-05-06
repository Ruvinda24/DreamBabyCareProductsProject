package lk.ijse.finalProject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.finalProject.dto.UserDto;
import lk.ijse.finalProject.model.UserModel;
import lk.ijse.finalProject.util.CrudUtil;

import java.sql.ResultSet;

public class SignUpPageController {


    public TextField signupNameField;
    public TextField signupEmailField;
    public TextField signupNumberField;
    public TextField signupUserNameField;
    public PasswordField signupPasswordField;
    public PasswordField signupReEnterPasswordField;
    public Button btnSignUp;

    private final String namePattern = "^[A-Za-z ]+$";
    private final String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private final String phonePattern = "\\d{10}";
    private final String userNamePattern = "^[A-Za-z0-9_]{3,}$";
    private final String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$";



    public void initialize() {
        // Add listeners to validate fields in real-time
        signupNameField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        signupEmailField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        signupNumberField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        signupUserNameField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        signupPasswordField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        signupReEnterPasswordField.textProperty().addListener((observable, oldValue, newValue) -> validateFields());

        // Disable sign-up button initially
        btnSignUp.setDisable(true);
    }

    private void validateFields() {
        boolean isValidName = signupNameField.getText().matches(namePattern);
        boolean isValidEmail = signupEmailField.getText().matches(emailPattern);
        boolean isValidPhone = signupNumberField.getText().matches(phonePattern);
        boolean isValidUserName = signupUserNameField.getText().matches(userNamePattern);
        boolean isValidPassword = signupPasswordField.getText().matches(passwordPattern);
        boolean isPasswordMatch = signupPasswordField.getText().equals(signupReEnterPasswordField.getText());

        // Reset styles with border radius
        signupNameField.setStyle("-fx-border-color: #7367F0; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        signupEmailField.setStyle("-fx-border-color: #7367F0; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        signupNumberField.setStyle("-fx-border-color: #7367F0; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        signupUserNameField.setStyle("-fx-border-color: #7367F0; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        signupPasswordField.setStyle("-fx-border-color: #7367F0; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        signupReEnterPasswordField.setStyle("-fx-border-color: #7367F0; -fx-border-radius: 12px; -fx-background-radius: 12px;");

        // Apply red border if invalid
        if (!isValidName) signupNameField.setStyle("-fx-border-color: red; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        if (!isValidEmail) signupEmailField.setStyle("-fx-border-color: red; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        if (!isValidPhone) signupNumberField.setStyle("-fx-border-color: red; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        if (!isValidUserName) signupUserNameField.setStyle("-fx-border-color: red; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        if (!isValidPassword) signupPasswordField.setStyle("-fx-border-color: red; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        if (!isPasswordMatch) signupReEnterPasswordField.setStyle("-fx-border-color: red; -fx-border-radius: 12px; -fx-background-radius: 12px;");

        // Enable or disable sign-up button
        btnSignUp.setDisable(!(isValidName && isValidEmail && isValidPhone && isValidUserName && isValidPassword && isPasswordMatch));
    }


    public void btnGoBackToLoginOnAction(ActionEvent actionEvent) {
        try {
            Parent dashBoardRoot = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
            Stage dashBoardStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            dashBoardStage.setScene(new Scene(dashBoardRoot));
            dashBoardStage.setTitle("Dream Baby Care Products");
            dashBoardStage.setResizable(true);
            dashBoardStage.show();
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load to Login page").show();
        }
    }

    public void btnSignUpOnAction(ActionEvent actionEvent) {
        String inputName = signupNameField.getText();
        String inputEmail = signupEmailField.getText();
        String inputNumber = signupNumberField.getText();
        String inputUserName = signupUserNameField.getText();
        String inputPassword = signupPasswordField.getText();
        String inputReEnterPassword = signupReEnterPasswordField.getText();

        // Validate empty fields
        if (inputName.isEmpty() || inputEmail.isEmpty() || inputNumber.isEmpty() || inputUserName.isEmpty() || inputPassword.isEmpty() || inputReEnterPassword.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all the fields").show();
            return;
        }

        // Validate email format
        if (!inputEmail.matches(emailPattern)) {
            new Alert(Alert.AlertType.ERROR, "Invalid email format").show();
            return;
        }

        // Validate phone number format
        if (!inputNumber.matches(phonePattern)) {
            new Alert(Alert.AlertType.ERROR, "Invalid phone number. It should contain 10 digits").show();
            return;
        }

        // Validate password strength
        if (!inputPassword.matches(passwordPattern)) {
            new Alert(Alert.AlertType.ERROR, "Password must be at least 8 characters long, contain an uppercase letter, a lowercase letter, and a digit").show();
            return;
        }

        // Validate password match
        if (!inputPassword.equals(inputReEnterPassword)) {
            new Alert(Alert.AlertType.ERROR, "Passwords do not match").show();
            return;
        }

        try {
            // Check for duplicate username or email in the database
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM user WHERE user_name = ? OR email = ?", inputUserName, inputEmail);
            if (resultSet.next()) {
                new Alert(Alert.AlertType.ERROR, "Username or email already exists").show();
                return;
            }

            // Generate a new user ID
            String userId = UserModel.getNextUserId();

            // Save the user to the database
            boolean isSaved = UserModel.saveUsers(new UserDto(userId, inputUserName, inputPassword, inputEmail, inputName, inputNumber));

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Sign up successful!").show();
                btnGoBackToLoginOnAction(actionEvent);
            } else {
                new Alert(Alert.AlertType.ERROR, "Sign up failed. Please try again.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Sign up failed. Database Connection Error").show();
        }
    }
}
