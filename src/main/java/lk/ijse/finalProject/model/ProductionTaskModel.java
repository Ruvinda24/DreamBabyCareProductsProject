package lk.ijse.finalProject.model;

import lk.ijse.finalProject.db.DBConnection;
import lk.ijse.finalProject.dto.ProductionTaskDto;
import lk.ijse.finalProject.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductionTaskModel {

    public String saveProductionTasks(ProductionTaskDto productionTaskDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "INSERT INTO production_task VALUES (?,?,?)",
                productionTaskDto.getProduction_task_id(),
                productionTaskDto.getProduction_id(),
                productionTaskDto.getTask_id()
        );
    }

    public String updateProductionTasks(ProductionTaskDto productionTaskDto) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "UPDATE production_task SET production_id = ?, task_id = ?WHERE production_task_id= ?",
                productionTaskDto.getProduction_id(),
                productionTaskDto.getTask_id(),
                productionTaskDto.getProduction_task_id()
        );
    }

    public String deleteProductionTasks(String production_task_id) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute("DELETE FROM production_task WHERE production_task_id = ?", production_task_id);

    }

    public ProductionTaskDto searchProductionTasks(String production_task_id) throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM production_task WHERE production_task_id = ?", production_task_id);

        if(rst.next()){
            ProductionTaskDto dto = new ProductionTaskDto(
                    rst.getString("production_task_id"),
                    rst.getString("production_id"),
                    rst.getString("task_id")
            );
            return dto;
        }
        return null;
    }
    public ArrayList<ProductionTaskDto> getAllProductionTasks() throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM production_task");
        ArrayList<ProductionTaskDto> productionTaskDto = new ArrayList<>();

        while (rst.next()) {
            ProductionTaskDto dto = new ProductionTaskDto(
                    rst.getString("production_task_id"),
                    rst.getString("production_id"),
                    rst.getString("task_id")
            );
            productionTaskDto.add(dto);
        }
        return productionTaskDto;
    }

    public String getNextProductionTaskId() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT production_task_id FROM production_task ORDER BY production_task_id DESC LIMIT 1");
        String tableCharacter = "PTASK"; // Use any character Ex:- customer table for C, item table for I
        if (resultSet.next()) {
            String lastId = resultSet.getString(1); // "C001"
            String lastIdNumberString = lastId.substring(1); // "001"
            int lastIdNumber = Integer.parseInt(lastIdNumberString); // 1
            int nextIdNUmber = lastIdNumber + 1; // 2
            // "C002"
            return String.format(tableCharacter + "%03d", nextIdNUmber);
        }
        // No data recode in table so return initial primary key
        return tableCharacter + "001";
    }

}
