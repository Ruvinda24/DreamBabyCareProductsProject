package lk.ijse.finalProject.model;

import lk.ijse.finalProject.db.DBConnection;
import lk.ijse.finalProject.dto.InventoryDto;
import lk.ijse.finalProject.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InventoryModel {

    public String saveItems(InventoryDto inventoryDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "INSERT INTO inventory VALUES (?,?,?,?,?,?,?)",
                inventoryDto.getInventory_id(),
                inventoryDto.getItem_name(),
                inventoryDto.getPrinted_embroidered(),
                inventoryDto.getSize(),
                inventoryDto.getUnit_price(),
                inventoryDto.getQuantity_available(),
                inventoryDto.getStored_location()
        );
    }
    public String updateItems(InventoryDto inventoryDto) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "UPDATE inventory SET item_name = ?, printed_embroidered = ?, size = ?, unit_price = ?, quantity_available = ? ,stored_location = ? WHERE inventory_id= ?",
                inventoryDto.getItem_name(),
                inventoryDto.getPrinted_embroidered(),
                inventoryDto.getSize(),
                inventoryDto.getUnit_price(),
                inventoryDto.getQuantity_available(),
                inventoryDto.getStored_location(),
                inventoryDto.getInventory_id()
        );
    }

    public String deleteItems(String inventory_id) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "DELETE FROM inventory WHERE inventory_id = ?",
                inventory_id
        );
    }

    public InventoryDto searchItems(String inventory_id) throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM inventory WHERE inventory_id = ?", inventory_id);

        if(rst.next()){
            InventoryDto dto = new InventoryDto(
                    rst.getString("inventory_id"),
                    rst.getString("item_name"),
                    rst.getString("printed_embroidered"),
                    rst.getString("size"),
                    rst.getDouble("unit_price"),
                    rst.getInt("quantity_available"),
                    rst.getString("stored_location")
            );
            return dto;
        }
        return null;
    }
    public ArrayList<InventoryDto> getAllItems() throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM inventory");
        ArrayList<InventoryDto> inventoryDto = new ArrayList<>();

        while (rst.next()) {
            InventoryDto dto = new InventoryDto(
                    rst.getString("inventory_id"),
                    rst.getString("item_name"),
                    rst.getString("printed_embroidered"),
                    rst.getString("size"),
                    rst.getDouble("unit_price"),
                    rst.getInt("quantity_available"),
                    rst.getString("stored_location")
            );
            inventoryDto.add(dto);
        }
        return inventoryDto;
    }

    public String getNextInventoryId() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT inventory_id FROM inventory ORDER BY inventory_id DESC LIMIT 1");
        String tableCharacter = "INV"; // Use any character Ex:- customer table for C, item table for I
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
