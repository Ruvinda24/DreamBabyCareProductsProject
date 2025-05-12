package lk.ijse.finalProject.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.finalProject.dto.CustomerDto;
import lk.ijse.finalProject.dto.tm.CustomerTM;
import lk.ijse.finalProject.model.CustomerModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    public Label overViewLabelButton;
    public Label customerIdLabel;
    public TextField txtName;
    public Button btnReset;
    public TextField txtNumber;
    public Button btnUpdate;
    public TextField txtAddress;
    public Button btnDelete;
    public ComboBox cmbOrderPlatform;
    public Button btnSave;

    private final CustomerModel customerModel = new CustomerModel();

    public TableColumn<CustomerTM, String> colId;
    public TableColumn<CustomerTM, String> colName;
    public TableColumn<CustomerTM, String> colNumber;
    public TableColumn<CustomerTM, String> colAddress;
    public TableColumn<CustomerTM, String> colOrderPlatform;
    public TableView<CustomerTM> tblCustomer;

    private final String namePattern = "^[A-Za-z ]+$";
    private final String numberPattern = "^[0-9]{10}$";
    private final String addressPattern = "^[A-Za-z0-9 ,./-]+$";
    ;

    public AnchorPane ancCustomerViewContainer;


    public void labelOverViewClickOnAction(MouseEvent mouseEvent) {
        navigateTo("/view/OverView.fxml");
    }

    public void resetBtnOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    public void updateBtnOnAction(ActionEvent actionEvent) {
        String customerId = customerIdLabel.getText();
        String name = txtName.getText();
        String number = txtNumber.getText();
        String address = txtAddress.getText();
        String orderPlatform = (String) cmbOrderPlatform.getValue();

        if (customerId.isEmpty() || name.isEmpty() || number.isEmpty() || address.isEmpty() || orderPlatform.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all the fields").show();
            return;
        }

        CustomerDto customerDto = new CustomerDto(
                customerId,
                name,
                number,
                address,
                orderPlatform
        );

        try {
            boolean isUpdated = customerModel.updateCustomers(customerDto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Customer updated successfully").show();
                resetPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update customer").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to update customer").show();
        }
    }

    public void deleteBtnOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this customer?",
                ButtonType.YES,
                ButtonType.NO
        );
        alert.setTitle("Confirmation");

        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES) {
            String customerId = customerIdLabel.getText();
            try {
                boolean isDeleted = customerModel.deleteCustomers(customerId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer deleted successfully").show();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete customer").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to delete customer").show();
            }
        }
    }

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



    public void loadCustomerTableDate() throws SQLException, ClassNotFoundException {

        tblCustomer.setItems(FXCollections.observableArrayList(
                customerModel.getAllCustomers()
                        .stream()
                        .map(customerDto -> new CustomerTM(
                                customerDto.getCustomerId(),
                                customerDto.getCustomerName(),
                                customerDto.getCustomerPhone(),
                                customerDto.getCustomerAddress(),
                                customerDto.getOrderPlatForm()
                        ))
                        .toList()
        ));
    }

    private void resetPage() {
        try {
            loadCustomerTableDate();
            loadNextId();

            //save button(id) -> enable
            btnSave.setDisable(false);
            //update button(id) -> disable
            btnUpdate.setDisable(true);
            //delete button(id) -> disable
            btnDelete.setDisable(true);

            txtName.setText("");
            txtNumber.setText("");
            txtAddress.setText("");
            cmbOrderPlatform.getSelectionModel().clearSelection();


        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load customers").show();

        }
    }

    public void onClickTable(MouseEvent mouseEvent) {
        CustomerTM selectedItem = tblCustomer.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            customerIdLabel.setText(selectedItem.getCustomerId());
            txtName.setText(selectedItem.getCustomerName());
            txtNumber.setText(selectedItem.getCustomerPhone());
            txtAddress.setText(selectedItem.getCustomerAddress());
            cmbOrderPlatform.setValue(selectedItem.getOrderPlatForm());

            // save button disable
            btnSave.setDisable(true);

            // update, delete button enable
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    private void navigateTo(String path) {
        try {
            ancCustomerViewContainer.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancCustomerViewContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancCustomerViewContainer.heightProperty());

            ancCustomerViewContainer.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colNumber.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        colOrderPlatform.setCellValueFactory(new PropertyValueFactory<>("orderPlatForm"));

        cmbOrderPlatform.setItems(FXCollections.observableArrayList("WhatsApp", "Facebook", "Instagram", "Direct Call", "Wholesale", "Online", "Retail Store"));
        try {
            loadCustomerTableDate();
            loadNextId();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load customers").show();
            ;
        }
    }

    private void loadNextId() {
        try {
            String nextId = customerModel.getNextCustomerId();
            customerIdLabel.setText(nextId);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load next customer ID").show();
        }
    }
}

