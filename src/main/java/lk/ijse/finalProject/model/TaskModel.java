package lk.ijse.finalProject.model;

import lk.ijse.finalProject.db.DBConnection;
import lk.ijse.finalProject.dto.TaskDto;
import lk.ijse.finalProject.util.CrudUtil;

import java.net.CacheRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TaskModel{


    public boolean saveTasks(TaskDto taskDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "INSERT INTO task VALUES (?,?,?,?)",
                taskDto.getTask_id(),
                taskDto.getEmployee_id(),
                taskDto.getDescription(),
                taskDto.getStatus()
        );
    }
    public boolean updateTasks(TaskDto taskDto) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "UPDATE task SET employee_id = ?, description = ?, status = ?WHERE task_id= ?",
                taskDto.getEmployee_id(),
                taskDto.getDescription(),
                taskDto.getStatus(),
                taskDto.getTask_id()
        );
    }

    public boolean deleteTasks(String task_id) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute("DELETE FROM task WHERE task_id = ?", task_id);
    }

    public ArrayList<TaskDto> searchTasks(String searchText) throws ClassNotFoundException, SQLException{

        ArrayList<TaskDto> taskDtoArrayList = new ArrayList<>();
        String sql = "SELECT * FROM task WHERE task_id LIKE ? OR employee_id LIKE ? OR description LIKE ? OR status LIKE ?";
        String pattern = "%" + searchText + "%";
        ResultSet rst = CrudUtil.execute(sql, pattern, pattern, pattern, pattern);

        while(rst.next()){
            TaskDto dto = new TaskDto(
                    rst.getString("task_id"),
                    rst.getString("employee_id"),
                    rst.getString("description"),
                    rst.getString("status")
            );
            taskDtoArrayList.add(dto);
        }
        return taskDtoArrayList;
    }
    public ArrayList<TaskDto> getAllTasks() throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM task");
        ArrayList<TaskDto> taskDto = new ArrayList<>();

        while (rst.next()) {
            TaskDto dto = new TaskDto(
                    rst.getString("task_id"),
                    rst.getString("employee_id"),
                    rst.getString("description"),
                    rst.getString("status")
            );
            taskDto.add(dto);
        }
        return taskDto;
    }

    public String getNextTaskId() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT task_id FROM task ORDER BY task_id DESC LIMIT 1");
        String tableCharacter = "TASK"; // Use any character Ex:- customer table for C, item table for I
        if (resultSet.next()) {
            String lastId = resultSet.getString(1); // "C001"
            String lastIdNumberString = lastId.substring(tableCharacter.length()); // "001"
            int lastIdNumber = Integer.parseInt(lastIdNumberString); // 1
            int nextIdNUmber = lastIdNumber + 1; // 2
            // "C002"
            return String.format(tableCharacter + "%03d", nextIdNUmber);
        }
        // No data recode in table so return initial primary key
        return tableCharacter + "001";
    }
    public ArrayList<String> getAllEmployeeIds() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT employee_id FROM employee");
        ArrayList<String> employeeIds = new ArrayList<>();
        while (resultSet.next()) {
            employeeIds.add(resultSet.getString("employee_id"));
        }
        return employeeIds;
    }

}
