package lk.ijse.finalProject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.finalProject.dto.tm.CartTM;
import lk.ijse.finalProject.model.CustomerModel;
import lk.ijse.finalProject.model.InventoryModel;
import lk.ijse.finalProject.model.OrdersModel;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrderPlacementController implements Initializable {
    public AnchorPane ancOrderPlacementPage;
    public Label homeLabel;
    public Label lblOrderId;
    public Label orderPlacementDate;
    public TextField txtCustomerContact;
    public Label lblCustomerName;
    public ComboBox cmbItemId;
    public Label lblItemName;
    public TextField lblItemQty;
    public Label txtAddToCartQty;
    public Label lblItemPrice;
    public TableView<CartTM> tblOrderPlacement;
    public TableColumn<CartTM, String> colCustomerId;
    public TableColumn<CartTM, String> colItemId;
    public TableColumn<CartTM, String> colItemName;
    public TableColumn<CartTM, Integer> colQuantity;
    public TableColumn<CartTM, Double> qtyPrice;
    public TableColumn<CartTM, Double> colTotal;
    public TableColumn<?, ?> colAction;
    public Label labelPopUpCustomer;

    private final OrdersModel ordersModel = new OrdersModel();
    private final CustomerModel customerModel = new CustomerModel();
    private final InventoryModel inventoryModel = new InventoryModel();

    private final ObservableList<CartTM> cartData = FXCollections.observableArrayList();


    public void goToDashBoardPage(MouseEvent mouseEvent) {
        navigateTo("/view/DashBoardView.fxml");
    }

    private void setCellValues() {
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("cartQty"));
        qtyPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));

        tblOrderPlacement.setItems(cartData);
    }

    public void btnAddToCartOnAction(ActionEvent actionEvent) {
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
    }

    public void goToCustomerPopUp(MouseEvent mouseEvent) {
    }

    public void searchCustomerContact(KeyEvent keyEvent) {
    }

    private void navigateTo(String path) {
        try {
            ancOrderPlacementPage.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancOrderPlacementPage.widthProperty());
            anchorPane.prefHeightProperty().bind(ancOrderPlacementPage.heightProperty());

            ancOrderPlacementPage.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setCellValues();
        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load data..!").show();
        }
    }

    private void refreshPage() throws SQLException, ClassNotFoundException {
        lblOrderId.setText(ordersModel.getNextOrderId());
        orderPlacementDate.setText(LocalDate.now().toString());

        loadCustomerIds();
        loadItemIds();
    }

    private void loadItemIds() {
        try {
            ArrayList<String> itemIdsList = itemIdsList = inventoryModel.getAllItemIds();
            ObservableList<String> itemIds = FXCollections.observableArrayList();
            itemIds.addAll(itemIdsList);
            cmbItemId.setItems(itemIds);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load data..!").show();
        }

    }

    private void loadCustomerIds() {
        try {
            ArrayList<String> customerIdsList = customerModel.getAllCustomerIds();
            ObservableList<String> customerIds = FXCollections.observableArrayList();
            customerIds.addAll(customerIdsList);
            cmbItemId.setItems(customerIds);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load data..!").show();
        }
    }

    private void resetPage(){
        //lblOrderId.setText("");

        loadNextId();
        loadCustomerIds();
        loadItemIds();

        orderPlacementDate.setText("");
        txtCustomerContact.setText("");
        lblCustomerName.setText("");
        cmbItemId.getSelectionModel().clearSelection();
        lblItemName.setText("");
        lblItemQty.setText("");
        txtAddToCartQty.setText("");
        lblItemPrice.setText("");
        cartData.clear();
        tblOrderPlacement.refresh();
    }

    private void loadNextId() {
        try {
            String nextId = ordersModel.getNextOrderId();
            lblOrderId.setText(nextId);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load next order ID..!").show();
        }
    }
}
