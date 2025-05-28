package lk.ijse.finalProject.model;

import lk.ijse.finalProject.db.DBConnection;
import lk.ijse.finalProject.dto.PaymentDto;
import lk.ijse.finalProject.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentModel {

    public boolean savePayments(PaymentDto paymentDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "INSERT INTO payment VALUES (?,?,?,?)",
                paymentDto.getPayment_id(),
                paymentDto.getOrder_id(),
                paymentDto.getAmount(),
                paymentDto.getPayment_method()
        );
    }

    public boolean updatePayments(PaymentDto paymentDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "UPDATE payment SET order_id = ?, amount = ?, payment_method = ?WHERE payment_id= ?",
                paymentDto.getOrder_id(),
                paymentDto.getAmount(),
                paymentDto.getPayment_method(),
                paymentDto.getPayment_id()
        );
    }

    public boolean deletePayments(String payment_id) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute("DELETE FROM payment WHERE payment_id = ?", payment_id);
    }

    public ArrayList<PaymentDto> searchPayments(String searchText) throws ClassNotFoundException, SQLException {

        ArrayList<PaymentDto> paymentDtoArrayList = new ArrayList<>();
        String sql ="SELECT * FROM payment WHERE payment_id LIKE ? OR order_id LIKE ? OR amount LIKE ? OR payment_method LIKE ?";
        String pattern = "%" + searchText + "%";
        ResultSet rst = CrudUtil.execute(sql, pattern, pattern, pattern, pattern);

        if (rst.next()) {
            PaymentDto dto = new PaymentDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(3),
                    rst.getString(4)
            );
            paymentDtoArrayList.add(dto);
        }
        return paymentDtoArrayList;
    }

    public ArrayList<PaymentDto> getAllPayments() throws ClassNotFoundException, SQLException {

        ResultSet rst = CrudUtil.execute("SELECT * FROM payment");
        ArrayList<PaymentDto> paymentDto = new ArrayList<>();

        while (rst.next()) {
            PaymentDto dto = new PaymentDto(
                    rst.getString("payment_id"),
                    rst.getString("order_id"),
                    rst.getDouble("amount"),
                    rst.getString("payment_method")
            );
            paymentDto.add(dto);
        }
        return paymentDto;
    }

    public String getNextPaymentId() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT payment_id FROM payment ORDER BY payment_id DESC LIMIT 1");
        String tableCharacter = "PAY"; // Use any character Ex:- customer table for C, item table for I
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

    public ArrayList<String> getAllOrderIds() throws ClassNotFoundException, SQLException {
        ArrayList<String> orderIds = new ArrayList<>();
        ResultSet rst = CrudUtil.execute("SELECT order_id FROM orders");
        while (rst.next()) {
            orderIds.add(rst.getString("order_id"));
        }
        return orderIds;
    }
}
