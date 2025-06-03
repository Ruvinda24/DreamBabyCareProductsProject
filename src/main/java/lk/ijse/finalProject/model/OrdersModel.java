package lk.ijse.finalProject.model;

import lk.ijse.finalProject.db.DBConnection;
import lk.ijse.finalProject.dto.OrdersDto;
import lk.ijse.finalProject.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class OrdersModel {

    public boolean saveOrders(OrdersDto orderDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "INSERT INTO orders VALUES (?,?,?,?,?)",
                orderDto.getOrder_id(),
                orderDto.getOrder_date(),
                orderDto.getCustomer_id(),
                orderDto.getShipment_id(),
                orderDto.getStatus()
        );
    }

    public boolean updateOrders(OrdersDto orderDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "UPDATE orders SET order_date = ?, customer_id = ?, shipment_id = ?, status = ?WHERE order_id= ?",
                orderDto.getOrder_date(),
                orderDto.getCustomer_id(),
                orderDto.getShipment_id(),
                orderDto.getStatus(),
                orderDto.getOrder_id()
        );
    }

    public boolean deleteOrders(String order_id) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute("DELETE FROM orders WHERE order_id = ?", order_id);
    }

    public ArrayList<OrdersDto> searchOrders(String searchText) throws ClassNotFoundException, SQLException {


        ArrayList<OrdersDto> ordersDtoArrayList = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE order_id LIKE ? OR order_date LIKE ? OR customer_id LIKE ? OR shipment_id LIKE ? OR status LIKE ?";
        String pattern = "%" + searchText + "%";
        ResultSet rst = CrudUtil.execute(sql, pattern, pattern, pattern, pattern, pattern);

        while (rst.next()) {
            OrdersDto ordersDto = new OrdersDto(
                    rst.getString(1),
                    LocalDate.parse(rst.getString(2)),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            );
            ordersDtoArrayList.add(ordersDto);
        }
        return ordersDtoArrayList;
    }

    public ArrayList<OrdersDto> getAllOrders() throws ClassNotFoundException, SQLException {

        ResultSet rst = CrudUtil.execute("SELECT * FROM orders");
        ArrayList<OrdersDto> orderDto = new ArrayList<>();

        while (rst.next()) {
            OrdersDto dto = new OrdersDto(
                    rst.getString("order_id"),
                    LocalDate.parse(rst.getString("order_date")),
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
            String lastIdNumberString = lastId.substring(tableCharacter.length()); // "001"
            int lastIdNumber = Integer.parseInt(lastIdNumberString); // 1
            int nextIdNUmber = lastIdNumber + 1; // 2
            // "C002"
            return String.format(tableCharacter + "%03d", nextIdNUmber);
        }
        // No data recode in table so return initial primary key
        return tableCharacter + "001";
    }

    public String getCustomerNameById(String customerId) {
        try {

            ResultSet rst = CrudUtil.execute("SELECT name FROM customer WHERE customer_id = ?", customerId);
            if (rst.next()) {
                return rst.getString("name");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTrackingNumberById(String shipmentId) {
        try {

            ResultSet rst = CrudUtil.execute("SELECT tracking_number FROM shipment WHERE shipment_id = ?", shipmentId);
            if (rst.next()) {
                return rst.getString("tracking_number");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getAllCustomerIds() throws SQLException, ClassNotFoundException {
        ArrayList<String> customerIds = new ArrayList<>();
        ResultSet rst = CrudUtil.execute("SELECT customer_id FROM customer");
        while (rst.next()) {
            customerIds.add(rst.getString("customer_id"));
        }
        return customerIds;
    }
    public ArrayList<String> getAllShipmentIds() throws SQLException, ClassNotFoundException {
        ArrayList<String> shipmentIds = new ArrayList<>();
        ResultSet rst = CrudUtil.execute("SELECT shipment_id FROM shipment");
        while (rst.next()) {
            shipmentIds.add(rst.getString("shipment_id"));
        }
        return shipmentIds;
    }

    public boolean saveNewOrder(String orderId, String orderDate, String customerId, String shipmentId, String status) {
        try{
                return CrudUtil.execute(
                        "INSERT INTO orders (order_id, order_date, customer_id, shipment_id, status) VALUES (?, ?, ?, ?, ?)",
                        orderId,
                        orderDate,
                        customerId,
                        shipmentId,
                        status
                );
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
