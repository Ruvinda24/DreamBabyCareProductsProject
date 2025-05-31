package lk.ijse.finalProject.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.finalProject.dto.tm.SupplierTM;

public class SupplierController {
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
    public TableColumn<SupplierTM,String> colAccountDetails;
    public TableColumn<SupplierTM,String> colContact;
    public TableColumn<SupplierTM,String> colName;
    public TableColumn<SupplierTM,String> colSupplierId;


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
