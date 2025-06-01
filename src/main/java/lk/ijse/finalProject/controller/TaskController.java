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
import lk.ijse.finalProject.dto.TaskDto;
import lk.ijse.finalProject.dto.tm.TaskTM;
import lk.ijse.finalProject.model.TaskModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class TaskController implements Initializable {

    public AnchorPane ancTaskContainer;
    public TextField searchField;
    public Label taskIdLabel;
    public ComboBox cmbEmployeeId;
    public TextField txtDescription;
    public ComboBox cmbStatus;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public TableView<TaskTM> tblTask;
    public TableColumn<TaskTM, String> colTaskId;
    public TableColumn<TaskTM, String> colEmployeeId;
    public TableColumn<TaskTM, String> colDescription;
    public TableColumn<TaskTM, String> colStatus;

    private final TaskModel taskModel = new TaskModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colTaskId.setCellValueFactory(new PropertyValueFactory<>("task_id"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employee_id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        try {
            cmbEmployeeId.setItems(FXCollections.observableArrayList(taskModel.getAllEmployeeIds()));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load employee IDs").show();
        }
        try {
            cmbStatus.setItems(FXCollections.observableArrayList("Pending", "In Progress", "Completed"));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load status options").show();
        }

        try {
            loadTaskTableData();
            loadNextId();
            loadStatusOptions();
            loadEmployeeId();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Fail to Initialize");
        }
    }


    public void labelOverViewClickOnAction(MouseEvent mouseEvent) {
        navigateTo("/view/OverView.fxml");
    }

    public void search(KeyEvent keyEvent) {
        String searchText = searchField.getText();
        if (searchText.isEmpty()) {
            try {
                loadTaskTableData();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to load tasks").show();
            }
        } else {
            try {
                tblTask.setItems(FXCollections.observableArrayList(
                        taskModel.searchTasks(searchText)
                                .stream()
                                .map(
                                        task -> new TaskTM(
                                                task.getTask_id(),
                                                task.getEmployee_id(),
                                                task.getDescription(),
                                                task.getStatus()
                                        )).toList()
                ));
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to search tasks").show();
            }
        }
    }

    public void goToAddEmployeePage(MouseEvent mouseEvent) {
        navigateTo("/view/EmployeeView.fxml");
    }

    public void saveBtnOnAction(ActionEvent actionEvent) {
        String taskId = taskIdLabel.getText();
        String employeeId = cmbEmployeeId.getSelectionModel().getSelectedItem().toString();
        String description = txtDescription.getText();
        String status = cmbStatus.getSelectionModel().getSelectedItem().toString();

        if (taskId.isEmpty() || employeeId.isEmpty() || description.isEmpty() || status.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
            return;
        }

        TaskDto taskDto = new TaskDto(
                taskId,
                employeeId,
                description,
                status
        );

        try {
            boolean isSaved = taskModel.saveTasks(taskDto);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Task saved successfully").show();
                resetPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save task").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error saving task: " + e.getMessage()).show();
        }
    }

    public void updateBtnOnAction(ActionEvent actionEvent) {
        String taskId = taskIdLabel.getText();
        String employeeId = cmbEmployeeId.getSelectionModel().getSelectedItem().toString();
        String description = txtDescription.getText();
        String status = cmbStatus.getSelectionModel().getSelectedItem().toString();

        if (taskId.isEmpty() || employeeId.isEmpty() || description.isEmpty() || status.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
            return;
        }

        TaskDto taskDto = new TaskDto(
                taskId,
                employeeId,
                description,
                status
        );

        try {
            boolean isUpdated = taskModel.updateTasks(taskDto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Task updated successfully").show();
                resetPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update task").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error updating task: " + e.getMessage()).show();
        }
    }

    public void deleteBtnOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this task?",
                ButtonType.YES,
                ButtonType.NO);
        alert.setTitle("Delete Task");
        Optional<ButtonType> response = alert.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.YES) {
            String taskId = taskIdLabel.getText();
            try {
                boolean isDeleted = taskModel.deleteTasks(taskId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Task deleted successfully").show();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete task").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error deleting task: " + e.getMessage()).show();
            }
        }
    }

    public void resetBtnOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    public void onClickTable(MouseEvent mouseEvent) {
        TaskTM selectedTask = tblTask.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            taskIdLabel.setText(selectedTask.getTask_id());
            cmbEmployeeId.getSelectionModel().select(selectedTask.getEmployee_id());
            txtDescription.setText(selectedTask.getDescription());
            cmbStatus.getSelectionModel().select(selectedTask.getStatus());

            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    private void navigateTo(String path) {
        try {
            ancTaskContainer.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancTaskContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancTaskContainer.heightProperty());

            ancTaskContainer.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }

    private void loadTaskTableData() throws SQLException, ClassNotFoundException {
        tblTask.setItems(FXCollections.observableArrayList(
                taskModel.getAllTasks()
                        .stream()
                        .map(
                                task -> new TaskTM(
                                        task.getTask_id(),
                                        task.getEmployee_id(),
                                        task.getDescription(),
                                        task.getStatus()
                                )).toList()
        ));
    }

    private void loadNextId() {
        try {
            String nextId = taskModel.getNextTaskId();
            taskIdLabel.setText(nextId);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load next task ID").show();
        }
    }

    private void loadEmployeeId(){
        try {
            cmbEmployeeId.setItems(FXCollections.observableArrayList(taskModel.getAllEmployeeIds()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetPage() {
        try {
            loadTaskTableData();
            loadNextId();

            taskIdLabel.setText("");
            cmbEmployeeId.getSelectionModel().clearSelection();
            txtDescription.setText("");
            cmbStatus.getSelectionModel().clearSelection();
            btnSave.setDisable(false);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to reset page").show();
        }
    }

    private void loadStatusOptions() {
        try {
            cmbStatus.setItems(FXCollections.observableArrayList("Pending", "In Progress", "Completed"));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load status options").show();
        }
    }
}