package lk.ijse.finalProject.model;

import lk.ijse.finalProject.db.DBConnection;
import lk.ijse.finalProject.dto.OrderItemDto;
import lk.ijse.finalProject.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderItemModel {

    public boolean saveOrderItems(OrderItemDto orderItemDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "INSERT INTO order_item VALUES (?,?,?,?,?)",
                orderItemDto.getOrder_item_id(),
                orderItemDto.getOrder_id(),
                orderItemDto.getInventory_id(),
                orderItemDto.getQuantity(),
                orderItemDto.getAmount()
        );
    }
    public boolean updateOrderItems(OrderItemDto orderItemDto) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "UPDATE order_item SET order_id = ?, inventory_id = ?, quantity = ?, amount = ?WHERE order_item_id= ?",
                orderItemDto.getOrder_id(),
                orderItemDto.getInventory_id(),
                orderItemDto.getQuantity(),
                orderItemDto.getAmount(),
                orderItemDto.getOrder_item_id()
        );
    }

    public boolean deleteOrderItems(String order_item_id) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute("DELETE FROM order_item WHERE order_item_id = ?", order_item_id);
    }

    public ArrayList<OrderItemDto> searchOrderItems(String searchText) throws ClassNotFoundException, SQLException{

        ArrayList<OrderItemDto> orderItemDtoArrayList = new ArrayList<>();
        String sql = "SELECT * FROM order_item WHERE order_item_id LIKE ? OR order_id LIKE ? OR inventory_id LIKE ? OR quantity LIKE ? OR amount LIKE ?";
        String pattern = "%" + searchText + "%";
        ResultSet rst = CrudUtil.execute(sql, pattern, pattern, pattern, pattern, pattern);

        while(rst.next()){
            OrderItemDto orderItemDtodto = new OrderItemDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(4),
                    rst.getDouble(5)
            );
            orderItemDtoArrayList.add(orderItemDtodto);
        }
        return orderItemDtoArrayList;
    }
    public ArrayList<OrderItemDto> getAllOrderItems() throws ClassNotFoundException, SQLException{
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM order_item";
        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet rst = CrudUtil.execute("SELECT * FROM order_item");
        ArrayList<OrderItemDto> orderItemDto = new ArrayList<>();

        while (rst.next()) {
            OrderItemDto dto = new OrderItemDto(
                    rst.getString("order_item_id"),
                    rst.getString("order_id"),
                    rst.getString("inventory_id"),
                    rst.getInt("quantity"),
                    rst.getDouble("amount")
            );
            orderItemDto.add(dto);
        }
        return orderItemDto;
    }

    public String getNextOrderItemId() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT order_item_id FROM order_item ORDER BY order_item_id DESC LIMIT 1");
        String tableCharacter = "OIT"; // Use any character Ex:- customer table for C, item table for I
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

    public ArrayList<String> getAllOrderIds() throws ClassNotFoundException, SQLException {
        ResultSet rst = CrudUtil.execute("SELECT order_id FROM orders");
        ArrayList<String> orderIds = new ArrayList<>();

        while (rst.next()) {
            orderIds.add(rst.getString("order_id"));
        }
        return orderIds;
    }

    public ArrayList<String> getAllInventoryIds() throws ClassNotFoundException, SQLException {
        ResultSet rst = CrudUtil.execute("SELECT inventory_id FROM inventory");
        ArrayList<String> inventoryIds = new ArrayList<>();

        while (rst.next()) {
            inventoryIds.add(rst.getString("inventory_id"));
        }
        return inventoryIds;
    }
    public String getInventoryItemNameById(String inventoryId) throws ClassNotFoundException, SQLException {
        ResultSet rst = CrudUtil.execute("SELECT item_name FROM inventory WHERE inventory_id = ?", inventoryId);
        if (rst.next()) {
            return rst.getString("item_name");
        }
        return null;
    }

    public boolean saveNewOrderItem(String orderItemId, String orderId, String itemId, int cartQty, double amount) {
        try {
            return CrudUtil.execute(
                    "INSERT INTO order_item (order_item_id, order_id, inventory_id, quantity, amount) VALUES (?, ?, ?, ?, ?)",
                    orderItemId,
                    orderId,
                    itemId,
                    cartQty,
                    amount
            );
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
