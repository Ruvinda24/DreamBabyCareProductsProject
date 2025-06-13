package lk.ijse.finalProject.model;

import lk.ijse.finalProject.dto.SupplierDto;
import lk.ijse.finalProject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierModel {
    public boolean saveSuppliers(SupplierDto supplierDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "INSERT INTO supplier VALUES (?,?,?,?)",
                supplierDto.getSupplier_id(),
                supplierDto.getName(),
                supplierDto.getContact(),
                supplierDto.getAccount_details()
        );
    }
    public boolean updateSuppliers(SupplierDto supplierDto) throws ClassNotFoundException, SQLException{

                return CrudUtil.execute(
                "UPDATE supplier SET name = ?, contact = ?, account_details = ? WHERE supplier_id= ?",
                supplierDto.getName(),
                supplierDto.getContact(),
                supplierDto.getAccount_details(),
                supplierDto.getSupplier_id()
        );
    }

    public boolean deleteSuppliers(String supplier_id) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute("DELETE FROM supplier WHERE supplier_id = ?", supplier_id);
    }

    public ArrayList<SupplierDto> searchSuppliers(String searchText) throws ClassNotFoundException, SQLException{

        ArrayList<SupplierDto> supplierDtoArrayList = new ArrayList<>();
        String sql = "SELECT * FROM supplier WHERE supplier_id LIKE ? OR name LIKE ? OR contact LIKE ? OR account_details LIKE ?";
        String pattern = "%" + searchText + "%";
        ResultSet rst = CrudUtil.execute(sql, pattern, pattern, pattern, pattern);

        while(rst.next()){
            SupplierDto dto = new SupplierDto(
                    rst.getString("supplier_id"),
                    rst.getString("name"),
                    rst.getString("contact"),
                    rst.getString("account_details")
            );
            supplierDtoArrayList.add(dto);
        }
        return supplierDtoArrayList;
    }
    public ArrayList<SupplierDto> getAllSuppliers() throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM supplier");
        ArrayList<SupplierDto> supplierDto = new ArrayList<>();

        while (rst.next()) {
            SupplierDto dto = new SupplierDto(
                    rst.getString("supplier_id"),
                    rst.getString("name"),
                    rst.getString("contact"),
                    rst.getString("account_details")
            );
            supplierDto.add(dto);
        }
        return supplierDto;
    }

    public String getNextSupplierId() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT supplier_id FROM supplier ORDER BY supplier_id DESC LIMIT 1");
        String tableCharacter = "SUP"; // Use any character Ex:- customer table for C, item table for I
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
