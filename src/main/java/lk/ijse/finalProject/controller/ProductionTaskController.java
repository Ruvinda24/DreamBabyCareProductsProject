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
import lk.ijse.finalProject.dto.ProductionTaskDto;
import lk.ijse.finalProject.dto.tm.ProductionTaskTM;
import lk.ijse.finalProject.model.ProductionTaskModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductionTaskController implements Initializable {

    public AnchorPane ancProductionTaskContainer;
    public TextField searchField;
    public Label productionTaskIdLabel;
    public ComboBox cmbProductionId;
    public ComboBox cmbTaskID;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public TableView<ProductionTaskTM> tblProductionTask;
    public TableColumn<ProductionTaskTM,String> colProductionTaskId;
    public TableColumn<ProductionTaskTM,String> colTaskId;
    public TableColumn<ProductionTaskTM,String> colProductionId;

    private final ProductionTaskModel productionTaskModel = new ProductionTaskModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colProductionTaskId.setCellValueFactory(new PropertyValueFactory<>("production_task_id"));
        colProductionId.setCellValueFactory(new PropertyValueFactory<>("production_id"));
        colTaskId.setCellValueFactory(new PropertyValueFactory<>("task_id"));

        try {
            loadProductionTaskTableData();
            loadNextId();
            loadProductionIdComboBox();
            loadTaskIdComboBox();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load data.").show();
        }

    }

    public void labelOverViewClickOnAction(MouseEvent mouseEvent) {
        navigateTo("/view/OverView.fxml");
    }

    public void search(KeyEvent keyEvent) {
        String searchText = searchField.getText().toLowerCase();
        if (searchText.isEmpty()) {
            try {
                loadProductionTaskTableData();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to load data.").show();
            }
        } else {
            try {
                tblProductionTask.setItems(FXCollections.observableArrayList(
                        productionTaskModel.searchProductionTasks(searchText)
                                .stream()
                                .map(
                                        productionTask -> new ProductionTaskTM(
                                                productionTask.getProduction_task_id(),
                                                productionTask.getProduction_id(),
                                                productionTask.getTask_id()
                                        )).toList()
                ));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to search data.").show();
            }
        }
    }

    public void goToAddProductionPage(MouseEvent mouseEvent) {
        navigateTo("/view/ProductionView.fxml");
    }

    public void goToAddTaskPage(MouseEvent mouseEvent) {
        navigateTo("/view/TaskView.fxml");
    }

    public void saveBtnOnAction(ActionEvent actionEvent) {
        String productionTaskId = productionTaskIdLabel.getText();
        String productionId = cmbProductionId.getSelectionModel().getSelectedItem().toString();
        String taskId = cmbTaskID.getSelectionModel().getSelectedItem().toString();

        if (productionTaskId.isEmpty() || productionId.isEmpty() || taskId.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields.").show();
            return;
        }

        ProductionTaskDto productionTaskDto = new ProductionTaskDto(
                productionTaskId,
                productionId,
                taskId
        );

        try {
            boolean isSaved = productionTaskModel.saveProductionTasks(productionTaskDto);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Production task saved successfully.").show();
                resetPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save production task.").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while saving the production task.").show();
        }
    }

    public void updateBtnOnAction(ActionEvent actionEvent) {
        String productionTaskId = productionTaskIdLabel.getText();
        String productionId = cmbProductionId.getSelectionModel().getSelectedItem().toString();
        String taskId = cmbTaskID.getSelectionModel().getSelectedItem().toString();

        if (productionTaskId.isEmpty() || productionId.isEmpty() || taskId.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields.").show();
            return;
        }

        ProductionTaskDto productionTaskDto = new ProductionTaskDto(
                productionTaskId,
                productionId,
                taskId
        );

        try {
            boolean isUpdated = productionTaskModel.updateProductionTasks(productionTaskDto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Production task updated successfully.").show();
                resetPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update production task.").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while updating the production task.").show();
        }
    }

    public void deleteBtnOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this production task?",
                ButtonType.YES,
                ButtonType.NO);
        alert.setTitle("Delete Confirmation");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            String productionTaskId = productionTaskIdLabel.getText();
            if (productionTaskId != null) {
                try {
                    boolean isDeleted = productionTaskModel.deleteProductionTasks(productionTaskId);
                    if (isDeleted) {
                        new Alert(Alert.AlertType.INFORMATION, "Production task deleted successfully.").show();
                        resetPage();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to delete production task.").show();
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Error occurred while deleting the production task.").show();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select a production task to delete.").show();
            }
        }
    }

    public void resetBtnOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    public void onClickTable(MouseEvent mouseEvent) {
        ProductionTaskTM selectedItem = tblProductionTask.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            productionTaskIdLabel.setText(selectedItem.getProduction_task_id());
            cmbProductionId.getSelectionModel().select(selectedItem.getProduction_id());
            cmbTaskID.getSelectionModel().select(selectedItem.getTask_id());
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a row from the table.").show();
        }
    }

    private void navigateTo(String path) {
        try {
            ancProductionTaskContainer.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancProductionTaskContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancProductionTaskContainer.heightProperty());

            ancProductionTaskContainer.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }

    private void resetPage() {
        try {
            loadProductionTaskTableData();
            loadNextId();

            productionTaskIdLabel.setText("");
            cmbProductionId.getSelectionModel().clearSelection();
            cmbTaskID.getSelectionModel().clearSelection();
            searchField.clear();
            tblProductionTask.getItems().clear();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to reset the page.").show();
        }
    }

    private void loadNextId() {
        try {
            String nextId = productionTaskModel.getNextProductionTaskId();
            productionTaskIdLabel.setText(nextId);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load next ID.").show();
        }
    }

    private void loadTaskIdComboBox() {
        try {
            cmbTaskID.setItems(FXCollections.observableArrayList(
                    productionTaskModel.getAllTaskIds()
            ));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Task IDs.").show();
        }
    }

    private void loadProductionIdComboBox() {
        try {
            cmbProductionId.setItems(FXCollections.observableArrayList(
                    productionTaskModel.getAllProductionIds()
            ));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Production IDs.").show();
        }
    }

    private void loadProductionTaskTableData() throws SQLException, ClassNotFoundException {
        tblProductionTask.setItems(FXCollections.observableArrayList(
                productionTaskModel.getAllProductionTasks()
                        .stream()
                        .map(
                                productionTask -> new ProductionTaskTM(
                                        productionTask.getProduction_task_id(),
                                        productionTask.getProduction_id(),
                                        productionTask.getTask_id()
                                )).toList()
        ));
    }
}
