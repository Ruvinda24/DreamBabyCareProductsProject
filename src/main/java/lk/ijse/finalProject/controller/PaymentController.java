package lk.ijse.finalProject.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.finalProject.dto.PaymentDto;
import lk.ijse.finalProject.dto.tm.PaymentTM;
import lk.ijse.finalProject.model.PaymentModel;

public class PaymentController {
    public AnchorPane ancPaymentContainer;
    public TextField searchField;
    public Label paymentIdLabel;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public TableView<PaymentTM> tblPayment;
    public TableColumn<PaymentTM,String> colPaymentId;
    public TableColumn<PaymentTM,String> colOrderId;
    public TableColumn<PaymentTM,String> colAmount;
    public TableColumn<PaymentTM,String> colPaymentMethod;
    
    private final PaymentModel paymentModel = new PaymentModel();
    public ComboBox cmbOrderId;
    public TextField txtAmount;
    public ComboBox cmbPaymentMethod;

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

    public void goToAddOrderLabel(MouseEvent mouseEvent) {
    }
}
