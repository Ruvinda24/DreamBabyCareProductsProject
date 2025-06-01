package lk.ijse.finalProject.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.finalProject.dto.SupplierDto;
import lk.ijse.finalProject.dto.tm.SupplierTM;
import lk.ijse.finalProject.model.SupplierModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierController implements Initializable {


    public AnchorPane ancSupplierContainer;
    public TextField searchField;
    public Label supplierIdLabel;
    public TextField txtName;
    public TextField txtContact;
    public TextField txtAccountDetails;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public TableView<SupplierTM> tblSupplier;
    public TableColumn<SupplierTM, String> colAccountDetails;
    public TableColumn<SupplierTM, String> colContact;
    public TableColumn<SupplierTM, String> colName;
    public TableColumn<SupplierTM, String> colSupplierId;

    private final SupplierModel supplierModel = new SupplierModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplier_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAccountDetails.setCellValueFactory(new PropertyValueFactory<>("account_details"));

        try {
            loadSupplierTableData();
            loadNextId();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void loadNextId() {
        try {
            String nextId = supplierModel.getNextSupplierId();
            supplierIdLabel.setText(nextId);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load next supplier ID").show();
        }
    }

    private void loadSupplierTableData() throws SQLException, ClassNotFoundException {
        tblSupplier.setItems(FXCollections.observableArrayList(
                supplierModel.getAllSuppliers().stream()
                        .map(supplier -> new SupplierTM(
                                supplier.getSupplier_id(),
                                supplier.getName(),
                                supplier.getContact(),
                                supplier.getAccount_details()))
                        .toList()
        ));
    }

    public void labelOverViewClickOnAction(MouseEvent mouseEvent) {
        navigateTo("/view/OverView.fxml");
    }

    public void search(KeyEvent keyEvent) {
        String searchText = searchField.getText().toLowerCase();
        if (searchText.isEmpty()) {
            try {
                loadSupplierTableData();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to load suppliers").show();
            }
            return;
        }

        try {
            tblSupplier.setItems(FXCollections.observableArrayList(
                    supplierModel.searchSuppliers(searchText).stream()
                            .map(supplier -> new SupplierTM(
                                    supplier.getSupplier_id(),
                                    supplier.getName(),
                                    supplier.getContact(),
                                    supplier.getAccount_details()))
                            .toList()
            ));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to search suppliers").show();
        }
    }

    public void saveBtnOnAction(ActionEvent actionEvent) {
        String supplierId = supplierIdLabel.getText();
        String name = txtName.getText();
        String contact = txtContact.getText();
        String accountDetails = txtAccountDetails.getText();

        if (supplierId.isEmpty() || name.isEmpty() || contact.isEmpty() || accountDetails.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields").show();
            return;
        }

        SupplierDto supplierDto = new SupplierDto(
                supplierId,
                name,
                contact,
                accountDetails
        );

        try {
            boolean isSaved = supplierModel.saveSuppliers(supplierDto);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier saved successfully").show();
                resetPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save supplier").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while saving supplier").show();
        }
    }

    public void updateBtnOnAction(ActionEvent actionEvent) {
        String supplierId = txtName.getText();
        String name = txtName.getText();
        String contact = txtContact.getText();
        String accountDetails = txtAccountDetails.getText();

        if (supplierId.isEmpty() || name.isEmpty() || contact.isEmpty() || accountDetails.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields").show();
            return;
        }

        SupplierDto supplierDto = new SupplierDto(
                supplierId,
                name,
                contact,
                accountDetails
        );

        try {
            boolean isUpdated = supplierModel.updateSuppliers(supplierDto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier updated successfully").show();
                resetPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update supplier").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while updating supplier").show();
        }
    }

    public void deleteBtnOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this supplier?",
                ButtonType.YES,
                ButtonType.NO);
        alert.setTitle("Delete Confirmation");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            String selectedSupplier = supplierIdLabel.getText();
            if (selectedSupplier != null) {
                try {
                    boolean isDeleted = supplierModel.deleteSuppliers(selectedSupplier);
                    if (isDeleted) {
                        new Alert(Alert.AlertType.INFORMATION, "Supplier deleted successfully").show();
                        resetPage();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to delete supplier").show();
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Error occurred while deleting supplier").show();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select a supplier to delete").show();
            }
        }
    }

    public void resetBtnOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    public void onClickTable(MouseEvent mouseEvent) {
        SupplierTM selectedSupplier = tblSupplier.getSelectionModel().getSelectedItem();

        if (selectedSupplier != null) {
            supplierIdLabel.setText(selectedSupplier.getSupplier_id());
            txtName.setText(selectedSupplier.getName());
            txtContact.setText(selectedSupplier.getContact());
            txtAccountDetails.setText(selectedSupplier.getAccount_details());

            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    private void navigateTo(String path) {
        try {
            ancSupplierContainer.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancSupplierContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancSupplierContainer.heightProperty());

            ancSupplierContainer.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }

    private void resetPage() {
        try {
            loadSupplierTableData();
            loadNextId();

            txtName.clear();
            txtContact.clear();
            txtAccountDetails.clear();
            supplierIdLabel.setText("");
            btnSave.setDisable(false);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
            searchField.clear();
            tblSupplier.getSelectionModel().clearSelection();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to reset the page").show();
        }
    }
}