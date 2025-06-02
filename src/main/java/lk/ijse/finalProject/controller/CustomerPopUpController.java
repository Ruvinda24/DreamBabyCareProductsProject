package lk.ijse.finalProject.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lk.ijse.finalProject.dto.CustomerDto;
import lk.ijse.finalProject.model.CustomerModel;

import java.sql.SQLException;

public class CustomerPopUpController {
    public AnchorPane ancCustomerViewContainer;
    public Label customerIdLabel;
    public TextField txtName;
    public TextField txtNumber;
    public TextField txtAddress;
    public ComboBox cmbOrderPlatform;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;

    private final String namePattern = "^[A-Za-z ]+$";
    private final String numberPattern = "^[0-9]{10}$";
    private final String addressPattern = "^[A-Za-z0-9 ,./-]+$";

    private final CustomerModel customerModel = new CustomerModel();

    public void saveBtnOnAction(ActionEvent actionEvent) {
        String customerId = customerIdLabel.getText();
        String name = txtName.getText();
        String number = txtNumber.getText();
        String address = txtAddress.getText();
        String orderPlatform = (String) cmbOrderPlatform.getValue();

        boolean isValidName = name.matches(namePattern);
        boolean isValidNumber = number.matches(numberPattern);
        boolean isValidAddress = address.matches(addressPattern);

        txtName.setStyle(txtName.getStyle() + ";-fx-border-color: #7367F0;");
        txtNumber.setStyle(txtNumber.getStyle() + ";-fx-border-color: #7367F0;");
        txtAddress.setStyle(txtAddress.getStyle() + ";-fx-border-color: #7367F0;");

        if (customerId.isEmpty() || name.isEmpty() || number.isEmpty() || address.isEmpty() || orderPlatform.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all the fields").show();
            return;
        }

        if (!isValidName) {
            txtName.setStyle(txtName.getStyle() + ";-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid name format").show();
            return;
        }
        if (!isValidNumber) {
            txtNumber.setStyle(txtNumber.getStyle() + ";-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid number format").show();
            return;
        }
        if (!isValidAddress) {
            txtAddress.setStyle(txtAddress.getStyle() + ";-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid address format").show();
            return;
        }
        if (cmbOrderPlatform.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please select an order platform").show();
            return;
        }

        CustomerDto customerDto = new CustomerDto(
                customerId,
                name,
                number,
                address,
                orderPlatform
        );

        // Save the customer
        if (isValidName && isValidNumber && isValidAddress) {
            try {
                boolean isSaved = customerModel.saveCustomers(customerDto);
                if (isSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer saved successfully").show();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to save customer").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to save customer").show();
            }
        }
    }

    public void resetBtnOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    private void resetPage() {
        loadNextId();
        //customerIdLabel.setText("");
        txtName.clear();
        txtNumber.clear();
        txtAddress.clear();
        cmbOrderPlatform.getSelectionModel().clearSelection();
    }

    private void loadNextId() {
        try {
            String nextId = customerModel.getNextCustomerId();
            customerIdLabel.setText(nextId);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
