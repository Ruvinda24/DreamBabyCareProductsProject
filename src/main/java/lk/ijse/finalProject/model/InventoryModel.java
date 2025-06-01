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

    public boolean saveItems(InventoryDto inventoryDto) throws ClassNotFoundException, SQLException {

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
    public boolean updateItems(InventoryDto inventoryDto) throws ClassNotFoundException, SQLException{

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

    public boolean deleteItems(String inventory_id) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "DELETE FROM inventory WHERE inventory_id = ?",
                inventory_id
        );
    }

    public ArrayList<InventoryDto> searchItems(String searchText) throws ClassNotFoundException, SQLException{

        ArrayList<InventoryDto> inventoryDtoArrayList = new ArrayList<>();
        String sql = "SELECT * FROM inventory WHERE inventory_id LIKE ? OR item_name LIKE ? OR printed_embroidered LIKE ? OR size LIKE ? OR unit_price LIKE ? OR quantity_available LIKE ? OR stored_location LIKE ?";
        String pattern = "%" + searchText + "%";
        ResultSet rst = CrudUtil.execute(sql,pattern,pattern,pattern,pattern,pattern,pattern,pattern);

        while (rst.next()){
            InventoryDto dto = new InventoryDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5),
                    rst.getInt(6),
                    rst.getString(7)
            );
            inventoryDtoArrayList.add(dto);
        }
        return inventoryDtoArrayList;
    }
    public ArrayList<InventoryDto> getAllItems() throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM inventory");
        ArrayList<InventoryDto> inventoryDto = new ArrayList<>();

        while (rst.next()) {
            InventoryDto dto = new InventoryDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5),
                    rst.getInt(6),
                    rst.getString(7)
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
            String lastIdNumberString = lastId.substring(tableCharacter.length()); // "001"
            int lastIdNumber = Integer.parseInt(lastIdNumberString); // 1
            int nextIdNUmber = lastIdNumber + 1; // 2
            // "C002"
            return String.format(tableCharacter + "%03d", nextIdNUmber);
        }
        // No data recode in table so return initial primary key
        return tableCharacter + "001";
    }
}
