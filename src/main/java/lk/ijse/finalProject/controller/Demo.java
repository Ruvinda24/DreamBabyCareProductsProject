package lk.ijse.finalProject.controller;

import javafx.event.ActionEvent;

public class Demo {
    public void orderPlacementOnAction(ActionEvent actionEvent) {
    }

    public void productionPlacementOnAction(ActionEvent actionEvent) {
    }

    public void OverViewOnAction(ActionEvent actionEvent) {
    }
    /*public void btnSaveOnAction(ActionEvent actionEvent) {
        String customerId = lblCustomerId.getText();
        String name = txtName.getText();
        String nic = txtNic.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();

        boolean isValidName = name.matches(namePattern);
        boolean isValidNic = nic.matches(nicPattern);
        boolean isValidEmail = email.matches(emailPattern);
        boolean isValidPhone = phone.matches(phonePattern);

//        if (!isValidName) return;

        txtName.setStyle(txtName.getStyle() + ";-fx-border-color: #7367F0;");
        txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: #7367F0;");
        txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: #7367F0;");
        txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: #7367F0;");

        if (!isValidName) txtName.setStyle(txtName.getStyle() + ";-fx-border-color: red;");
        if (!isValidNic) txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: red;");
        if (!isValidEmail) txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: red;");
        if (!isValidPhone) txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: red;");


//        [A-Za-z ]+
//        1. Using Pattern object java.util.regex
//        Pattern namePattern = Pattern.compile("^[A-Za-z ]+$");
//        boolean isValidName = namePattern.matcher(name).matches();
//        System.out.println(name + " is valid: " + isValidName);

//        2. Using String class matches() method
//        boolean isValidName = name.matches("^[A-Za-z ]+$");
//        System.out.println(name + " is valid: " + isValidName);


        // Data transfer object -dto

        // Create dto object wrap data to single unit
        CustomerDTO customerDTO = new CustomerDTO(
                customerId,
                name,
                nic,
                email,
                phone
        );

        // Call CustomerModel inside saveCustomer
        // method and parse

        // controller to model parse data using dto
        if (isValidName && isValidNic && isValidEmail && isValidPhone) {
            try {
                boolean isSaved = customerModel.saveCustomer(customerDTO);

                if (isSaved) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Customer saved successfully.").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fail to save customer.").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to save customer.").show();
            }
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String customerId = lblCustomerId.getText();
        String name = txtName.getText();
        String nic = txtNic.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();

        CustomerDTO customerDTO = new CustomerDTO(
                customerId,
                name,
                nic,
                email,
                phone
        );

        try {
            boolean isUpdated = customerModel.updateCustomer(customerDTO);

            if (isUpdated) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Customer updated successfully.").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to update customer.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to update customer.").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure ?",
                ButtonType.YES,
                ButtonType.NO
        );

        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES) {
            // user with agree to delete data
            String customerId = lblCustomerId.getText();
            try {
                boolean isDeleted = customerModel.deleteCustomer(customerId);

                if (isDeleted) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Customer deleted successfully.").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fail to delete customer.").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to delete customer.").show();
            }
        }
    }
        public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("customer Id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colOrderPlatform.setCellValueFactory(new PropertyValueFactory<>("order platform"));
        try {
            loadCustomerTableDate();
            customerIdLabel.setText(customerModel.getNextCustomerId());
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load customers").show();
            ;
        }
    }
*/
}
