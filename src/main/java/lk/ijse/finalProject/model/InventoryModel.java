package lk.ijse.finalProject.model;

import javafx.scene.control.Alert;
import lk.ijse.finalProject.dto.InventoryDto;
import lk.ijse.finalProject.util.CrudUtil;

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

    public ArrayList<String> getAllItemIds() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT inventory_id FROM inventory");
        ArrayList<String> inventoryIds = new ArrayList<>();
        while (resultSet.next()) {
            inventoryIds.add(resultSet.getString("inventory_id"));
        }
        return inventoryIds;
    }

    public InventoryDto getItemsByIds(String itemId) {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM inventory WHERE inventory_id = ?", itemId);
            if (resultSet.next()) {
                return new InventoryDto(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getDouble(5),
                        resultSet.getInt(6),
                        resultSet.getString(7)
                );
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load items by Item ID...").show();
        }
        return null;
    }

    public boolean reduceItemQty(String itemId, int cartQty) {
        try {
            ResultSet resultSet = CrudUtil.execute(
                    "SELECT quantity_available FROM inventory WHERE inventory_id = ?",
                    itemId
            );
            if (resultSet.next()) {
                int currentQty = resultSet.getInt("quantity_available");
                if (currentQty >= cartQty) {
                    int newQty = currentQty - cartQty;
                    return CrudUtil.execute(
                            "UPDATE inventory SET quantity_available = ? WHERE inventory_id = ?",
                            newQty,
                            itemId);
                } else {
                    new Alert(Alert.AlertType.ERROR, "Insufficient stock for Item ID: " + itemId).show();
                    return false;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error reducing item quantity...").show();
        }
        return false;
    }

    public ArrayList<String> getAllInventoryIds() {
        ArrayList<String> inventoryIds = new ArrayList<>();
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT inventory_id FROM inventory");
            while (resultSet.next()) {
                inventoryIds.add(resultSet.getString("inventory_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load inventory IDs...").show();
        }
        return inventoryIds;
    }

    public String getItemNameById(String newVal) {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT item_name FROM inventory WHERE inventory_id = ?", newVal);
            if (resultSet.next()) {
                return resultSet.getString("item_name");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load item name by ID...").show();
        }
        return null;
    }

    public boolean increaseInventoryQty(String inventoryId, int productQty) {
        try {
            ResultSet resultSet = CrudUtil.execute(
                    "SELECT quantity_available FROM inventory WHERE inventory_id = ?",
                    inventoryId
            );
            if (resultSet.next()) {
                int currentQty = resultSet.getInt("quantity_available");
                int newQty = currentQty + productQty;
                return CrudUtil.execute(
                        "UPDATE inventory SET quantity_available = ? WHERE inventory_id = ?",
                        newQty,
                        inventoryId);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error increasing inventory quantity...").show();
        }
        return false;
    }

/*    public boolean updateItemQuantity(String itemId, int newQty) throws SQLException {
        String sql = "UPDATE inventory SET quantity_available = ? WHERE item_id = ?";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, newQty);
            pst.setString(2, itemId);
            return pst.executeUpdate() > 0;
        }
    }*/
    
}
