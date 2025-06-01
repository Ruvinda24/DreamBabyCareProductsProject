package lk.ijse.finalProject.model;

import lk.ijse.finalProject.db.DBConnection;
import lk.ijse.finalProject.dto.SupplyDto;
import lk.ijse.finalProject.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplyModel {
    public boolean saveSupply(SupplyDto supplyDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "INSERT INTO supply VALUES (?,?,?,?,?)",
                supplyDto.getSupply_id(),
                supplyDto.getSupplier_id(),
                supplyDto.getMaterial_id(),
                supplyDto.getAmount(),
                supplyDto.getQuantity()
        );
    }
    public boolean updateSupply(SupplyDto supplyDto) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "UPDATE supply SET supplier_id = ?, material_id = ?, amount = ?, quantity = ?WHERE supply_id= ?",
                supplyDto.getSupplier_id(),
                supplyDto.getMaterial_id(),
                supplyDto.getAmount(),
                supplyDto.getQuantity(),
                supplyDto.getSupply_id()

        );
    }

    public boolean deleteSupply(String supply_id) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute("DELETE FROM supply WHERE supply_id = ?", supply_id);
    }

    public ArrayList<SupplyDto> searchSupply(String searchText) throws ClassNotFoundException, SQLException{

        ArrayList<SupplyDto> supplyDtoArrayList = new ArrayList<>();
        String sql = "SELECT * FROM supply WHERE supply_id LIKE ? OR supplier_id LIKE ? OR material_id LIKE ? OR amount LIKE ? OR quantity LIKE ?";
        String pattern = "%" + searchText + "%";
        ResultSet rst = CrudUtil.execute(sql, pattern, pattern, pattern, pattern, pattern);

        while(rst.next()){
            SupplyDto supplyDto = new SupplyDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getInt(5)
            );
            supplyDtoArrayList.add(supplyDto);
        }
        return supplyDtoArrayList;
    }
    public ArrayList<SupplyDto> getAllSupply() throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM supply");
        ArrayList<SupplyDto> supplyDto = new ArrayList<>();

        while (rst.next()) {
            SupplyDto dto = new SupplyDto(
                    rst.getString("supply_id"),
                    rst.getString("supplier_id"),
                    rst.getString("material_id"),
                    rst.getDouble("amount"),
                    rst.getInt("quantity")
            );
            supplyDto.add(dto);
        }
        return supplyDto;
    }

    public String getNextSupplyId() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT supply_id FROM supply ORDER BY supply_id DESC LIMIT 1");
        String tableCharacter = "SUPPLY"; // Use any character Ex:- customer table for C, item table for I
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

    public ArrayList<String> getAllSupplierIds() throws ClassNotFoundException, SQLException {
        ResultSet rst = CrudUtil.execute("SELECT supplier_id FROM supplier");
        ArrayList<String> supplierIds = new ArrayList<>();

        while (rst.next()) {
            supplierIds.add(rst.getString("supplier_id"));
        }
        return supplierIds;
    }

    public ArrayList<String> getAllMaterialIds() throws ClassNotFoundException, SQLException {
        ResultSet rst = CrudUtil.execute("SELECT material_id FROM material");
        ArrayList<String> materialIds = new ArrayList<>();

        while (rst.next()) {
            materialIds.add(rst.getString("material_id"));
        }
        return materialIds;
    }

/*    public String getMaterialNameById(String materialId) throws ClassNotFoundException, SQLException {
        ResultSet rst = CrudUtil.execute("SELECT name FROM material WHERE material_id = ?", materialId);
        if (rst.next()) {
            return rst.getString("name");
        }
        return null;
    }

    public String getSupplierNameById(String supplierId) throws ClassNotFoundException, SQLException {
        ResultSet rst = CrudUtil.execute("SELECT name FROM supplier WHERE supplier_id = ?", supplierId);
        if (rst.next()) {
            return rst.getString("name");
        }
        return null;
    }*/

}
