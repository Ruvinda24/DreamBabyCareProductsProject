package lk.ijse.finalProject.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.finalProject.dto.tm.InventoryTM;
import lk.ijse.finalProject.model.InventoryModel;

public class InventoryController {
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
    }

    public void search(KeyEvent keyEvent) {
    }

    public void saveBtnOnAction(ActionEvent actionEvent) {
    }

    public void updateBtnOnAction(ActionEvent actionEvent) {
    }

    public void deleteBtnOnAction(ActionEvent actionEvent) {
    }

    public void resetBtnOnAction(ActionEvent actionEvent) {
    }

    public void onClickTable(MouseEvent mouseEvent) {
    }
}
