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
import lk.ijse.finalProject.dto.MaterialUsageDto;
import lk.ijse.finalProject.dto.tm.MaterialUsageTM;
import lk.ijse.finalProject.model.MaterialUsageModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class MaterialUsageController implements Initializable {

    public TextField searchField;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public TableView<MaterialUsageTM> tblMaterialUsage;
    public TableColumn<MaterialUsageTM, String> colUsageId;
    public TableColumn<MaterialUsageTM, String> colProductionId;
    public TableColumn<MaterialUsageTM, String> colMaterialId;
    public TableColumn<MaterialUsageTM, String> colQuantityUsed;
    public AnchorPane ancMaterialUsageContainer;
    public Label materialUsageIdLabel;
    public ComboBox cmbProductionId;
    public ComboBox cmbMaterialId;
    public TextField txtQuantityUsed;
    
    private final MaterialUsageModel materialUsageModel = new MaterialUsageModel();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colUsageId.setCellValueFactory(new PropertyValueFactory<>("usage_id"));
        colProductionId.setCellValueFactory(new PropertyValueFactory<>("production_id"));
        colMaterialId.setCellValueFactory(new PropertyValueFactory<>("material_id"));
        colQuantityUsed.setCellValueFactory(new PropertyValueFactory<>("quantity_used"));

        /*try {
            cmbProductionId.setItems(FXCollections.observableArrayList(materialUsageModel.getAllProductionIds()));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load production IDs").show();
        }
        
        try {
            cmbMaterialId.setItems(FXCollections.observableArrayList(materialUsageModel.getAllMaterialIds()));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load material IDs").show();
        }*/
        
        try {
            loadMaterialUsageTableData();
            loadNextMaterialUsageId();
            loadPaymentIds();
            loadMaterialIDs();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Material Usage").show();
        }
    }

    private void loadMaterialIDs() {
        try {
            cmbMaterialId.setItems(FXCollections.observableArrayList(materialUsageModel.getAllMaterialIds()));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load material IDs").show();
        }
    }

    private void loadPaymentIds() {
        try {
            cmbProductionId.setItems(FXCollections.observableArrayList(materialUsageModel.getAllProductionIds()));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load production IDs").show();
        }
    }

    private void loadNextMaterialUsageId() {
        try {
            String nextId = materialUsageModel.getNextMaterialUsageId();
            materialUsageIdLabel.setText(nextId);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load next Material Usage ID").show();
        }
    }

    private void loadMaterialUsageTableData() throws SQLException, ClassNotFoundException {
        tblMaterialUsage.setItems(FXCollections.observableArrayList(
                materialUsageModel.getAllMaterialUsage()
                        .stream()
                        .map(
                                materialUsageDto -> new MaterialUsageTM(
                                        materialUsageDto.getUsage_id(),
                                        materialUsageDto.getProduction_id(),
                                        materialUsageDto.getMaterial_id(),
                                        materialUsageDto.getQuantity_used()
                                ))
                        .toList()
        ));
    }


    public void labelOverViewClickOnAction(MouseEvent mouseEvent) {
        navigateTo("/view/OverView.fxml");
    }

    public void search(KeyEvent keyEvent) {
        String searchText = searchField.getText();
        if (searchText.isEmpty()) {
            try {
                loadMaterialUsageTableData();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to load Material Usage").show();
            }
        } else {
            try {
                tblMaterialUsage.setItems(FXCollections.observableArrayList(
                        materialUsageModel.searchMaterialUsage(searchText)
                                .stream()
                                .map(
                                        materialUsageDto -> new MaterialUsageTM(
                                                materialUsageDto.getUsage_id(),
                                                materialUsageDto.getProduction_id(),
                                                materialUsageDto.getMaterial_id(),
                                                materialUsageDto.getQuantity_used()
                                        ))
                                .toList()
                ));
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to search Material Usage").show();
            }
        }
    }

    public void saveBtnOnAction(ActionEvent actionEvent) {
        String usageId = materialUsageIdLabel.getText();
        String productionId = cmbProductionId.getValue().toString();
        String materialId = cmbMaterialId.getValue().toString();
        int quantityUsed;

        try {
            quantityUsed = Integer.parseInt(txtQuantityUsed.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid quantity used").show();
            return;
        }
        if (usageId.isEmpty() || productionId.isEmpty() || materialId.isEmpty() || txtQuantityUsed.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "All fields must be filled").show();
            return;
        }

        MaterialUsageDto materialUsageDto = new MaterialUsageDto(
                usageId,
                productionId,
                materialId,
                quantityUsed
        );


        try {
            boolean isSaved = materialUsageModel.saveMaterialUsage(materialUsageDto);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Material Usage saved successfully!").show();
                loadMaterialUsageTableData();
                loadNextMaterialUsageId();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save Material Usage").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to save Material Usage").show();
        }
    }

    public void updateBtnOnAction(ActionEvent actionEvent) {
        String usageId = materialUsageIdLabel.getText();
        String productionId = cmbProductionId.getValue().toString();
        String materialId = cmbMaterialId.getValue().toString();
        int quantityUsed;

        try {
            quantityUsed = Integer.parseInt(txtQuantityUsed.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid quantity used").show();
            return;
        }
        if (usageId.isEmpty() || productionId.isEmpty() || materialId.isEmpty() || txtQuantityUsed.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "All fields must be filled").show();
            return;
        }

        MaterialUsageDto materialUsageDto = new MaterialUsageDto(
                usageId,
                productionId,
                materialId,
                quantityUsed
        );

        try {
            boolean isUpdated = materialUsageModel.updateMaterialUsage(materialUsageDto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Material Usage updated successfully!").show();
                loadMaterialUsageTableData();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update Material Usage").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to update Material Usage").show();
        }
    }

    public void deleteBtnOnAction(ActionEvent actionEvent) {
        Alert alert =new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this Material Usage?",
                ButtonType.YES,
                ButtonType.NO);
        alert.setTitle("Confirm Delete");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            String usageId = materialUsageIdLabel.getText();
            try {
                boolean isDeleted = materialUsageModel.deleteMaterialUsage(usageId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Material Usage deleted successfully!").show();
                    loadMaterialUsageTableData();
                    loadNextMaterialUsageId();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete Material Usage").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to delete Material Usage").show();
            }
        }
    }

    public void resetBtnOnAction(ActionEvent actionEvent) {
        materialUsageIdLabel.setText("");
        cmbProductionId.getSelectionModel().clearSelection();
        cmbMaterialId.getSelectionModel().clearSelection();
        txtQuantityUsed.clear();
        searchField.clear();

        try {
            loadNextMaterialUsageId();
            loadMaterialUsageTableData();

            materialUsageIdLabel.setText("");
            cmbProductionId.getSelectionModel().clearSelection();
            cmbMaterialId.getSelectionModel().clearSelection();
            txtQuantityUsed.clear();
            searchField.clear();
            btnSave.setDisable(false);
            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to reset Material Usage").show();
        }
    }

    public void onClickTable(MouseEvent mouseEvent) {
        MaterialUsageTM selectedItem = tblMaterialUsage.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            materialUsageIdLabel.setText(selectedItem.getUsage_id());
            cmbProductionId.setValue(selectedItem.getProduction_id());
            cmbMaterialId.setValue(selectedItem.getMaterial_id());
            txtQuantityUsed.setText(String.valueOf(selectedItem.getQuantity_used()));

            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    public void goToAddProductionLabel(MouseEvent mouseEvent) {
        navigateTo("/view/ProductionView.fxml");
    }

    public void goToAddMaterialLabel(MouseEvent mouseEvent) {
        navigateTo("/view/MaterialView.fxml");
    }


    private void navigateTo(String path) {
        try {
            ancMaterialUsageContainer.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancMaterialUsageContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancMaterialUsageContainer.heightProperty());

            ancMaterialUsageContainer.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }
}
