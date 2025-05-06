package lk.ijse.finalProject.controller;

import lk.ijse.finalProject.dto.CustomerDto;
import lk.ijse.finalProject.model.CustomerModel;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerController {
    private CustomerModel customerModel = new CustomerModel();

    public boolean saveCustomer(CustomerDto customerDto) throws ClassNotFoundException, SQLException {
        boolean resp = customerModel.saveCustomers(customerDto);
        return resp;

    }
    public boolean updateCustomer(CustomerDto customerDto) throws ClassNotFoundException, SQLException {
        boolean resp = customerModel.updateCustomers(customerDto);
        return resp;

    }    public boolean deleteCustomer(CustomerDto customerDto) throws ClassNotFoundException, SQLException {
        boolean resp = customerModel.deleteCustomers(customerDto.getCustomerId());
        return resp;

    }

    public CustomerDto searchCustomer(String customerCode) throws ClassNotFoundException, SQLException {
        CustomerDto resp = customerModel.searchCustomers(customerCode);
        return resp;
    }

    public ArrayList<CustomerDto> getAll() throws ClassNotFoundException, SQLException{
        ArrayList<CustomerDto> resp = customerModel.getAllCustomers();
        return resp;
    }
}

