package lk.ijse.finalProject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;



public class OverViewController {
    private AnchorPane ancMainContainer;

    public void goToCustomerPageOnAction(ActionEvent actionEvent) {

    }

    public void goToDiscountPageOnAction(ActionEvent actionEvent) {
    }

    public void goToEmployeePageOnAction(ActionEvent actionEvent) {
    }

    public void goToInventoryPageOnAction(ActionEvent actionEvent) {
    }

    public void goToMaterialPageOnAction(ActionEvent actionEvent) {
    }

    public void goToMaterialUsagePageOnAction(ActionEvent actionEvent) {
    }

    public void goToOrderItemsPageOnAction(ActionEvent actionEvent) {
    }

    public void goToOrdersPageOnAction(ActionEvent actionEvent) {
    }

    public void goToPaymentsPageOnAction(ActionEvent actionEvent) {
    }

    public void goToProductionPageOnAction(ActionEvent actionEvent) {
    }

    public void goToProductionTaskPageOnAction(ActionEvent actionEvent) {
    }

    public void goToShipmentPageOnAction(ActionEvent actionEvent) {
    }

    public void goToSuppliersPageOnAction(ActionEvent actionEvent) {
    }

    public void goToSuppliesPageOnAction(ActionEvent actionEvent) {
    }

    public void goToTasksPageOnAction(ActionEvent actionEvent) {
    }
    private void navigateTo(String path) {
        try {
            ancMainContainer.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancMainContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancMainContainer.heightProperty());

            ancMainContainer.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }
}
