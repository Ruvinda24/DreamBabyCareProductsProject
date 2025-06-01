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
import lk.ijse.finalProject.dto.InventoryDto;
import lk.ijse.finalProject.dto.tm.InventoryTM;
import lk.ijse.finalProject.model.InventoryModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {
    public AnchorPane ancInventoryContainer;
    public TextField searchField;
    public Label inventoryIdLabel;
    public TextField txtItemName;
    public ComboBox cmbPrintedEmbroidered;
    public TextField txtSize;
    public TextField txtUnitPrice;
    public TextField txtQuantityAvailable;
    public TextField txtStoredLocation;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public TableView<InventoryTM> tblInventory;
    public TableColumn<InventoryTM,String> colStoredLocation;
    public TableColumn<InventoryTM,String> colQuantityAvailable;
    public TableColumn<InventoryTM,String> colUnitPrice;
    public TableColumn<InventoryTM,String> colSize;
    public TableColumn<InventoryTM,String> colPrinterEmbroidered;
    public TableColumn<InventoryTM,String> colItemName;
    public TableColumn<InventoryTM,String> colInventoryId;

    private final InventoryModel inventoryModel = new InventoryModel();

    public void labelOverViewClickOnAction(MouseEvent mouseEvent) {
        navigateTo("/view/OverView.fxml");
    }

    public void search(KeyEvent keyEvent) {
        String searchText = searchField.getText();
        if (searchText.isEmpty()) {
            try {
                loadInventoryDataTable();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to load inventory data.").show();
            }
        } else {
            try {
                tblInventory.setItems(FXCollections.observableArrayList(
                        inventoryModel.searchItems(searchText)
                                .stream()
                                .map(inventoryDto -> new InventoryTM(
                                        inventoryDto.getInventory_id(),
                                        inventoryDto.getItem_name(),
                                        inventoryDto.getPrinted_embroidered(),
                                        inventoryDto.getSize(),
                                        inventoryDto.getUnit_price(),
                                        inventoryDto.getQuantity_available(),
                                        inventoryDto.getStored_location()
                                ))
                                .toList()
                ));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to search items.").show();
            }
        }
    }

    public void saveBtnOnAction(ActionEvent actionEvent) {
        String inventoryId = inventoryIdLabel.getText();
        String itemName = txtItemName.getText();
        String printedEmbroidered = cmbPrintedEmbroidered.getValue().toString();
        String size = txtSize.getText();
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int quantityAvailable = Integer.parseInt(txtQuantityAvailable.getText());
        String storedLocation = txtStoredLocation.getText();

        if (inventoryId.isEmpty() || itemName.isEmpty() || printedEmbroidered.isEmpty() || size.isEmpty() || txtUnitPrice.getText().isEmpty() || txtQuantityAvailable.getText().isEmpty() || storedLocation.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields.").show();
        } else {
            try {
                boolean isSaved = inventoryModel.saveItems(new InventoryDto(
                        inventoryId,
                        itemName,
                        printedEmbroidered,
                        size,
                        unitPrice,
                        quantityAvailable,
                        storedLocation
                ));

                if (isSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "Item saved successfully.").show();
                    loadInventoryDataTable();
                    loadNextInventoryId();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to save item.").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error occurred while saving item.").show();
            }
        }
    }

    private void resetPage() {
        try {
            loadInventoryDataTable();
            loadNextInventoryId();

            btnSave.setDisable(false);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);

            txtItemName.setText("");
            txtQuantityAvailable.setText("");
            txtUnitPrice.setText("");
            txtSize.setText("");
            txtStoredLocation.setText("");
            cmbPrintedEmbroidered.setValue(null);
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to reset.").show();
        }
    }

    public void updateBtnOnAction(ActionEvent actionEvent) {
        String inventoryId = inventoryIdLabel.getText();
        String itemName = txtItemName.getText();
        String printedEmbroidered = cmbPrintedEmbroidered.getValue().toString();
        String size = txtSize.getText();
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int quantityAvailable = Integer.parseInt(txtQuantityAvailable.getText());
        String storedLocation = txtStoredLocation.getText();

        if (inventoryId.isEmpty() || itemName.isEmpty() || printedEmbroidered.isEmpty() || size.isEmpty() || txtUnitPrice.getText().isEmpty() || txtQuantityAvailable.getText().isEmpty() || storedLocation.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select an item to update.").show();
        } else {
            try {
                boolean isUpdated = inventoryModel.updateItems(new InventoryDto(
                        inventoryId,
                        itemName,
                        printedEmbroidered,
                        size,
                        unitPrice,
                        quantityAvailable,
                        storedLocation
                ));

                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "Item updated successfully.").show();
                    loadInventoryDataTable();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to update item.").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error occurred while updating item.").show();
            }
        }
    }

    public void deleteBtnOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this item?",
                ButtonType.YES,
                ButtonType.NO);
        alert.setTitle("Delete Confirmation");

        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES) {
            String inventoryId = inventoryIdLabel.getText();
            if (inventoryId.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please select an item to delete.").show();
            } else {
                try {
                    boolean isDeleted = inventoryModel.deleteItems(inventoryId);
                    if (isDeleted) {
                        new Alert(Alert.AlertType.INFORMATION, "Item deleted successfully.").show();
                        loadInventoryDataTable();
                        resetPage();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to delete item.").show();
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Error occurred while deleting item.").show();
                }
            }
        }
    }

    public void resetBtnOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    public void onClickTable(MouseEvent mouseEvent) {
        InventoryTM selectedItem = tblInventory.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            inventoryIdLabel.setText(selectedItem.getInventory_id());
            txtItemName.setText(selectedItem.getItem_name());
            cmbPrintedEmbroidered.setValue(selectedItem.getPrinted_embroidered());
            txtSize.setText(selectedItem.getSize());
            txtUnitPrice.setText(String.valueOf(selectedItem.getUnit_price()));
            txtQuantityAvailable.setText(String.valueOf(selectedItem.getQuantity_available()));
            txtStoredLocation.setText(selectedItem.getStored_location());

            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    private void navigateTo(String path) {
        try {
            ancInventoryContainer.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancInventoryContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancInventoryContainer.heightProperty());

            ancInventoryContainer.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colInventoryId.setCellValueFactory(new PropertyValueFactory<>("inventory_id"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("item_name"));
        colPrinterEmbroidered.setCellValueFactory(new PropertyValueFactory<>("printed_embroidered"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
        colQuantityAvailable.setCellValueFactory(new PropertyValueFactory<>("quantity_available"));
        colStoredLocation.setCellValueFactory(new PropertyValueFactory<>("stored_location"));

        cmbPrintedEmbroidered.setItems(FXCollections.observableArrayList("Printed", "Embroidered"));
        try{
            loadInventoryDataTable();
            loadNextInventoryId();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load inventory data.").show();

        }
    }

    private void loadNextInventoryId() {
        try {
            String nextInventoryId = inventoryModel.getNextInventoryId();
            inventoryIdLabel.setText(nextInventoryId);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load next inventory ID.").show();
        }
    }

        private void loadInventoryDataTable() throws SQLException, ClassNotFoundException {

        tblInventory.setItems(FXCollections.observableArrayList(
                inventoryModel.getAllItems()
                        .stream()
                        .map(
                                inventoryDto -> new InventoryTM(
                                        inventoryDto.getInventory_id(),
                                        inventoryDto.getItem_name(),
                                        inventoryDto.getPrinted_embroidered(),
                                        inventoryDto.getSize(),
                                        inventoryDto.getUnit_price(),
                                        inventoryDto.getQuantity_available(),
                                        inventoryDto.getStored_location()
                                ))
                        .toList()
        ));
    }
}
