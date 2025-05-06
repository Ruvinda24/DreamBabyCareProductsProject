package lk.ijse.finalProject.model;

import lk.ijse.finalProject.db.DBConnection;
import lk.ijse.finalProject.dto.OrdersDto;
import lk.ijse.finalProject.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrdersModel {

    public String saveOrders(OrdersDto orderDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "INSERT INTO orders VALUES (?,?,?,?,?)",
                orderDto.getOrder_id(),
                orderDto.getOrder_date(),
                orderDto.getCustomer_id(),
                orderDto.getShipment_id(),
                orderDto.getStatus()
        );
    }

    public String updateOrders(OrdersDto orderDto) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "UPDATE orders SET order_date = ?, customer_id = ?, shipment_id = ?, status = ?WHERE order_id= ?",
                orderDto.getOrder_date(),
                orderDto.getCustomer_id(),
                orderDto.getShipment_id(),
                orderDto.getStatus(),
                orderDto.getOrder_id()
        );
    }

    public String deleteOrders(String order_id) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute("DELETE FROM orders WHERE order_id = ?", order_id);
    }

    public OrdersDto searchOrders(String order_id) throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM orders WHERE order_id = ?", order_id);

        if(rst.next()){
            OrdersDto dto = new OrdersDto(
                    rst.getString("order_id"),
                    rst.getString("order_date"),
                    rst.getString("customer_id"),
                    rst.getString("shipment_id"),
                    rst.getString("status")
            );
            return dto;
        }
        return null;
    }
    public ArrayList<OrdersDto> getAllOrders() throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM orders");
        ArrayList<OrdersDto> orderDto = new ArrayList<>();

        while (rst.next()) {
            OrdersDto dto = new OrdersDto(
                    rst.getString("order_id"),
                    rst.getString("order_date"),
                    rst.getString("customer_id"),
                    rst.getString("shipment_id"),
                    rst.getString("status")
            );
            orderDto.add(dto);
        }
        return orderDto;
    }

    public String getNextOrderId() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1");
        String tableCharacter = "ORD"; // Use any character Ex:- customer table for C, item table for I
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
