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
import lk.ijse.finalProject.dto.MaterialDto;
import lk.ijse.finalProject.dto.tm.MaterialTM;
import lk.ijse.finalProject.model.MaterialModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class MaterialController implements Initializable {
    public AnchorPane ancMaterialContainer;
    public TextField searchField;
    public Label materialIdLabel;
    public TextField txtName;
    public TextField txColorType;
    public TextField txtQuantity;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public TableView<MaterialTM> tblMaterial;
    public TableColumn<MaterialTM,String> colQuantity;
    public TableColumn<MaterialTM,String> colColorType;
    public TableColumn<MaterialTM,String> colName;
    public TableColumn<MaterialTM,String> colMaterialId;

    private final MaterialModel materialModel = new MaterialModel();


    public void labelOverViewClickOnAction(MouseEvent mouseEvent) {
        navigateTo("/view/OverView.fxml");
    }

    public void search(KeyEvent keyEvent) {
        String searchText = searchField.getText();
        if (searchText.isEmpty()) {
            try {
                loadMaterialsTableData();
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to load materials: " + e.getMessage()).show();
            }
        } else {
            try {
                tblMaterial.setItems(FXCollections.observableArrayList(
                        materialModel.searchMaterials(searchText)
                                .stream()
                                .map(
                                        material -> new MaterialTM(
                                                material.getMaterial_id(),
                                                material.getName(),
                                                material.getColor_type(),
                                                material.getQuantity()
                                        )).toList()
                ));
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to search materials: " + e.getMessage()).show();
            }
        }
    }

    public void saveBtnOnAction(ActionEvent actionEvent) {
        String materialId = materialIdLabel.getText();
        String name = txtName.getText();
        String colorType = txColorType.getText();
        String quantity = txtQuantity.getText();

        if (materialId.isEmpty() || name.isEmpty() || colorType.isEmpty() || quantity.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields.").show();
            return;
        }

        MaterialDto materialDto = new MaterialDto(
                materialId,
                name,
                colorType,
                Integer.parseInt(quantity)
        );

        try {
            boolean isSaved = materialModel.saveMaterials(materialDto);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Material saved successfully.").show();
                resetPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save material.").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error saving material: " + e.getMessage()).show();
        }
    }

    public void updateBtnOnAction(ActionEvent actionEvent) {
        String materialId = materialIdLabel.getText();
        String name = txtName.getText();
        String colorType = txColorType.getText();
        String quantity = txtQuantity.getText();

        if (materialId.isEmpty() || name.isEmpty() || colorType.isEmpty() || quantity.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields.").show();
            return;
        }

        MaterialDto materialDto = new MaterialDto(
                materialId,
                name,
                colorType,
                Integer.parseInt(quantity)
        );


        try {
            boolean isUpdated = materialModel.updateMaterials(materialDto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Material updated successfully.").show();
                resetPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update material.").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error updating material: " + e.getMessage()).show();
        }
    }

    public void deleteBtnOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this material?",
                ButtonType.YES,
                ButtonType.NO);
        alert.setTitle("Delete Material");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            String materialId = materialIdLabel.getText();
            if (materialId != null) {
                try {
                    boolean isDeleted = materialModel.deleteMaterials(materialId);
                    if (isDeleted) {
                        new Alert(Alert.AlertType.INFORMATION, "Material deleted successfully.").show();
                        resetPage();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to delete material.").show();
                    }
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Error deleting material: " + e.getMessage()).show();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select a material to delete.").show();
            }
        }
    }

    public void resetBtnOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    public void onClickTable(MouseEvent mouseEvent) {
        MaterialTM selectedMaterial = tblMaterial.getSelectionModel().getSelectedItem();

        if (selectedMaterial != null) {
            materialIdLabel.setText(selectedMaterial.getMaterial_id());
            txtName.setText(selectedMaterial.getName());
            txColorType.setText(selectedMaterial.getColor_type());
            txtQuantity.setText(String.valueOf(selectedMaterial.getQuantity()));
            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colMaterialId.setCellValueFactory(new PropertyValueFactory<>("material_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colColorType.setCellValueFactory(new PropertyValueFactory<>("color_type"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        try{
            loadMaterialsTableData();
            loadNextId();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load materials: " + e.getMessage()).show();
        }

    }

    private void loadNextId() {
        try {
            String nextId = materialModel.getNextMaterialId();
            materialIdLabel.setText(nextId);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load next material ID: " + e.getMessage()).show();
        }
    }

    private void loadMaterialsTableData() throws SQLException, ClassNotFoundException {
        tblMaterial.setItems(FXCollections.observableArrayList(
                materialModel.getAllMaterials()
                        .stream()
                        .map(
                                material -> new MaterialTM(
                                        material.getMaterial_id(),
                                        material.getName(),
                                        material.getColor_type(),
                                        material.getQuantity()
                                )).toList()
        ));
    }

    private void navigateTo(String path) {
        try {
            ancMaterialContainer.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancMaterialContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancMaterialContainer.heightProperty());

            ancMaterialContainer.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }

    private void resetPage() {
        try {
            loadMaterialsTableData();
            loadNextId();

            materialIdLabel.setText("");
            txtName.clear();
            txColorType.clear();
            txtQuantity.clear();
            btnSave.setDisable(false);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
            tblMaterial.getSelectionModel().clearSelection();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to reset page: " + e.getMessage()).show();
        }
    }

}
