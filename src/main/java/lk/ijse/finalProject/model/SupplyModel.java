package lk.ijse.finalProject.model;

import lk.ijse.finalProject.db.DBConnection;
import lk.ijse.finalProject.dto.SupplyDto;
import lk.ijse.finalProject.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplyModel {
    public String saveSupply(SupplyDto supplyDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "INSERT INTO supply VALUES (?,?,?,?,?)",
                supplyDto.getSupply_id(),
                supplyDto.getSupplier_id(),
                supplyDto.getMaterial_id(),
                supplyDto.getAmount(),
                supplyDto.getQuantity()
        );
    }
    public String updateSupply(SupplyDto supplyDto) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "UPDATE supply SET supplier_id = ?, material_id = ?, amount = ?, quantity = ?WHERE supply_id= ?",
                supplyDto.getSupplier_id(),
                supplyDto.getMaterial_id(),
                supplyDto.getAmount(),
                supplyDto.getQuantity(),
                supplyDto.getSupply_id()

        );
    }

    public String deleteSupply(String supply_id) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute("DELETE FROM supply WHERE supply_id = ?", supply_id);
    }

    public SupplyDto searchSupply(String supply_id) throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM supply WHERE supply_id = ?", supply_id);

        if(rst.next()){
            SupplyDto dto = new SupplyDto(
                    rst.getString("supply_id"),
                    rst.getString("supplier_id"),
                    rst.getString("material_id"),
                    rst.getDouble("amount"),
                    rst.getInt("quantity")
            );
            return dto;
        }
        return null;
    }
    public ArrayList<SupplyDto> getAllSupply() throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM supply");
        ArrayList<SupplyDto> supplyDto = new ArrayList<>();

        while (rst.next()) {
            SupplyDto dto = new SupplyDto(
                    rst.getString("supply_id"),
                    rst.getString("supplier_id"),
                    rst.getString("material_id"),
                    rst.getDouble("amount"),
                    rst.getInt("quantity")
            );
            supplyDto.add(dto);
        }
        return supplyDto;
    }

    public String getNextSupplyId() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT supply_id FROM supply ORDER BY supply_id DESC LIMIT 1");
        String tableCharacter = "SUPPLY"; // Use any character Ex:- customer table for C, item table for I
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
