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

    public String saveOrderItems(OrderItemDto orderItemDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "INSERT INTO order_item VALUES (?,?,?,?,?)",
                orderItemDto.getOrder_item_id(),
                orderItemDto.getOrder_id(),
                orderItemDto.getInventory_id(),
                orderItemDto.getQuantity(),
                orderItemDto.getAmount()
        );
    }
    public String updateOrderItems(OrderItemDto orderItemDto) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "UPDATE order_item SET order_id = ?, inventory_id = ?, quantity = ?, amount = ?WHERE order_item_id= ?",
                orderItemDto.getOrder_id(),
                orderItemDto.getInventory_id(),
                orderItemDto.getQuantity(),
                orderItemDto.getAmount(),
                orderItemDto.getOrder_item_id()
        );
    }

    public String deleteOrderItems(String order_item_id) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute("DELETE FROM order_item WHERE order_item_id = ?", order_item_id);
    }

    public OrderItemDto searchOrderItems(String order_item_id) throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM order_item WHERE order_item_id = ?", order_item_id);

        if(rst.next()){
            OrderItemDto dto = new OrderItemDto(
                    rst.getString("order_item_id"),
                    rst.getString("order_id"),
                    rst.getString("inventory_id"),
                    rst.getInt("quantity"),
                    rst.getDouble("amount")
            );
            return dto;
        }
        return null;
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
