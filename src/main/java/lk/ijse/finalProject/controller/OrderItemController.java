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
import lk.ijse.finalProject.dto.OrderItemDto;
import lk.ijse.finalProject.dto.tm.OrderItemTM;
import lk.ijse.finalProject.model.OrderItemModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class OrderItemController implements Initializable {
    public Label overViewLabelButton;
    public AnchorPane ancOrdersItemViewContainer;
    public Label ordersItemIdLabel;
    public ComboBox cmbOrderId1;
    public Label lblOrderId1;
    public Button btnReset;
    public ComboBox cmbInventoryId1;
    public Label lblInventoryId1;
    public Button btnUpdate;
    public TextField txtQuantity;
    public Button btnDelete;
    public Button btnSave;

    public TableView tblOrderItems;
    public TableColumn colOrderItemId;
    public TableColumn colOrderId;
    public TableColumn colInventoryId;
    public TableColumn colQuantity;
    public TableColumn colAmount;

    private final OrderItemModel orderItemModel = new OrderItemModel();
    public TextField txtAmount;

    private final String quantityRegex = "^\\d+$";
    private final String amountRegex = "^\\d+(\\.\\d{2})?$";
    public TextField searchField;

    public void labelOverViewClickOnAction(MouseEvent mouseEvent) {
        navigateTo("/view/OverView.fxml");
    }

    public void resetBtnOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    public void updateBtnOnAction(ActionEvent actionEvent) {
        String orderItemId = ordersItemIdLabel.getText();
        String orderId = (String) cmbOrderId1.getSelectionModel().getSelectedItem();
        String inventoryId = (String) cmbInventoryId1.getSelectionModel().getSelectedItem();
        int quantity = Integer.parseInt(txtQuantity.getText());
        double amount = Double.parseDouble(txtAmount.getText());

        if(orderId.isEmpty() || inventoryId.isEmpty() || txtQuantity.getText().isEmpty() || txtAmount.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Please fill all the fields..!").show();
            return;
        }

        OrderItemDto orderItemDto = new OrderItemDto(
                orderItemId,
                orderId,
                inventoryId,
                quantity,
                amount
        );

        try {
            boolean isUpdated = orderItemModel.updateOrderItems(orderItemDto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Order Item Updated Successfully..!").show();
                loadOrderItemTableData();
                resetPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update Order Item..!").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to update Order Item..!").show();
        }
    }

    public void deleteBtnOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this Order Item?",
                ButtonType.YES,
                ButtonType.NO
        );
        alert.setTitle("Confirmation");

        Optional<ButtonType> response = alert.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.YES) {
            String orderItemId = ordersItemIdLabel.getText();
            try {
                boolean isDeleted = orderItemModel.deleteOrderItems(orderItemId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Order Item Deleted Successfully..!").show();
                    loadOrderItemTableData();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete Order Item..!").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to delete Order Item..!").show();
            }
        }
    }

    public void saveBtnOnAction(ActionEvent actionEvent) {
        String orderItemId = ordersItemIdLabel.getText();
        String orderId = (String) cmbOrderId1.getSelectionModel().getSelectedItem();
        String inventoryId = (String) cmbInventoryId1.getSelectionModel().getSelectedItem();
        int quantity = Integer.parseInt(txtQuantity.getText());
        double amount = Double.parseDouble(txtAmount.getText());

        if (orderId.isEmpty() || inventoryId.isEmpty() || txtQuantity.getText().isEmpty() || txtAmount.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all the fields..!").show();
            return;
        }
        txtQuantity.setStyle(txtQuantity.getStyle() + ";-fx-border-color: #7367F0");
        txtAmount.setStyle(txtAmount.getStyle() + ";-fx-border-color: #7367F0");

        boolean isValidQuantity = txtQuantity.getText().matches(quantityRegex);
        boolean isValidAmount = txtAmount.getText().matches(amountRegex);

        if (!isValidQuantity) {
            txtQuantity.setStyle(txtQuantity.getStyle() + ";-fx-border-color: red");
            new Alert(Alert.AlertType.ERROR, "Invalid Quantity format..!").show();
            return;
        }
        if (!isValidAmount) {
            txtAmount.setStyle(txtAmount.getStyle() + ";-fx-border-color: red");
            new Alert(Alert.AlertType.ERROR, "Invalid Amount format..!").show();
            return;
        }

        OrderItemDto orderItemDto = new OrderItemDto(
                orderItemId,
                orderId,
                inventoryId,
                quantity,
                amount
        );

        if (isValidAmount && isValidQuantity) {
            try {
                boolean isSaved = orderItemModel.saveOrderItems(orderItemDto);
                if (isSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "Order Item Saved Successfully..!").show();
                    loadOrderItemTableData();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to save Order Item..!").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to save Order Item..!").show();
            }
        }
    }

    public void onClickTable(MouseEvent mouseEvent) {
        OrderItemTM selectedOrderItem = (OrderItemTM) tblOrderItems.getSelectionModel().getSelectedItem();
        if (selectedOrderItem != null) {
            ordersItemIdLabel.setText(selectedOrderItem.getOrder_item_id());
            cmbOrderId1.getSelectionModel().select(selectedOrderItem.getOrder_id());
            cmbInventoryId1.getSelectionModel().select(selectedOrderItem.getInventory_id());
            txtQuantity.setText(String.valueOf(selectedOrderItem.getQuantity()));
            txtAmount.setText(String.valueOf(selectedOrderItem.getAmount()));

            //save button(id) -> disable
            btnSave.setDisable(true);
            //update button(id) -> enable
            btnUpdate.setDisable(false);
            //delete button(id) -> enable
            btnDelete.setDisable(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrderItemId.setCellValueFactory(new PropertyValueFactory<>("order_item_id"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("order_id"));
        colInventoryId.setCellValueFactory(new PropertyValueFactory<>("inventory_id"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        try {
            cmbOrderId1.setItems(FXCollections.observableArrayList(orderItemModel.getAllOrderIds()));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Order IDs..!").show();
        }

        try {
            cmbInventoryId1.setItems(FXCollections.observableArrayList(orderItemModel.getAllInventoryIds()));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Inventory IDs..!").show();
        }

        try {
            loadOrderItemTableData();
            loadNextId();
            loadOrdersIds();
            loadInventoryIds();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Order Item IDs..!").show();
        }

    }

    private void loadNextId() {
        try {
            String nextOrderItemId = orderItemModel.getNextOrderItemId();
            ordersItemIdLabel.setText(nextOrderItemId);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Order Item IDs..!").show();
        }
    }

    private void loadOrderItemTableData() throws SQLException, ClassNotFoundException {
        tblOrderItems.setItems(FXCollections.observableArrayList(
                orderItemModel.getAllOrderItems()
                        .stream()
                        .map(orderItemDto -> new OrderItemTM(
                                orderItemDto.getOrder_item_id(),
                                orderItemDto.getOrder_id(),
                                orderItemDto.getInventory_id(),
                                orderItemDto.getQuantity(),
                                orderItemDto.getAmount()
                        ))
                        .toList()
        ));

    }

    private void navigateTo(String path) {
        try {
            ancOrdersItemViewContainer.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancOrdersItemViewContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancOrdersItemViewContainer.heightProperty());

            ancOrdersItemViewContainer.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }

    private void loadOrdersIds() throws SQLException, ClassNotFoundException {
        cmbOrderId1.setItems(FXCollections.observableArrayList(orderItemModel.getAllOrderIds()));
    }

    private void loadInventoryIds() throws SQLException, ClassNotFoundException {
        cmbInventoryId1.setItems(FXCollections.observableArrayList(orderItemModel.getAllInventoryIds()));
    }

    /*private void displayItemName(String inventoryId) throws SQLException, ClassNotFoundException {
        try {
            String item = orderItemModel.getInventoryItemNameById(inventoryId);
            lblInventoryId1.setText(item);
        }catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Item Names..!").show();
        }
    }*/

    private void resetPage(){
        try {
            loadOrderItemTableData();
            loadNextId();

            //save button(id) -> enable
            btnSave.setDisable(false);
            //update button(id) -> disable
            btnUpdate.setDisable(true);
            //delete button(id) -> disable
            btnDelete.setDisable(true);

            cmbOrderId1.getSelectionModel().clearSelection();
            cmbInventoryId1.getSelectionModel().clearSelection();
            txtQuantity.setText("");
            lblInventoryId1.setText("");
            lblOrderId1.setText("");
            txtAmount.setText("");



        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load order item table").show();

        }
    }

    public void search(KeyEvent keyEvent) {
        String searchText = searchField.getText();
        if (searchText.isEmpty()) {
            try{
                loadOrderItemTableData();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to load order items").show();
            }
        }else {
            try {
                ArrayList<OrderItemDto> orderItemList = orderItemModel.searchOrderItems(searchText);
                tblOrderItems.setItems(FXCollections.observableArrayList(
                        orderItemList.stream()
                                .map(orderItemDto -> new OrderItemTM(
                                        orderItemDto.getOrder_item_id(),
                                        orderItemDto.getOrder_id(),
                                        orderItemDto.getInventory_id(),
                                        orderItemDto.getQuantity(),
                                        orderItemDto.getAmount()
                                ))
                        .toList()
                ));
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to load order items").show();
            }
        }
    }

    public void goToAddOrderLabel(MouseEvent mouseEvent) {
        navigateTo("/view/OrdersView.fxml");
    }

    public void goToAddItemsLabel(MouseEvent mouseEvent) {
        navigateTo("/view/InventoryView.fxml");
    }
}
