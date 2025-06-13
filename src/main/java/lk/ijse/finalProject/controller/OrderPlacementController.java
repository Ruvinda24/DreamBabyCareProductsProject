package lk.ijse.finalProject.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.finalProject.db.DBConnection;
import lk.ijse.finalProject.dto.InventoryDto;
import lk.ijse.finalProject.dto.PaymentDto;
import lk.ijse.finalProject.dto.ShipmentDto;
import lk.ijse.finalProject.dto.tm.OrderCartTM;
import lk.ijse.finalProject.model.*;
import lk.ijse.finalProject.util.OrderReportMailer;

import java.net.URL;
import java.sql.Connection;
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
    public ComboBox cmbCustomerName;
    public ComboBox cmbItemId;
    public Label lblItemName;
    public Label txtAddToCartQty;
    public Label lblItemPrice;
    public TableView<OrderCartTM> tblOrderPlacement;
    public TableColumn<OrderCartTM, String> colCustomerId;
    public TableColumn<OrderCartTM, String> colItemId;
    public TableColumn<OrderCartTM, String> colItemName;
    public TableColumn<OrderCartTM, Integer> colQuantity;
    public TableColumn<OrderCartTM, Double> qtyPrice;
    public TableColumn<OrderCartTM, Double> colTotal;
    public TableColumn<OrderCartTM, String> colPaymentMethod;
    public TableColumn<?, ?> colAction;
    public Label labelPopUpCustomer;

    private final OrdersModel ordersModel = new OrdersModel();
    private final CustomerModel customerModel = new CustomerModel();
    private final InventoryModel inventoryModel = new InventoryModel();
    private final OrderItemModel orderItemModel = new OrderItemModel();
    private final PaymentModel paymentModel = new PaymentModel();
    private final ShipmentModel shipmentModel = new ShipmentModel();

    private final ObservableList<OrderCartTM> cartData = FXCollections.observableArrayList();
    public TextField txtCartQty;
    public ComboBox cmbPaymentMethod;
    public TextField txtShipmentTrackingNumber;


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
        colPaymentMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));

        tblOrderPlacement.setItems(cartData);
    }

    public void btnAddToCartOnAction(ActionEvent actionEvent) {
        String selectedItemId = (String) cmbItemId.getSelectionModel().getSelectedItem();
        String cartQtyString = txtCartQty.getText();

        if (selectedItemId == null) {
            new Alert(Alert.AlertType.WARNING, "Please select item..!").show();
            return;
        }

        if (!cartQtyString.matches("^[0-9]+$")) {
            new Alert(Alert.AlertType.WARNING, "Please enter valid quantity..!").show();
            return;
        }

        String itemQtyOnStockString = txtAddToCartQty.getText();
        int cartQty = Integer.parseInt(cartQtyString);
        int itemQtyOnStock = Integer.parseInt(itemQtyOnStockString);

        if (itemQtyOnStock < cartQty) {
            new Alert(Alert.AlertType.WARNING, "Not enough item quantity..!").show();
            return;
        }

        String selectedCustomerId = (String) cmbCustomerName.getValue();
        String itemName = lblItemName.getText();
        String itemUnitPriceString = lblItemPrice.getText();

        double itemUnitPrice = Double.parseDouble(itemUnitPriceString);
        double total = itemUnitPrice * cartQty;
        String paymentMethod = (String) cmbPaymentMethod.getSelectionModel().getSelectedItem();

        if (paymentMethod == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a payment method..!").show();
            return;
        }

        for (OrderCartTM cartTM : cartData) {
            if (cartTM.getItemId().equals(selectedItemId)) {
                int newQty = cartTM.getCartQty() + cartQty;
                if (itemQtyOnStock < newQty) {
                    new Alert(Alert.AlertType.WARNING, "Not enough item quantity..!").show();
                    return;
                }
                cartTM.setCartQty(newQty);
                cartTM.setTotal(newQty * itemUnitPrice);
                tblOrderPlacement.refresh();
                // Update available quantity in UI only
                txtAddToCartQty.setText(String.valueOf(itemQtyOnStock - cartQty));
                return;
            }
        }

        Button removeBtn = new Button("Remove");
        OrderCartTM cartTM = new OrderCartTM(
                selectedCustomerId,
                selectedItemId,
                itemName,
                cartQty,
                itemUnitPrice,
                total,
                paymentMethod,
                removeBtn
        );

        removeBtn.setOnAction(action -> {
            cartData.remove(cartTM);
            tblOrderPlacement.refresh();
        });

        cartData.add(cartTM);
        // Update available quantity in UI only
        txtAddToCartQty.setText(String.valueOf(itemQtyOnStock - cartQty));
        // Do not update inventory in the database here
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            // 1. Prepare IDs and data
            String shipmentId = shipmentModel.getNextShipmentId();
            String shipmentDate = orderPlacementDate.getText();
            String trackingNumber = txtShipmentTrackingNumber.getText();

            String orderId = lblOrderId.getText();
            String orderDate = orderPlacementDate.getText();
            String customerContact = txtCustomerContact.getText();
            String customerId = customerModel.getCustomerIdByContact(customerContact);
            String status = "Shipped";

            String paymentId = paymentModel.getNextPaymentId();
            String paymentMethod = (String) cmbPaymentMethod.getSelectionModel().getSelectedItem();
            double totalAmount = cartData.stream().mapToDouble(OrderCartTM::getTotal).sum();

            // Save shipment
            boolean shipmentSaved = shipmentModel.saveShipments(
                    new ShipmentDto(
                            shipmentId,
                            trackingNumber,
                            LocalDate.parse(shipmentDate))
            );
            if (!shipmentSaved) {
                connection.rollback();
                new Alert(Alert.AlertType.ERROR, "Failed to save shipment!").show();
                return;
            }

            // Save order
            boolean orderSaved = ordersModel.saveNewOrder(
                    orderId,
                    orderDate,
                    customerId,
                    shipmentId,
                    status
            );
            if (!orderSaved) {
                connection.rollback();
                new Alert(Alert.AlertType.ERROR, "Failed to save order!").show();
                return;
            }

            // Save order items and update inventory
            boolean allItemsSaved = true;
            for (OrderCartTM cartTM : cartData) {
                String orderItemId = orderItemModel.getNextOrderItemId();
                boolean itemSaved = orderItemModel.saveNewOrderItem(
                        orderItemId,
                        orderId,
                        cartTM.getItemId(),
                        cartTM.getCartQty(),
                        cartTM.getUnitPrice() * cartTM.getCartQty()
                );
                boolean inventoryUpdated = inventoryModel.reduceItemQty(
                        cartTM.getItemId(),
                        cartTM.getCartQty()
                );
                if (!itemSaved || !inventoryUpdated) {
                    allItemsSaved = false;
                    break;
                }
            }
            if (!allItemsSaved) {
                connection.rollback();
                new Alert(Alert.AlertType.ERROR, "Failed to save order items or update inventory!").show();
                return;
            }

            // Save payment
            boolean paymentSaved = paymentModel.savePayments(
                    new PaymentDto(
                            paymentId,
                            orderId,
                            totalAmount,
                            paymentMethod)
            );
            if (!paymentSaved) {
                connection.rollback();
                new Alert(Alert.AlertType.ERROR, "Failed to save payment!").show();
                return;
            }

            resetPage();
            new Alert(Alert.AlertType.INFORMATION, "Order placed successfully!").show();

            // Send order report email in a new thread (after commit)
                try {
                    boolean isEmailSent =new OrderReportMailer().sendLastOrderReport();

                    if (isEmailSent) {
                        new Alert(Alert.AlertType.INFORMATION, "Order report sent to email successfully!").show();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to send order report via email!").show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            // Commit transaction
            connection.commit();


        } catch (Exception e) {
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error placing order!").show();
        } finally {
            try {
                if (connection != null) connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void goToCustomerPopUp(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/CustomerPagePopUp.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(anchorPane));
            stage.setTitle("Add New Customer");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load customer pop-up..!").show();
        }

    }

    public void searchCustomerContact(KeyEvent keyEvent) {
        String contact = txtCustomerContact.getText();
        try {
            if (contact.isEmpty()) {
                cmbCustomerName.setItems(FXCollections.observableArrayList());
                cmbCustomerName.getSelectionModel().clearSelection();
                return;
            }
            ArrayList<String> matchingNames = customerModel.getCustomerNamesByPartialPhone(contact);
            if (!matchingNames.isEmpty()) {
                cmbCustomerName.setItems(FXCollections.observableArrayList(matchingNames));
                cmbCustomerName.getSelectionModel().selectFirst();
            } else {
                cmbCustomerName.setItems(FXCollections.observableArrayList("No customer found with this contact"));
                cmbCustomerName.getSelectionModel().selectFirst();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to search customer..!").show();
        }
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
        cmbPaymentMethod.setItems(FXCollections.observableArrayList("Online", "Cash on Delivery", "Card Payment"));


        cmbItemId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadItemDetails((String) newValue);
            }
        });


    }

    private void loadItemDetails(String itemId) {
        try {
            InventoryDto item = inventoryModel.getItemsByIds(itemId);
            if (item != null) {
                lblItemName.setText(item.getItem_name());
                txtAddToCartQty.setText(String.valueOf(item.getQuantity_available()));
                lblItemPrice.setText(String.valueOf(item.getUnit_price()));
            } else {
                lblItemName.setText("");
                txtCartQty.setText("");
                lblItemPrice.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load item details..!").show();
        }
    }

    private void refreshPage() throws SQLException, ClassNotFoundException {
        lblOrderId.setText(ordersModel.getNextOrderId());
        orderPlacementDate.setText(LocalDate.now().toString());

        //loadCustomerIds();
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

/*    private void loadCustomerIds() {
        try {
            ArrayList<String> customerIdsList = customerModel.getAllCustomerIds();
            ObservableList<String> customerIds = FXCollections.observableArrayList();
            customerIds.addAll(customerIdsList);
            cmbItemId.setItems(customerIds);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load data..!").show();
        }
    }*/

    private void resetPage() {
        //lblOrderId.setText("");

        loadNextId();
        //loadCustomerIds();
        loadItemIds();

        //orderPlacementDate.setText("");
        txtCustomerContact.setText("");
        cmbCustomerName.getSelectionModel().clearSelection();
        cmbItemId.getSelectionModel().clearSelection();
        lblItemName.setText("");
        txtCartQty.setText("");
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

    private void resetWhenAddToCart() {
        cmbItemId.getSelectionModel().clearSelection();
        txtCartQty.clear();
        lblItemName.setText("");
        txtAddToCartQty.setText("");
        lblItemPrice.setText("");
        cmbPaymentMethod.getSelectionModel().clearSelection();
    }
}
