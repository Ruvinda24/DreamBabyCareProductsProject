package lk.ijse.finalProject.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.finalProject.dto.tm.ProductionTaskTM;
import lk.ijse.finalProject.model.ProductionTaskModel;

public class ProductionTaskController {
    public AnchorPane ancProductionTaskContainer;
    public TextField searchField;
    public Label productionTaskIdLabel;
    public ComboBox cmbProductionId;
    public ComboBox cmbTaskID;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public TableView<ProductionTaskTM> tblProductionTask;
    public TableColumn<ProductionTaskTM,String> colProductionTaskId;
    public TableColumn<ProductionTaskTM,String> colTaskId;
    public TableColumn<ProductionTaskTM,String> colProductionId;

    private final ProductionTaskModel productionTaskModel = new ProductionTaskModel();

    public void labelOverViewClickOnAction(MouseEvent mouseEvent) {
    }

    public void search(KeyEvent keyEvent) {
    }

    public void goToAddProductionPage(MouseEvent mouseEvent) {
    }

    public void goToAddTaskPage(MouseEvent mouseEvent) {
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
