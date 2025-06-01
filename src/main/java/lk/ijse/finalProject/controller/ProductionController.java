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
import lk.ijse.finalProject.dto.ProductionDto;
import lk.ijse.finalProject.dto.tm.ProductionTM;
import lk.ijse.finalProject.dto.tm.ProductionTaskTM;
import lk.ijse.finalProject.dto.tm.TaskTM;
import lk.ijse.finalProject.model.ProductionModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductionController implements Initializable {


    public AnchorPane ancProductionContainer;
    public TextField searchField;
    public Label productionIdLabel;
    public ComboBox cmbInventoryID;
    public TextField txtDescription;
    public ComboBox cmbStatus;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public TableView<ProductionTM> tblProduction;
    public TableColumn<ProductionTM,String> colProductionId;
    public TableColumn<ProductionTM,String> colInventoryId;
    public TableColumn<ProductionTM,String> colDescription;
    public TableColumn<ProductionTM,String> colStatus;

    private final ProductionModel productionModel = new ProductionModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the production table columns
        colProductionId.setCellValueFactory(new PropertyValueFactory<>("production_id"));
        colInventoryId.setCellValueFactory(new PropertyValueFactory<>("inventory_id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        try {
            cmbInventoryID.setItems(FXCollections.observableArrayList(productionModel.getAllInventoryIds()));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load inventory IDs").show();
        }

        try {
            cmbStatus.setItems(FXCollections.observableArrayList("Pending", "In Progress", "Completed"));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load status options").show();
        }

        try {
            loadProductionTableData();
            loadNextId();
            loadInventoryId();
            loadStatusOptions();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Initialize").show();
        }

    }

    private void loadProductionTableData() throws SQLException, ClassNotFoundException {
        tblProduction.setItems(FXCollections.observableArrayList(
                productionModel.getAllProductions()
                        .stream()
                        .map(
                                productionTM -> new ProductionTM(
                                        productionTM.getProduction_id(),
                                        productionTM.getInventory_id(),
                                        productionTM.getDescription(),
                                        productionTM.getStatus()
                                )).toList()
        ));
    }

    public void labelOverViewClickOnAction(MouseEvent mouseEvent) {
        navigateTo("/view/OverView.fxml");
    }

    public void search(KeyEvent keyEvent) {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            try {
                loadProductionTableData();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to load productions").show();
            }
            return;
        }

        try {
            tblProduction.setItems(FXCollections.observableArrayList(
                    productionModel.searchProductions(searchText)
                            .stream()
                            .map(
                                    productionTM -> new ProductionTM(
                                            productionTM.getProduction_id(),
                                            productionTM.getInventory_id(),
                                            productionTM.getDescription(),
                                            productionTM.getStatus()
                                    )).toList()
            ));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to search productions").show();
        }
    }

    public void goToAddInventoryPage(MouseEvent mouseEvent) {
        navigateTo("/view/InventoryView.fxml");
    }

    public void saveBtnOnAction(ActionEvent actionEvent) {
        String productionId = productionIdLabel.getText();
        String inventoryId = cmbInventoryID.getSelectionModel().getSelectedItem().toString();
        String description = txtDescription.getText();
        String status = cmbStatus.getSelectionModel().getSelectedItem().toString();

        if (productionId.isEmpty() || inventoryId.isEmpty() || description.isEmpty() || status.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields").show();
            return;
        }

        ProductionDto newProduction = new ProductionDto(
                productionId,
                inventoryId,
                description,
                status);

        try {
            boolean isSaved = productionModel.saveProductions(newProduction);
            if (!isSaved) {
                new Alert(Alert.AlertType.ERROR, "Failed to save production").show();
                return;
            } else {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Production saved successfully").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to save production").show();
        }
    }

    public void updateBtnOnAction(ActionEvent actionEvent) {
        String productionId = productionIdLabel.getText();
        String inventoryId = cmbInventoryID.getSelectionModel().getSelectedItem().toString();
        String description = txtDescription.getText();
        String status = cmbStatus.getSelectionModel().getSelectedItem().toString();

        if (productionId.isEmpty() || inventoryId.isEmpty() || description.isEmpty() || status.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields").show();
            return;
        }

            ProductionDto updatedProduction = new ProductionDto(
                    productionId,
                    inventoryId,
                    description,
                    status);

            try {
                boolean isUpdated = productionModel.updateProductions(updatedProduction);
                if (!isUpdated) {
                    new Alert(Alert.AlertType.ERROR, "Failed to update production").show();
                    return;
                } else{
                    resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Production updated successfully").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to update production").show();
            }
    }

    public void deleteBtnOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this production?",
                ButtonType.YES,
                ButtonType.NO);

        alert.setTitle("Delete Confirmation");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            ProductionTM selectedProduction = tblProduction.getSelectionModel().getSelectedItem();
            if (selectedProduction != null) {
                try {
                    productionModel.deleteProductions(selectedProduction.getProduction_id());
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Production deleted successfully").show();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Failed to delete production").show();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "No production selected").show();
            }
        }
    }

    public void resetBtnOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    public void onClickTable(MouseEvent mouseEvent) {
        ProductionTM selectedProduction = tblProduction.getSelectionModel().getSelectedItem();
        if (selectedProduction != null) {
            productionIdLabel.setText(selectedProduction.getProduction_id());
            cmbInventoryID.getSelectionModel().select(selectedProduction.getInventory_id());
            txtDescription.setText(selectedProduction.getDescription());
            cmbStatus.getSelectionModel().select(selectedProduction.getStatus());

            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    private void navigateTo(String path) {
        try {
            ancProductionContainer.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancProductionContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancProductionContainer.heightProperty());

            ancProductionContainer.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }

    private void loadTaskTableData() throws SQLException, ClassNotFoundException {
        tblProduction.setItems(FXCollections.observableArrayList(
                productionModel.getAllProductions()
                        .stream()
                        .map(
                                productionTM -> new ProductionTM(
                                        productionTM.getProduction_id(),
                                        productionTM.getInventory_id(),
                                        productionTM.getDescription(),
                                        productionTM.getStatus()
                                )).toList()
        ));
    }

    private void loadNextId() {
        try {
            String nextId = productionModel.getNextProductionId();
            productionIdLabel.setText(nextId);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load next task ID").show();
        }
    }

    private void loadInventoryId(){
        try {
            cmbInventoryID.setItems(FXCollections.observableArrayList(productionModel.getAllInventoryIds()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetPage() {
        try {
            loadTaskTableData();
            loadNextId();

            productionIdLabel.setText("");
            cmbInventoryID.getSelectionModel().clearSelection();
            txtDescription.setText("");
            cmbStatus.getSelectionModel().clearSelection();
            btnSave.setDisable(false);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to reset page").show();
        }
    }

    private void loadStatusOptions() {
        try {
            cmbStatus.setItems(FXCollections.observableArrayList("Pending", "In Progress", "Completed"));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load status options").show();
        }
    }
}
