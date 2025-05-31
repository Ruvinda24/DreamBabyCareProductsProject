package lk.ijse.finalProject.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.finalProject.dto.tm.TaskTM;
import lk.ijse.finalProject.model.TaskModel;

public class TaskController {
    public AnchorPane ancTaskContainer;
    public TextField searchField;
    public Label taskIdLabel;
    public ComboBox cmbEmployeeId;
    public TextField txtDescription;
    public ComboBox cmbStatus;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public TableView<TaskTM> tblTask;
    public TableColumn<TaskTM,String> colTaskId;
    public TableColumn<TaskTM,String> colEmployeeId;
    public TableColumn<TaskTM,String> colDescription;
    public TableColumn<TaskTM,String> colStatus;

    private final TaskModel taskModel = new TaskModel();

    public void labelOverViewClickOnAction(MouseEvent mouseEvent) {
    }

    public void search(KeyEvent keyEvent) {
    }

    public void goToAddEmployeePage(MouseEvent mouseEvent) {
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
