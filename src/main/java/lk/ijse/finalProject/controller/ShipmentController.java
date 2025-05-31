package lk.ijse.finalProject.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.finalProject.dto.tm.ShipmentTM;
import lk.ijse.finalProject.model.ShipmentModel;

public class ShipmentController {
    public AnchorPane ancShipmentContainer;
    public TextField searchField;
    public Label shipmentIdLabel;
    public TextField txtTrackingNumber;
    public DatePicker shipmentDatePicker;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public TableView<ShipmentTM> tblShipment;
    public TableColumn<ShipmentTM,String> colShipmentDate;
    public TableColumn<ShipmentTM,String> colTrackingNumber;
    public TableColumn<ShipmentTM,String> colShipmentId;

    private final ShipmentModel shipmentModel = new ShipmentModel();

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
