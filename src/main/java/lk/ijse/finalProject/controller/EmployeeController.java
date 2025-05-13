package lk.ijse.finalProject.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.finalProject.dto.EmployeeDto;
import lk.ijse.finalProject.dto.tm.EmployeeTM;
import lk.ijse.finalProject.model.EmployeeModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    public Label overViewLabelButton;
    public Label employeeIdLabel;
    public TextField txtName;
    public Button btnReset;
    public TextField txtNIC;
    public Button btnUpdate;
    public TextField txtNumber;
    public Button btnDelete;
    public ComboBox cmbRole;
    public Button btnSave;

    public TableView<EmployeeTM> tblEmployee;
    public TableColumn<EmployeeTM,String> colId;
    public TableColumn<EmployeeTM,String> colName;
    public TableColumn<EmployeeTM,String> colNIC;
    public TableColumn<EmployeeTM,String> colNumber;
    public TableColumn<EmployeeTM,String> colRole;

    public AnchorPane ancEmployeeViewContainer;

    private final EmployeeModel employeeModel = new EmployeeModel();

    private final String namePattern = "^[A-Za-z ]+$";
    private final String numberPattern = "^[0-9]{10}$";
    private final String nicPattern = "^[0-9]{9}[V]$";

    public void labelOverViewClickOnAction(MouseEvent mouseEvent) {
        navigateTo("/view/OverView.fxml");
    }

    public void resetBtnOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    public void updateBtnOnAction(ActionEvent actionEvent) {
        String employeeId = employeeIdLabel.getText();
        String name = txtName.getText();
        String nic = txtNIC.getText();
        String number = txtNumber.getText();
        String role = (String) cmbRole.getSelectionModel().getSelectedItem();

        if (employeeId.isEmpty() || name.isEmpty() || nic.isEmpty() || number.isEmpty() || role.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all the fields").show();
            return;
        }

        EmployeeDto employeeDto = new EmployeeDto(
                employeeId,
                name,
                nic,
                number,
                role
        );

        try {
            boolean isUpdated = employeeModel.updateEmployees(employeeDto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Employee updated successfully").show();
                resetPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update employee").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to update employee").show();
        }
    }

    public void deleteBtnOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this employee?",
                ButtonType.YES,
                ButtonType.NO
        );

        alert.setTitle("Confirmation");

        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES) {
            String employeeId = employeeIdLabel.getText();
            try {
                boolean isDeleted = employeeModel.deleteEmployees(employeeId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Employee deleted successfully").show();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete employee").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to delete employee").show();
            }
        }
    }

    public void saveBtnOnAction(ActionEvent actionEvent) {

        String employeeId = employeeIdLabel.getText();
        String name = txtName.getText();
        String nic = txtNIC.getText();
        String number = txtNumber.getText();
        String role = (String) cmbRole.getSelectionModel().getSelectedItem();

        boolean isNameValid = name.matches(namePattern);
        boolean isNumberValid = number.matches(numberPattern);
        boolean isNicValid = nic.matches(nicPattern);

        txtName.setStyle(txtName.getStyle() + ";-fx-border-color: #7367F0;");
        txtNIC.setStyle(txtNIC.getStyle() + ";-fx-border-color: #7367F0;");
        txtNumber.setStyle(txtNumber.getStyle() + ";-fx-border-color: #7367F0;");

        if (employeeId.isEmpty() || name.isEmpty() || nic.isEmpty() || number.isEmpty() || role.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all the fields").show();
            return;
        }

        if (!isNameValid) {
            txtName.setStyle(txtName.getStyle() + ";-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid name").show();
            return;
        }

        if (!isNicValid) {
            txtNIC.setStyle(txtNIC.getStyle() + ";-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid NIC").show();
            return;
        }

        if (!isNumberValid) {
            txtNumber.setStyle(txtNumber.getStyle() + ";-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid number").show();
            return;
        }

        if (cmbRole.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please select a role").show();
            return;
        }

        EmployeeDto employeeDto = new EmployeeDto(
                employeeId,
                name,
                nic,
                number,
                role
        );

        if (isNameValid && isNumberValid && isNicValid) {
            try {
                boolean isSaved = employeeModel.saveEmployees(employeeDto);
                if (isSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "Employee saved successfully").show();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to save employee").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to save employee").show();
            }
        }
    }

    public void onClickTable(MouseEvent mouseEvent) {
        EmployeeTM selectedEmployee = tblEmployee.getSelectionModel().getSelectedItem();

        if (selectedEmployee != null) {
            employeeIdLabel.setText(selectedEmployee.getEmployee_id());
            txtName.setText(selectedEmployee.getName());
            txtNIC.setText(selectedEmployee.getNic());
            txtNumber.setText(selectedEmployee.getNumber());
            cmbRole.getSelectionModel().select(selectedEmployee.getRole());

            //save button(id) -> disable
            btnSave.setDisable(true);
            //update button(id) -> enable
            btnUpdate.setDisable(false);
            //delete button(id) -> enable
            btnDelete.setDisable(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colId.setCellValueFactory(new PropertyValueFactory<>("employee_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        cmbRole.setItems(FXCollections.observableArrayList("Tailor", "Cutting Staff", "Packaging & Quality Control", "Inventory & Stock Manager ", "Supervisor"));
        try {
            loadEmployeeTableDate();
            loadNextId();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load employees").show();
        }
    }

    private void loadNextId() {
        try {
            String nextId = EmployeeModel.getNextEmployeeId();
            employeeIdLabel.setText(nextId);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load next Employee ID").show();
        }
    }

    private void loadEmployeeTableDate() throws SQLException, ClassNotFoundException {

        tblEmployee.setItems(FXCollections.observableArrayList(
                employeeModel.getAllEmployees()
                        .stream()
                        .map(employeeDto -> new EmployeeTM(
                                employeeDto.getEmployee_id(),
                                employeeDto.getName(),
                                employeeDto.getNic(),
                                employeeDto.getNumber(),
                                employeeDto.getRole()
                        ))
                        .toList()
        ));
        
    }

    private void navigateTo(String path) {
        try {
            ancEmployeeViewContainer.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancEmployeeViewContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancEmployeeViewContainer.heightProperty());

            ancEmployeeViewContainer.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }

    private void resetPage() {
        try {
            loadEmployeeTableDate();
            loadNextId();

            //save button(id) -> enable
            btnSave.setDisable(false);
            //update button(id) -> disable
            btnUpdate.setDisable(true);
            //delete button(id) -> disable
            btnDelete.setDisable(true);

            txtName.setText("");
            txtNIC.setText("");
            txtNumber.setText("");
            cmbRole.getSelectionModel().clearSelection();


        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load customers").show();

        }
    }

}
