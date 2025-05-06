package lk.ijse.finalProject.model;

import lk.ijse.finalProject.dto.CustomerDto;
import lk.ijse.finalProject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerModel {

    public boolean saveCustomers(CustomerDto customerDto) throws SQLException, ClassNotFoundException {

        return CrudUtil.execute(
                "INSERT INTO customer VALUES (?,?,?,?,?)",
                customerDto.getCustomerId(),
                customerDto.getCustomerName(),
                customerDto.getCustomerPhone(),
                customerDto.getCustomerAddress(),
                customerDto.getOrderPlatForm()
        );
    }
    public boolean updateCustomers(CustomerDto customerDto) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "UPDATE customer SET name = ?, phone = ?, address = ?, order_platform = ?WHERE customer_id= ?",
                customerDto.getCustomerName(),
                customerDto.getCustomerPhone(),
                customerDto.getCustomerAddress(),
                customerDto.getOrderPlatForm(),
                customerDto.getCustomerId()

        );
    }
    public boolean deleteCustomers(String customer_id) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute("DELETE FROM customer WHERE customer_id = ?",
                customer_id);
    }

    public CustomerDto searchCustomers(String customer_id) throws ClassNotFoundException, SQLException{

        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer WHERE customer_id = ?",
                customer_id);

        if(resultSet.next()){
            CustomerDto dto = new CustomerDto(
                    resultSet.getString("customer_id"),
                    resultSet.getString("name"),
                    resultSet.getString("phone"),
                    resultSet.getString("address"),
                    resultSet.getString("order_platform")
            );
            return dto;
        }
        return null;
    }
    public ArrayList<CustomerDto> getAllCustomers() throws ClassNotFoundException, SQLException{

        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer");
        ArrayList<CustomerDto> customerDtoArrayList = new ArrayList<>();
        while (resultSet.next()) {
            CustomerDto customerDTO = new CustomerDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
            customerDtoArrayList.add(customerDTO);
        }

        return customerDtoArrayList;
    }

    public String getNextCustomerId() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT customer_id FROM customer ORDER BY customer_id DESC LIMIT 1");
        String tableCharacter = "CUS"; // Use any character Ex:- customer table for C, item table for I
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