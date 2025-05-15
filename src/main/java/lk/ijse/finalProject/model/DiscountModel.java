package lk.ijse.finalProject.model;

import lk.ijse.finalProject.db.DBConnection;
import lk.ijse.finalProject.dto.DiscountDto;
import lk.ijse.finalProject.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DiscountModel {
    public boolean saveDiscounts(DiscountDto discountDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "INSERT INTO discount VALUES (?,?,?,?)",
                discountDto.getDiscount_id(),
                discountDto.getPayment_id(),
                discountDto.getDiscount_type(),
                discountDto.getDiscount_percentage()
        );
    }
    public boolean updateDiscounts(DiscountDto discountDto) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "UPDATE discount SET payment_id = ?, discount_type = ?, discount_percentage = ?WHERE discount_id= ?",
                discountDto.getPayment_id(),
                discountDto.getDiscount_type(),
                discountDto.getDiscount_percentage(),
                discountDto.getDiscount_id()
        );
    }

    public boolean deleteDiscounts(String discount_id) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "DELETE FROM discount WHERE discount_id = ?",
                discount_id
        );
    }

    public DiscountDto searchDiscounts(String discount_id) throws ClassNotFoundException, SQLException{

        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM discount WHERE discount_id = ?",
                discount_id
        );

        if(resultSet.next()){
            DiscountDto dto = new DiscountDto(
                    resultSet.getString("discount_id"),
                    resultSet.getString("payment_id"),
                    resultSet.getString("discount_type"),
                    resultSet.getDouble("discount_percentage")
            );
            return dto;
        }
        return null;
    }
    public ArrayList<DiscountDto> getAllDiscounts() throws ClassNotFoundException, SQLException{
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM discount"
        );
        ArrayList<DiscountDto> discountDto = new ArrayList<>();

        while (resultSet.next()) {
            DiscountDto dto = new DiscountDto(
                    resultSet.getString("discount_id"),
                    resultSet.getString("payment_id"),
                    resultSet.getString("discount_type"),
                    resultSet.getDouble("discount_percentage")
            );
            discountDto.add(dto);
        }
        return discountDto;
    }

    public String getNextDiscountId() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT discount_id FROM discount ORDER BY discount_id DESC LIMIT 1");
        String tableCharacter = "DIS"; // Use any character Ex:- customer table for C, item table for I
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

    public ArrayList<String> getAllPaymentIds() throws ClassNotFoundException, SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT payment_id FROM payment");
        ArrayList<String> paymentIds = new ArrayList<>();
        while (resultSet.next()) {
            paymentIds.add(resultSet.getString("payment_id"));
        }
        return paymentIds;
    }


    public double getPaymentAmountById(String paymentId) throws ClassNotFoundException, SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT amount FROM payment WHERE payment_id = ?", paymentId);
        if (resultSet.next()) {
            return resultSet.getDouble("amount");
        }
        return 0.0;
    }
}

