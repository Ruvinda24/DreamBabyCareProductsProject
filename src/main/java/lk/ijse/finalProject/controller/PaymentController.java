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
import lk.ijse.finalProject.dto.PaymentDto;
import lk.ijse.finalProject.dto.tm.PaymentTM;
import lk.ijse.finalProject.model.PaymentModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {
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
        navigateTo("/view/OverView.fxml");
    }

    public void search(KeyEvent keyEvent) {
        String searchText = searchField.getText();
        if (searchText.isEmpty()) {
            try {
                loadPaymentTableData();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to load payment data").show();
            }
        } else {
            try {
                tblPayment.setItems(FXCollections.observableArrayList(
                        paymentModel.searchPayments(searchText)
                                .stream()
                                .map(payment -> new PaymentTM(
                                        payment.getPayment_id(),
                                        payment.getOrder_id(),
                                        payment.getAmount(),
                                        payment.getPayment_method()
                                )).toList()
                ));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to search payments").show();
            }
        }
    }

    public void saveBtnOnAction(ActionEvent actionEvent) {
        String paymentId = paymentIdLabel.getText();
        String orderId = cmbOrderId.getSelectionModel().getSelectedItem().toString();
        double amount;
        try {
            amount = Double.parseDouble(txtAmount.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid amount format").show();
            return;
        }
        String paymentMethod = cmbPaymentMethod.getSelectionModel().getSelectedItem().toString();

        if (paymentId.isEmpty() || orderId.isEmpty() || txtAmount.getText().isEmpty() || paymentMethod.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields").show();
            return;
        }

        try {
            PaymentDto paymentDto = new PaymentDto(
                    paymentId,
                    orderId,
                    amount,
                    paymentMethod
            );

            boolean isSaved = paymentModel.savePayments(paymentDto);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Payment saved successfully").show();
                resetPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save payment").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to save payment").show();
        }
    }

    public void updateBtnOnAction(ActionEvent actionEvent) {
        String paymentId = paymentIdLabel.getText();
        String orderId = cmbOrderId.getSelectionModel().getSelectedItem().toString();
        double amount = Double.parseDouble(txtAmount.getText());
        String paymentMethod = cmbPaymentMethod.getSelectionModel().getSelectedItem().toString();

        if (paymentId.isEmpty() || orderId.isEmpty() || txtAmount.getText().isEmpty() || paymentMethod.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields").show();
            return;
        }

        try {
            PaymentDto paymentDto = new PaymentDto(
                    paymentId,
                    orderId,
                    amount,
                    paymentMethod
            );

            boolean isUpdated = paymentModel.updatePayments(paymentDto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Payment updated successfully").show();
                resetPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update payment").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid amount format").show();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to update payment").show();
        }
    }

    public void deleteBtnOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this payment?",
                ButtonType.YES,
                ButtonType.NO);
        alert.setTitle("Delete Payment");

        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES) {
            String selectedPayment = paymentIdLabel.getText();
            if (selectedPayment != null) {
                try {
                    boolean isDeleted = paymentModel.deletePayments(selectedPayment);
                    if (isDeleted) {
                        new Alert(Alert.AlertType.INFORMATION, "Payment deleted successfully").show();
                        resetPage();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to delete payment").show();
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Failed to delete payment").show();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select a payment to delete").show();
            }
        }
    }

    public void resetBtnOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    private void resetPage() {
        try {
            loadNextId();
            loadPaymentTableData();
            loadOrderIds();

            paymentIdLabel.setText("");
            cmbOrderId.setValue(null);
            txtAmount.clear();
            cmbPaymentMethod.setValue(null);

            btnSave.setDisable(false);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);

            tblPayment.getSelectionModel().clearSelection();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to reset page").show();
        }
    }

    public void onClickTable(MouseEvent mouseEvent) {
        PaymentTM selectedPayment = tblPayment.getSelectionModel().getSelectedItem();
        if (selectedPayment != null) {
            paymentIdLabel.setText(selectedPayment.getPayment_id());
            cmbOrderId.setValue(selectedPayment.getOrder_id());
            txtAmount.setText(String.valueOf(selectedPayment.getAmount()));
            cmbPaymentMethod.setValue(selectedPayment.getPayment_method());

            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    public void goToAddOrderLabel(MouseEvent mouseEvent) {
        navigateTo("/view/OrdersView.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("payment_id"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("order_id"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colPaymentMethod.setCellValueFactory(new PropertyValueFactory<>("payment_method"));

        try {
            cmbOrderId.setItems(FXCollections.observableArrayList(paymentModel.getAllOrderIds()));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Order Ids").show();
        }

        try {
            cmbPaymentMethod.setItems(FXCollections.observableArrayList("Online","Cash", "Card"));
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load payment methods").show();
        }

        try {
            loadPaymentTableData();
            loadNextId();
            loadOrderIds();

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load payment data").show();
        }
    }

    private void loadOrderIds() {
        try {
            cmbOrderId.setItems(FXCollections.observableArrayList(paymentModel.getAllOrderIds()));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Order Ids").show();
        }
    }

    private void loadNextId() {
        try {
            String nextId = paymentModel.getNextPaymentId();
            paymentIdLabel.setText(nextId);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load next Payment ID").show();
        }
    }

    private void loadPaymentTableData() throws SQLException, ClassNotFoundException {
        tblPayment.setItems(FXCollections.observableArrayList(
                paymentModel.getAllPayments()
                        .stream()
                        .map(payment -> new PaymentTM(
                                payment.getPayment_id(),
                                payment.getOrder_id(),
                                payment.getAmount(),
                                payment.getPayment_method()
                        )).toList()
        ));
    }

    private void navigateTo(String path) {
        try {
            ancPaymentContainer.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancPaymentContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancPaymentContainer.heightProperty());

            ancPaymentContainer.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }

}
