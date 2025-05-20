package lk.ijse.finalProject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;



public class OverViewController {

    public AnchorPane ancOverViewContainer;

    public void goToCustomerPageOnAction(ActionEvent actionEvent) {
        navigateTo("/view/CustomerView.fxml");
    }

    public void goToDiscountPageOnAction(ActionEvent actionEvent) {
        navigateTo("/view/DiscountView.fxml");
    }

    public void goToEmployeePageOnAction(ActionEvent actionEvent) {
        navigateTo("/view/EmployeeView.fxml");
    }

    public void goToInventoryPageOnAction(ActionEvent actionEvent) {
    }

    public void goToMaterialPageOnAction(ActionEvent actionEvent) {
    }

    public void goToMaterialUsagePageOnAction(ActionEvent actionEvent) {
    }

    public void goToOrderItemsPageOnAction(ActionEvent actionEvent) {
        navigateTo("/view/OrderItemView.fxml");
    }

    public void goToOrdersPageOnAction(ActionEvent actionEvent) {
        navigateTo("/view/OrdersView.fxml");
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
        navigateTo("/view/SupplyView.fxml");
    }

    public void goToTasksPageOnAction(ActionEvent actionEvent) {
    }
    private void navigateTo(String path) {
        try {
            ancOverViewContainer.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancOverViewContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancOverViewContainer.heightProperty());

            anchorPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            ancOverViewContainer.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }

    public void goToDashboard(MouseEvent mouseEvent) {
        navigateTo("/view/DashBoardView.fxml");
    }
}
