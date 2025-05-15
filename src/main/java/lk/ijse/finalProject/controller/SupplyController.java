package lk.ijse.finalProject.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.finalProject.dto.OrderItemDto;
import lk.ijse.finalProject.dto.SupplyDto;
import lk.ijse.finalProject.dto.tm.SupplyTM;
import lk.ijse.finalProject.model.SupplyModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplyController implements Initializable {

    public AnchorPane ancSupplyContainer;
    public Label overViewLabelButton;
    public Label supplyIdLabel;
    public ComboBox cmbSupplierId1;
    public Button btnReset;
    public ComboBox cmbMaterialId1;
    public Label lblMaterialName1;
    public Button btnUpdate;
    public TextField txtAmount;
    public Button btnDelete;
    public TextField txtQuantity;
    public Button btnSave;
    public TableView<SupplyTM> tblSupply;
    public TableColumn<SupplyTM, String> colSupplyId;
    public TableColumn<SupplyTM, String> colSupplierId;
    public TableColumn<SupplyTM, String> colMaterialId;
    public TableColumn<SupplyTM, String> colAmount;
    public TableColumn<SupplyTM, String> colQuantity;
    public Label lblSupplierName11;

    private final SupplyModel supplyModel = new SupplyModel();
    private final String quantityRegex = "^\\d+$";
    private final String amountRegex = "^\\d+(\\.\\d{2})?$";

    public void labelOverViewClickOnAction(MouseEvent mouseEvent) {
        navigateTo("/view/OverView.fxml");
    }

    public void resetBtnOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    public void updateBtnOnAction(ActionEvent actionEvent) {
        String supplyId = supplyIdLabel.getText();
        String supplierId = (String) cmbSupplierId1.getSelectionModel().getSelectedItem();
        String materialId = (String) cmbMaterialId1.getSelectionModel().getSelectedItem();
        int quantity = Integer.parseInt(txtQuantity.getText());
        double amount = Double.parseDouble(txtAmount.getText());

        if(supplierId.isEmpty() || materialId.isEmpty() || txtQuantity.getText().isEmpty() || txtAmount.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Please fill all the fields..!").show();
            return;
        }

        SupplyDto supplyDto = new SupplyDto(
                supplyId,
                supplierId,
                materialId,
                amount,
                quantity
        );

        try {
            boolean isUpdated = supplyModel.updateSupply(supplyDto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Supply updated successfully..!").show();
                loadSupplyTableData();
                resetPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update Supply..!").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to update Supply..!").show();
        }

    }

    public void deleteBtnOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this Supply?",
                ButtonType.YES,
                ButtonType.NO
        );
        alert.setTitle("Confirmation");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            String supplyId = supplyIdLabel.getText();
            try {
                boolean isDeleted = supplyModel.deleteSupply(supplyId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Supply deleted successfully..!").show();
                    loadSupplyTableData();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete Supply..!").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to delete Supply..!").show();
            }
        }
    }

    public void saveBtnOnAction(ActionEvent actionEvent) {

        String supplyId = supplyIdLabel.getText();
        String supplierId = (String) cmbSupplierId1.getSelectionModel().getSelectedItem();
        String materialId = (String) cmbMaterialId1.getSelectionModel().getSelectedItem();
        int quantity = Integer.parseInt(txtQuantity.getText());
        double amount = Double.parseDouble(txtAmount.getText());

        if(supplierId.isEmpty() || materialId.isEmpty() || txtQuantity.getText().isEmpty() || txtAmount.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Please fill all the fields..!").show();
            return;
        }

        txtAmount.setStyle(txtAmount.getStyle()+";-fx-border-color: #7367F0");
        txtQuantity.setStyle(txtQuantity.getStyle()+";-fx-border-color: #7367F0");

        boolean isValidAmount = txtAmount.getText().matches(amountRegex);
        boolean isValidQuantity = txtQuantity.getText().matches(quantityRegex);

        if (!isValidAmount) {
            txtAmount.setStyle(txtAmount.getStyle() + ";-fx-border-color: red");
            new Alert(Alert.AlertType.ERROR, "Invalid Amount..!").show();
            return;
        }

        if (!isValidQuantity) {
            txtQuantity.setStyle(txtQuantity.getStyle() + ";-fx-border-color: red");
            new Alert(Alert.AlertType.ERROR, "Invalid Quantity..!").show();
            return;
        }

        SupplyDto supplyDto = new SupplyDto(
                supplyId,
                supplierId,
                materialId,
                amount,
                quantity
        );

        if (isValidAmount && isValidQuantity){
            try {
                boolean isSaved = supplyModel.saveSupply(supplyDto);
                if (isSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "Supply saved successfully..!").show();
                    loadSupplyTableData();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to save Supply..!").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to save Supply..!").show();
            }
        }
    }

    public void onClickTable(MouseEvent mouseEvent) {
        SupplyTM selectedItem = tblSupply.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            supplyIdLabel.setText(selectedItem.getSupply_id());
            cmbSupplierId1.getSelectionModel().select(selectedItem.getSupplier_id());
            cmbMaterialId1.getSelectionModel().select(selectedItem.getMaterial_id());
            txtQuantity.setText(String.valueOf(selectedItem.getQuantity()));
            txtAmount.setText(String.valueOf(selectedItem.getAmount()));

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
        colSupplyId.setCellValueFactory(new PropertyValueFactory<>("supply_id"));
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplier_id"));
        colMaterialId.setCellValueFactory(new PropertyValueFactory<>("material_id"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        try {
            cmbSupplierId1.setItems(FXCollections.observableArrayList(supplyModel.getAllSupplierIds()));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Supplier IDs..!").show();
        }

        try {
            cmbMaterialId1.setItems(FXCollections.observableArrayList(supplyModel.getAllMaterialIds()));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Material IDs..!").show();
        }

        try {
            loadSupplyTableData();
            loadNextId();
            loadSupplierIds();
            loadMaterialIds();

            cmbSupplierId1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    try {
                        displaySupplierName((String) newValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.ERROR, "Failed to load Supplier Name..!").show();
                    }
                }
            });

            cmbMaterialId1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    try {
                        displayMaterialName((String) newValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.ERROR, "Failed to load Material Name..!").show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Supply Table Data..!").show();
        }
    }

    private void loadNextId() {
        try {
            String nextSupplyId = supplyModel.getNextSupplyId();
            supplyIdLabel.setText(nextSupplyId);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Order Item IDs..!").show();
        }
    }

    private void loadSupplyTableData() throws SQLException, ClassNotFoundException {
        tblSupply.setItems(FXCollections.observableArrayList(
                supplyModel.getAllSupply()
                        .stream()
                        .map(supplyDto -> new SupplyTM(
                                supplyDto.getSupply_id(),
                                supplyDto.getSupplier_id(),
                                supplyDto.getMaterial_id(),
                                supplyDto.getAmount(),
                                supplyDto.getQuantity()
                        ))
                        .toList()
        ));

    }

    private void navigateTo(String path) {
        try {
            ancSupplyContainer.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancSupplyContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancSupplyContainer.heightProperty());

            ancSupplyContainer.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }

    //load supplier ids (forieng keys)
    private void loadSupplierIds() throws SQLException, ClassNotFoundException {
        cmbSupplierId1.setItems(FXCollections.observableArrayList(supplyModel.getAllSupplierIds()));
    }

    private void loadMaterialIds() throws SQLException, ClassNotFoundException {
        cmbMaterialId1.setItems(FXCollections.observableArrayList(supplyModel.getAllMaterialIds()));
    }

    private void displaySupplierName(String supplierId) throws SQLException, ClassNotFoundException {
        try {
            String item = supplyModel.getSupplierNameById(supplierId);
            lblSupplierName11.setText(item);
        }catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Item Names..!").show();
        }
    }

    private void displayMaterialName(String materialId) throws SQLException, ClassNotFoundException {
        try {
            String item = supplyModel.getMaterialNameById(materialId);
            lblMaterialName1.setText(item);
        }catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Item Names..!").show();
        }
    }

    private void resetPage(){
        try {
            loadSupplyTableData();
            loadNextId();

            //save button(id) -> enable
            btnSave.setDisable(false);
            //update button(id) -> disable
            btnUpdate.setDisable(true);
            //delete button(id) -> disable
            btnDelete.setDisable(true);

            cmbSupplierId1.getSelectionModel().clearSelection();
            cmbMaterialId1.getSelectionModel().clearSelection();
            txtQuantity.setText("");
            lblSupplierName11.setText("");
            lblMaterialName1.setText("");
            txtAmount.setText("");



        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to Reset").show();

        }
    }
}
