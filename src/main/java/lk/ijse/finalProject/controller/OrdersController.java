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
import lk.ijse.finalProject.dto.OrdersDto;
import lk.ijse.finalProject.dto.tm.EmployeeTM;
import lk.ijse.finalProject.dto.tm.OrdersTM;
import lk.ijse.finalProject.model.OrdersModel;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class OrdersController implements Initializable {

    public AnchorPane ancOrdersViewContainer;
    public Label overViewLabelButton;
    public Label ordersIdLabel;
    public TextField txtOrderDate;
    public Button btnReset;
    public Button btnUpdate;
    public Button btnDelete;
    public ComboBox cmbStatus;
    public Button btnSave;

    public TableView<OrdersTM> tblOrders;
    public TableColumn<EmployeeTM, String> colDate;
    public TableColumn<EmployeeTM, String> colOrderId;
    public TableColumn<EmployeeTM, String> colCustomerId;
    public TableColumn<EmployeeTM, String> colShipmentId;
    public TableColumn<EmployeeTM, String> colStatus;

    private final OrdersModel ordersModel = new OrdersModel();
    public ComboBox cmbCustomerId;
    public Label lblCustomerName;
    public ComboBox cmbShipmentId;
    public Label lbTrackingNumber;

    private final String datePattern = "^\\d{4}/\\d{2}/\\d{2}$";
    public TextField searchField;
    public DatePicker ordersDatePicker;


    public void labelOverViewClickOnAction(MouseEvent mouseEvent) {
        navigateTo("/view/OverView.fxml");
    }

    public void resetBtnOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    public void updateBtnOnAction(ActionEvent actionEvent) {
        String orderId = ordersIdLabel.getText();
        //String orderDate = txtOrderDate.getText();
        LocalDate orderDate = ordersDatePicker.getValue();
        String customerId = (String) cmbCustomerId.getSelectionModel().getSelectedItem();
        String shipmentId = (String) cmbShipmentId.getSelectionModel().getSelectedItem();
        String status = (String) cmbStatus.getSelectionModel().getSelectedItem();

        if (orderId.isEmpty() || orderDate == null || customerId.isEmpty() || shipmentId.isEmpty() || status == null) {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
            return;
        }

        OrdersDto ordersDto = new OrdersDto(
                orderId,
                orderDate,
                customerId,
                shipmentId,
                status
        );

        try {
            boolean isUpdated = ordersModel.updateOrders(ordersDto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Order Updated Successfully").show();
                loadOrdersTableData();
                resetPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update Order").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to update Order").show();
        }
    }

    public void deleteBtnOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this order?",
                ButtonType.YES,
                ButtonType.NO
        );
        alert.setTitle("Confirmation");

        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES) {
            String orderId = ordersIdLabel.getText();
            try {
                boolean isDeleted = ordersModel.deleteOrders(orderId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Order Deleted Successfully").show();
                    loadOrdersTableData();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete Order").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to delete Order").show();
            }
        }
    }

    public void saveBtnOnAction(ActionEvent actionEvent) {

        String orderId = ordersIdLabel.getText();
        //String orderDate = txtOrderDate.getText();
        LocalDate orderDate = ordersDatePicker.getValue();
        String customerId = (String) cmbCustomerId.getSelectionModel().getSelectedItem();
        String shipmentId = (String) cmbShipmentId.getSelectionModel().getSelectedItem();
        String status = (String) cmbStatus.getSelectionModel().getSelectedItem();

        boolean isValidDate = orderDate != null && orderDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")).matches(datePattern);

        ordersDatePicker.setStyle(txtOrderDate.getStyle() + ";-fx-border-color: #7367F0");

        if (orderId.isEmpty() || orderDate == null || customerId.isEmpty() || shipmentId.isEmpty() || status == null) {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
            return;
        }
        if (!isValidDate) {
            txtOrderDate.setStyle(txtOrderDate.getStyle() + ";-fx-border-color: red");
            new Alert(Alert.AlertType.ERROR, "Invalid date format. Use YYYY/MM/DD").show();
            return;
        }
        OrdersDto ordersDto = new OrdersDto(
                orderId,
                orderDate,
                customerId,
                shipmentId,
                status
        );

        if (isValidDate){
            try {
                boolean isSaved = ordersModel.saveOrders(ordersDto);
                if (isSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "Order Saved Successfully").show();
                    loadOrdersTableData();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to save Order").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to save Order").show();
            }
        }


    }

    public void onClickTable(MouseEvent mouseEvent) {
        OrdersTM selectedItem = tblOrders.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            ordersIdLabel.setText(selectedItem.getOrder_id());
            ordersDatePicker.setValue(selectedItem.getOrder_date());
            cmbCustomerId.setValue(selectedItem.getCustomer_id());
            cmbShipmentId.setValue(selectedItem.getShipment_id());
            cmbStatus.setValue(selectedItem.getStatus());

            //save button(id) -> disable
            btnSave.setDisable(true);
            //update button(id) -> enable
            btnUpdate.setDisable(false);
            //delete button(id) -> enable
            btnDelete.setDisable(false);
        }
    }

    private void navigateTo(String path) {
        try {
            ancOrdersViewContainer.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancOrdersViewContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancOrdersViewContainer.heightProperty());

            ancOrdersViewContainer.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }

    private void resetPage() {
        try {
            loadOrdersTableData();
            loadNextId();

            //save button(id) -> enable
            btnSave.setDisable(false);
            //update button(id) -> disable
            btnUpdate.setDisable(true);
            //delete button(id) -> disable
            btnDelete.setDisable(true);

            //txtOrderDate.setText("");
            //lblCustomerName.setText("");
            //lbTrackingNumber.setText("");
            ordersDatePicker.setValue(null);
            cmbCustomerId.getSelectionModel().clearSelection();
            cmbShipmentId.getSelectionModel().clearSelection();
            cmbStatus.getSelectionModel().clearSelection();


        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to Reset").show();
        }

    }

    private void loadOrdersTableData() throws SQLException, ClassNotFoundException {
        tblOrders.setItems(FXCollections.observableArrayList(
                ordersModel.getAllOrders()
                        .stream()
                        .map(ordersDto -> new OrdersTM(
                                ordersDto.getOrder_id(),
                                ordersDto.getOrder_date(),
                                ordersDto.getCustomer_id(),
                                ordersDto.getShipment_id(),
                                ordersDto.getStatus()
                        ))
                        .toList()
        ));

    }

    private void loadNextId() {
        try {
            String nextId = ordersModel.getNextOrderId();
            ordersIdLabel.setText(nextId);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load next Employee ID").show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("order_id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("order_date"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        colShipmentId.setCellValueFactory(new PropertyValueFactory<>("shipment_id"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        cmbStatus.setItems(FXCollections.observableArrayList("Pending", "Shipped", "Delivered", "Cancelled"));
        try {
            cmbCustomerId.setItems(FXCollections.observableArrayList(ordersModel.getAllCustomerIds()));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load customers").show();
        }

        try {
            cmbShipmentId.setItems(FXCollections.observableArrayList(ordersModel.getAllShipmentIds()));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load shipments").show();
        }

        try {
            loadOrdersTableData();
            loadNextId();
            loadCustomerIds();
            loadShipmentIds();
            setOrdersDateTimePicker();
            /*cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    displayCustomerName((String) newValue);
                }
            });
            cmbShipmentId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    displayTrackingNumber((String) newValue);
                }
            });*/

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load customers").show();
        }

    }

    private void loadCustomerIds() throws SQLException, ClassNotFoundException {
        cmbCustomerId.setItems(FXCollections.observableArrayList(ordersModel.getAllCustomerIds()));
    }

    private void loadShipmentIds() throws SQLException, ClassNotFoundException {
        cmbShipmentId.setItems(FXCollections.observableArrayList(ordersModel.getAllShipmentIds()));
    }

    /*private void displayCustomerName(String customerId) {
        try {
            String customerName = ordersModel.getCustomerNameById(customerId);
            lblCustomerName.setText(customerName);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load customer name").show();
        }
    }

    private void displayTrackingNumber(String shipmentId) {
        try {
            String trackingNumber = ordersModel.getTrackingNumberById(shipmentId);
            lbTrackingNumber.setText(trackingNumber);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load tracking number").show();
        }
    }*/

    public void search(KeyEvent keyEvent) {
        String searchText = searchField.getText();
        if (searchText.isEmpty()) {
            try {
                loadOrdersTableData();
            }catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to load orders").show();
            }
        }else {
            try {
                ArrayList<OrdersDto> ordersList = ordersModel.searchOrders(searchText);
                tblOrders.setItems(FXCollections.observableArrayList(
                        ordersList.stream()
                                .map(ordersDto -> new OrdersTM(
                                        ordersDto.getOrder_id(),
                                        ordersDto.getOrder_date(),
                                        ordersDto.getCustomer_id(),
                                        ordersDto.getShipment_id(),
                                        ordersDto.getStatus()
                                        ))
                                .toList()
                ));
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to load orders").show();
            }
        }
    }

    public void goToAddCustomersLabel(MouseEvent mouseEvent) {
        navigateTo("/view/CustomerView.fxml");
    }

    public void goToAddShipmentLabel(MouseEvent mouseEvent) {
        navigateTo("/view/ShipmentView.fxml");
    }

    private void setOrdersDateTimePicker() {
        // Set prompt text to today's date
        ordersDatePicker.setPromptText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // Set up row click to load date into DatePicker
        tblOrders.setRowFactory(tv -> {
            TableRow<OrdersTM> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    OrdersTM rowData = row.getItem();
                    // Assuming OrdersTM has a getDate() method returning String in yyyy-MM-dd or similar format
                    try {
                        LocalDate date = rowData.getOrder_date();
                        ordersDatePicker.setValue(date);
                    } catch (Exception e) {
                        ordersDatePicker.setValue(null);
                    }
                }
            });
            return row;
        });
    }
}