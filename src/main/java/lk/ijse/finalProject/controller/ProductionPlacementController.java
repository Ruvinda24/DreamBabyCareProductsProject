package lk.ijse.finalProject.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.finalProject.db.DBConnection;
import lk.ijse.finalProject.dto.MaterialUsageDto;
import lk.ijse.finalProject.dto.ProductionDto;
import lk.ijse.finalProject.dto.ProductionTaskDto;
import lk.ijse.finalProject.dto.TaskDto;
import lk.ijse.finalProject.dto.tm.ProductionCartTM;
import lk.ijse.finalProject.model.*;
import lk.ijse.finalProject.util.EmailUtil;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ProductionPlacementController implements Initializable {

    public AnchorPane ancProductionPlacementPage;
    public Label homeLabel;
    public Label lblProductionId;
    public Label productionPlacementDate;
    public ComboBox cmbEmployeeId;
    public Label lblEmployeeName;
    public Label labelPopUpCustomer;
    public Label lblEmployeeRole;
    public ComboBox cmbInventoryId;
    public Label lblItemName;
    public TextField txtProductionDescription;
    public TextField txtProductQty;
    public TextField txtTaskDescription;
    public TextField txtMaterialUsageQty;
    public Label lblMaterialName;
    public Label lblMaterialColorType;
    public ComboBox cmbMaterialId;
    public Label lblMaterialColorType11;
    public Label lblMaterialQty;
    public Button btnAddToCart;
    public TableView<ProductionCartTM> tblProductionPlacement;
    public TableColumn<ProductionCartTM, String> colEmployeeName;
    public TableColumn<ProductionCartTM, String> colItemName;
    public TableColumn<ProductionCartTM, String> colProductDescription;
    public TableColumn<ProductionCartTM, Integer> colQuantity;
    public TableColumn<ProductionCartTM, String> colTaskDescription;
    public TableColumn<ProductionCartTM, String> colMaterialName;
    public TableColumn<ProductionCartTM, Integer> colQtyNeeded;
    public TableColumn<ProductionCartTM, String> colColorType;
    public TableColumn<ProductionCartTM, Button> colAction;

    private final ProductionModel productionModel = new ProductionModel();
    private final EmployeeModel employeeModel = new EmployeeModel();
    private final InventoryModel inventoryModel = new InventoryModel();
    private final MaterialModel materialModel = new MaterialModel();
    private final MaterialUsageModel materialUsageModel = new MaterialUsageModel();
    private final TaskModel taskModel = new TaskModel();
    private final ProductionTaskModel productionTaskModel = new ProductionTaskModel();


    private void setCellValues(){
        colEmployeeName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colProductDescription.setCellValueFactory(new PropertyValueFactory<>("productDescription"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colTaskDescription.setCellValueFactory(new PropertyValueFactory<>("taskDescription"));
        colMaterialName.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        colQtyNeeded.setCellValueFactory(new PropertyValueFactory<>("qtyNeeded"));
        colColorType.setCellValueFactory(new PropertyValueFactory<>("colorType"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnAction"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setCellValues();
        try {
            refreshPage();
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
        }
        // Load employee IDs
        try {
            cmbEmployeeId.setItems(FXCollections.observableArrayList(employeeModel.getAllEmployeeIds()));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load employees..!").show();
        }

        // Load inventory IDs
        try {
            cmbInventoryId.setItems(FXCollections.observableArrayList(inventoryModel.getAllInventoryIds()));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load inventory items..!").show();
        }

        // Load material IDs
        try {
            cmbMaterialId.setItems(FXCollections.observableArrayList(materialModel.getAllMaterialIds()));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load materials..!").show();
        }

        // Employee selection listener
        cmbEmployeeId.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    lblEmployeeName.setText(employeeModel.getEmployeeNameById((String) newVal));
                    lblEmployeeRole.setText(employeeModel.getEmployeeRoleById((String) newVal));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Inventory selection listener
        cmbInventoryId.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    lblItemName.setText(inventoryModel.getItemNameById((String) newVal));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Material selection listener
        cmbMaterialId.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    lblMaterialName.setText(materialModel.getMaterialNameById((String) newVal));
                    lblMaterialColorType.setText(materialModel.getMaterialColorTypeById((String) newVal));
                    lblMaterialQty.setText(String.valueOf(materialModel.getMaterialQtyById((String) newVal)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void refreshPage() {
        // Clear all fields
        cmbEmployeeId.getSelectionModel().clearSelection();
        cmbInventoryId.getSelectionModel().clearSelection();
        cmbMaterialId.getSelectionModel().clearSelection();
        lblEmployeeName.setText("");
        lblEmployeeRole.setText("");
        lblItemName.setText("");
        txtProductionDescription.clear();
        txtProductQty.clear();
        txtTaskDescription.clear();
        txtMaterialUsageQty.clear();
        lblMaterialName.setText("");
        lblMaterialColorType.setText("");
        lblMaterialQty.setText("");

        // Clear the table
        tblProductionPlacement.getItems().clear();

        // Set new production ID and date
        try {
            lblProductionId.setText(productionModel.generateNewProductionId());
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to generate new production ID..!").show();
        }
        productionPlacementDate.setText(String.valueOf(LocalDate.now()));
    }

    public void goToDashBoardPage(MouseEvent mouseEvent) {
        navigateTo("/view/DashBoardView.fxml");
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
        refreshPage();
    }

    public void btnPlaceProductionOnAction(ActionEvent actionEvent) {
        java.sql.Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            // Prepare IDs and data
            String productionId = lblProductionId.getText();
            String inventoryId = (String) cmbInventoryId.getSelectionModel().getSelectedItem();
            String productionDescription = txtProductionDescription.getText();
            String productionStatus = "In Production";

            String employeeId = (String) cmbEmployeeId.getSelectionModel().getSelectedItem();
            String taskDescription = txtTaskDescription.getText();
            String taskStatus = "In Progress";

            String materialId = (String) cmbMaterialId.getSelectionModel().getSelectedItem();
            int materialUsageQty = Integer.parseInt(txtMaterialUsageQty.getText());
            int productQty = Integer.parseInt(txtProductQty.getText());

            String productionTaskId = productionTaskModel.getNextProductionTaskId();
            String taskId = taskModel.getNextTaskId();
            String usageId = materialUsageModel.getNextMaterialUsageId();

            //Save new production
            boolean productionSaved = productionModel.saveProductions(
                    new ProductionDto(
                            productionId, inventoryId, productionDescription, productionStatus
                    )
            );
            if (!productionSaved) {
                connection.rollback();
                new Alert(Alert.AlertType.ERROR, "Failed to save production!").show();
                return;
            }

            //Save new task
            boolean taskSaved = taskModel.saveTasks(
                    new TaskDto(
                            taskId, employeeId, taskDescription, taskStatus
                    )
            );
            if (!taskSaved) {
                connection.rollback();
                new Alert(Alert.AlertType.ERROR, "Failed to save task!").show();
                return;
            }

            //Save new production_task
            boolean productionTaskSaved = productionTaskModel.saveProductionTasks(
                    new ProductionTaskDto(
                            productionTaskId, productionId, taskId
                    )
            );
            if (!productionTaskSaved) {
                connection.rollback();
                new Alert(Alert.AlertType.ERROR, "Failed to save production task!").show();
                return;
            }

            //Save new material_usage
            boolean materialUsageSaved = materialUsageModel.saveMaterialUsage(
                    new MaterialUsageDto(
                            usageId, productionId, materialId, materialUsageQty
                    )
            );
            if (!materialUsageSaved) {
                connection.rollback();
                new Alert(Alert.AlertType.ERROR, "Failed to save material usage!").show();
                return;
            }

            //Update material quantity (reduce)
            boolean materialUpdated = materialModel.reduceMaterialQty(materialId, materialUsageQty);
            if (!materialUpdated) {
                connection.rollback();
                new Alert(Alert.AlertType.ERROR, "Failed to update material quantity!").show();
                return;
            }

            //Update inventory quantity (increase)
            boolean inventoryUpdated = inventoryModel.increaseInventoryQty(inventoryId, productQty);
            if (!inventoryUpdated) {
                connection.rollback();
                new Alert(Alert.AlertType.ERROR, "Failed to update inventory quantity!").show();
                return;
            }

/*            boolean isEmailSent = EmailUtil.sendSimpleMail();
            if (!isEmailSent) {
                connection.rollback();
                new Alert(Alert.AlertType.ERROR, "Failed to send email notification!").show();
                return;
            }*/


            //Commit transaction
            connection.commit();
            new Alert(Alert.AlertType.INFORMATION, "Production placed successfully!").show();
            refreshPage();

        } catch (Exception e) {
            try {
                if (connection != null) connection.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error placing production!").show();
        } finally {
            try {
                if (connection != null) connection.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void btnAddToCartOnAction(ActionEvent actionEvent) {
        String employeeId = (String) cmbEmployeeId.getSelectionModel().getSelectedItem();
        String inventoryId = (String) cmbInventoryId.getSelectionModel().getSelectedItem();
        String materialId = (String) cmbMaterialId.getSelectionModel().getSelectedItem();
        String productDescription = txtProductionDescription.getText();
        String taskDescription = txtTaskDescription.getText();
        String productQtyStr = txtProductQty.getText();
        String materialUsageQtyStr = txtMaterialUsageQty.getText();

        // Validate selections and inputs
        if (employeeId == null || inventoryId == null || materialId == null ||
                productDescription.isEmpty() || taskDescription.isEmpty() ||
                productQtyStr.isEmpty() || materialUsageQtyStr.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields!").show();
            return;
        }

        if (!productQtyStr.matches("\\d+") || !materialUsageQtyStr.matches("\\d+")) {
            new Alert(Alert.AlertType.WARNING, "Enter valid numeric quantities!").show();
            return;
        }

        int productQty = Integer.parseInt(productQtyStr);
        int materialUsageQty = Integer.parseInt(materialUsageQtyStr);
        int availableMaterialQty = Integer.parseInt(lblMaterialQty.getText());

        if (materialUsageQty > availableMaterialQty) {
            new Alert(Alert.AlertType.WARNING, "Not enough material quantity!").show();
            return;
        }

        String employeeName = lblEmployeeName.getText();
        String itemName = lblItemName.getText();
        String materialName = lblMaterialName.getText();
        String colorType = lblMaterialColorType.getText();

        // Check if this item/material is already in the cart
        for (ProductionCartTM cartTM : tblProductionPlacement.getItems()) {
            if (cartTM.getItemName().equals(itemName) && cartTM.getMaterialName().equals(materialName)) {
                int newProductQty = cartTM.getQuantity() + productQty;
                int newMaterialUsageQty = cartTM.getQtyNeeded() + materialUsageQty;
                if (newMaterialUsageQty > availableMaterialQty) {
                    new Alert(Alert.AlertType.WARNING, "Not enough material quantity!").show();
                    return;
                }
                cartTM.setQuantity(newProductQty);
                cartTM.setQtyNeeded(newMaterialUsageQty);
                tblProductionPlacement.refresh();
                lblMaterialQty.setText(String.valueOf(availableMaterialQty - materialUsageQty));
                return;
            }
        }

        // Add new row to cart
        Button removeBtn = new Button("Remove");
        ProductionCartTM cartTM = new ProductionCartTM(
                employeeName,
                itemName,
                productDescription,
                productQty,
                taskDescription,
                materialName,
                materialUsageQty,
                colorType,
                removeBtn
        );

        removeBtn.setOnAction(e -> {
            tblProductionPlacement.getItems().remove(cartTM);
            tblProductionPlacement.refresh();
            // Optionally, restore material quantity in UI here
        });

        tblProductionPlacement.getItems().add(cartTM);
        lblMaterialQty.setText(String.valueOf(availableMaterialQty - materialUsageQty));
    }
    private void navigateTo(String path) {
        try {
            ancProductionPlacementPage.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));


            anchorPane.prefWidthProperty().bind(ancProductionPlacementPage.widthProperty());
            anchorPane.prefHeightProperty().bind(ancProductionPlacementPage.heightProperty());

            ancProductionPlacementPage.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }


}
