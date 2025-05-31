package lk.ijse.finalProject.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.finalProject.dto.tm.MaterialTM;
import lk.ijse.finalProject.model.MaterialModel;

public class MaterialController {
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
