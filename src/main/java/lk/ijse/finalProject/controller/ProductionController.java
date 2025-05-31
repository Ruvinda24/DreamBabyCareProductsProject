package lk.ijse.finalProject.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.finalProject.dto.tm.ProductionTM;
import lk.ijse.finalProject.dto.tm.ProductionTaskTM;
import lk.ijse.finalProject.model.ProductionModel;

public class ProductionController {
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

    public void labelOverViewClickOnAction(MouseEvent mouseEvent) {
    }

    public void search(KeyEvent keyEvent) {
    }

    public void goToAddInventoryPage(MouseEvent mouseEvent) {
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
