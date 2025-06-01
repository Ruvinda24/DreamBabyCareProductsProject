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
import lk.ijse.finalProject.dto.ShipmentDto;
import lk.ijse.finalProject.dto.tm.ShipmentTM;
import lk.ijse.finalProject.model.ShipmentModel;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class ShipmentController implements Initializable {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colShipmentId.setCellValueFactory(new PropertyValueFactory<>("shipment_id"));
        colTrackingNumber.setCellValueFactory(new PropertyValueFactory<>("tracking_number"));
        colShipmentDate.setCellValueFactory(new PropertyValueFactory<>("shipment_date"));

        try {
            loadShipmentTableData();
            loadNextId();
            setSupplierDateTimePicker();
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load shipment data").show();
        }

    }

    private void setSupplierDateTimePicker() {
        shipmentDatePicker.setPromptText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        tblShipment.setRowFactory(tv -> {
            TableRow<ShipmentTM> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    ShipmentTM selectedShipment = row.getItem();
                    try {
                        LocalDate date = selectedShipment.getShipment_date();
                        shipmentDatePicker.setValue(date);
                    }catch (Exception e){
                        e.printStackTrace();
                        new Alert(Alert.AlertType.ERROR, "Failed to set shipment date").show();
                        shipmentDatePicker.setValue(null);
                    }
                }
            });
            return row;
        });
    }

    private void loadNextId() {
        try {
            String nextId = shipmentModel.getNextShipmentId();
            shipmentIdLabel.setText(nextId);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load next shipment ID").show();
        }
    }

    private void loadShipmentTableData() throws SQLException, ClassNotFoundException {
        tblShipment.setItems(FXCollections.observableArrayList(
                shipmentModel.getAllShipments()
                        .stream()
                        .map(
                                shipment -> new ShipmentTM(
                                        shipment.getShipment_id(),
                                        shipment.getTracking_number(),
                                        shipment.getShipment_date()
                                )).toList()
        ));
    }


    public void labelOverViewClickOnAction(MouseEvent mouseEvent) {
        navigateTo("/view/OverView.fxml");
    }

    public void search(KeyEvent keyEvent) {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            try {
                loadShipmentTableData();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to load shipment data").show();
            }
            return;
        }

        try {
            tblShipment.setItems(FXCollections.observableArrayList(
                    shipmentModel.searchShipments(searchText)
                            .stream()
                            .map(
                                    shipment -> new ShipmentTM(
                                            shipment.getShipment_id(),
                                            shipment.getTracking_number(),
                                            shipment.getShipment_date()
                                    )).toList()
            ));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to search shipments").show();
        }
    }

    public void saveBtnOnAction(ActionEvent actionEvent) {
        String shipmentId = shipmentIdLabel.getText();
        String trackingNumber = txtTrackingNumber.getText();
        LocalDate shipmentDate = shipmentDatePicker.getValue();

        if (shipmentId.isEmpty() || trackingNumber.isEmpty() || shipmentDate == null) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields").show();
            return;
        }

        ShipmentDto shipmentDto = new ShipmentDto(
                shipmentId,
                trackingNumber,
                shipmentDate
        );

        try {
            boolean isSaved = shipmentModel.saveShipments(shipmentDto);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Shipment saved successfully").show();
                loadShipmentTableData();
                resetPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save shipment").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to save shipment").show();
        }
    }

    public void updateBtnOnAction(ActionEvent actionEvent) {
        String shipmentId = shipmentIdLabel.getText();
        String trackingNumber = txtTrackingNumber.getText();
        LocalDate shipmentDate = shipmentDatePicker.getValue();

        if (shipmentId.isEmpty() || trackingNumber.isEmpty() || shipmentDate.equals(null)) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields").show();
            return;
        }
        ShipmentDto shipmentDto = new ShipmentDto(
                shipmentId,
                trackingNumber,
                shipmentDate
        );

        try {
                boolean isUpdated = shipmentModel.updateShipments(shipmentDto);
                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "Shipment updated successfully").show();
                    loadShipmentTableData();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to update shipment").show();
                }
                } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to update shipment").show();
            }


    }

    public void deleteBtnOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this shipment?",
                ButtonType.YES,
                ButtonType.NO);
        alert.setTitle("Delete Shipment");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            String shipmentId = shipmentIdLabel.getText();
            if (shipmentId != null) {
                try {
                    shipmentModel.deleteShipments(shipmentId);
                    loadShipmentTableData();
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Shipment deleted successfully").show();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Failed to delete shipment").show();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select a shipment to delete").show();
            }
        }
    }

    public void resetBtnOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    public void onClickTable(MouseEvent mouseEvent) {
        ShipmentTM selectedShipment = tblShipment.getSelectionModel().getSelectedItem();
        if (selectedShipment != null) {
            shipmentIdLabel.setText(selectedShipment.getShipment_id());
            txtTrackingNumber.setText(selectedShipment.getTracking_number());
            shipmentDatePicker.setValue(selectedShipment.getShipment_date());
            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    private void navigateTo(String path) {
        try {
            ancShipmentContainer.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancShipmentContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancShipmentContainer.heightProperty());

            ancShipmentContainer.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }

    private void resetPage() {

        try {
            loadShipmentTableData();
            loadNextId();

            txtTrackingNumber.clear();
            shipmentDatePicker.setValue(null);
            btnSave.setDisable(false);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to reset page").show();
        }
    }
}
