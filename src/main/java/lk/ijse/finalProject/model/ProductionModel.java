package lk.ijse.finalProject.model;


import lk.ijse.finalProject.dto.ProductionDto;
import lk.ijse.finalProject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductionModel {


    public String saveProductions(ProductionDto productionDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "INSERT INTO production VALUES (?,?,?,?)",
                productionDto.getProduction_id(),
                productionDto.getInventory_id(),
                productionDto.getDescription(),
                productionDto.getStatus()
        );
    }

    public String updateProductions(ProductionDto productionDto) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "UPDATE production SET inventory_id = ?, description = ?, status = ?WHERE production_id= ?",
                productionDto.getInventory_id(),
                productionDto.getDescription(),
                productionDto.getStatus(),
                productionDto.getProduction_id()
        );
    }

    public String deleteProductions(String production_id) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute("DELETE FROM production WHERE production_id = ?", production_id);
    }

    public ProductionDto searchProductions(String production_id) throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM production WHERE production_id = ?", production_id);

        if(rst.next()){
            ProductionDto dto = new ProductionDto(
                    rst.getString("production_id"),
                    rst.getString("inventory_id"),
                    rst.getString("description"),
                    rst.getString("status")
            );
            return dto;
        }
        return null;
    }
    public ArrayList<ProductionDto> getAllProductions() throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM production");
        ArrayList<ProductionDto> productionDto = new ArrayList<>();

        while (rst.next()) {
            ProductionDto dto = new ProductionDto(
                    rst.getString("production_id"),
                    rst.getString("inventory_id"),
                    rst.getString("description"),
                    rst.getString("status")
            );
            productionDto.add(dto);
        }
        return productionDto;
    }

    public String getNextProductionId() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT production_id FROM production ORDER BY production_id DESC LIMIT 1");
        String tableCharacter = "PROD"; // Use any character Ex:- customer table for C, item table for I
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
