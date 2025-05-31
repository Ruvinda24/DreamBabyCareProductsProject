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
        navigateTo("/view/InventoryView.fxml");
    }

    public void goToMaterialPageOnAction(ActionEvent actionEvent) {
        navigateTo("/view/MaterialView.fxml");
    }

    public void goToMaterialUsagePageOnAction(ActionEvent actionEvent) {
        navigateTo("/view/MaterialUsageView.fxml");
    }

    public void goToOrderItemsPageOnAction(ActionEvent actionEvent) {
        navigateTo("/view/OrderItemView.fxml");
    }

    public void goToOrdersPageOnAction(ActionEvent actionEvent) {
        navigateTo("/view/OrdersView.fxml");
    }

    public void goToPaymentsPageOnAction(ActionEvent actionEvent) {
        navigateTo("/view/PaymentView.fxml");
    }

    public void goToProductionPageOnAction(ActionEvent actionEvent) {
        navigateTo("/view/ProductionView.fxml");
    }

    public void goToProductionTaskPageOnAction(ActionEvent actionEvent) {
        navigateTo("/view/ProductionTaskView.fxml");
    }

    public void goToShipmentPageOnAction(ActionEvent actionEvent) {
        navigateTo("/view/ShipmentView.fxml");
    }

    public void goToSuppliersPageOnAction(ActionEvent actionEvent) {
        navigateTo("/view/SupplierView.fxml");
    }

    public void goToSuppliesPageOnAction(ActionEvent actionEvent) {
        navigateTo("/view/SupplyView.fxml");
    }

    public void goToTasksPageOnAction(ActionEvent actionEvent) {
        navigateTo("/view/TasksView.fxml");
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
