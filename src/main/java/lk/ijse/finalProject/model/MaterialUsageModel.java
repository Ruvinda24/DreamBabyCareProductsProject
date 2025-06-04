package lk.ijse.finalProject.model;

import lk.ijse.finalProject.db.DBConnection;
import lk.ijse.finalProject.dto.MaterialUsageDto;
import lk.ijse.finalProject.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MaterialUsageModel {

    public boolean saveMaterialUsage(MaterialUsageDto materialUsageDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "INSERT INTO material_usage VALUES (?,?,?,?)",
                materialUsageDto.getUsage_id(),
                materialUsageDto.getProduction_id(),
                materialUsageDto.getMaterial_id(),
                materialUsageDto.getQuantity_used()
        );
    }

    public boolean updateMaterialUsage(MaterialUsageDto materialUsageDto) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "UPDATE material_usage SET production_id = ?, material_id = ?, quantity_used = ?WHERE usage_id= ?",
                materialUsageDto.getProduction_id(),
                materialUsageDto.getMaterial_id(),
                materialUsageDto.getQuantity_used(),
                materialUsageDto.getUsage_id()
        );
    }

    public boolean deleteMaterialUsage(String usage_id) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute("DELETE FROM material_usage WHERE usage_id = ?", usage_id);
    }

    public ArrayList<MaterialUsageDto> searchMaterialUsage(String searchText) throws ClassNotFoundException, SQLException{

        ArrayList<MaterialUsageDto> materialUsageDtoArrayList = new ArrayList<>();
        String sql = "SELECT * FROM material_usage WHERE usage_id LIKE ? OR production_id LIKE ? OR material_id LIKE ? OR quantity_used LIKE ?";
        String pattern = "%" + searchText + "%";
        ResultSet rst = CrudUtil.execute(sql,pattern, pattern, pattern, pattern);

        while(rst.next()){
            MaterialUsageDto dto = new MaterialUsageDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(4)
            );
            materialUsageDtoArrayList.add(dto);
        }
        return materialUsageDtoArrayList;
    }
    public ArrayList<MaterialUsageDto> getAllMaterialUsage() throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM material_usage");
        ArrayList<MaterialUsageDto> materialUsageDto = new ArrayList<>();

        while (rst.next()) {
            MaterialUsageDto dto = new MaterialUsageDto(
                    rst.getString("usage_id"),
                    rst.getString("production_id"),
                    rst.getString("material_id"),
                    rst.getInt("quantity_used")
            );
            materialUsageDto.add(dto);
        }
        return materialUsageDto;
    }

    public String getNextMaterialUsageId() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT usage_id FROM material_usage ORDER BY usage_id DESC LIMIT 1");
        String tableCharacter = "MU"; // Use any character Ex:- customer table for C, item table for I
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

    public ArrayList<String> getAllProductionIds() throws ClassNotFoundException, SQLException {
        ArrayList<String> productionIds = new ArrayList<>();
        ResultSet rst = CrudUtil.execute("SELECT production_id FROM production");
        while (rst.next()) {
            productionIds.add(rst.getString(1));
        }
        return productionIds;
    }

    public ArrayList<String> getAllMaterialIds() throws ClassNotFoundException, SQLException {
        ArrayList<String> materialIds = new ArrayList<>();
        ResultSet rst = CrudUtil.execute("SELECT material_id FROM material");
        while (rst.next()) {
            materialIds.add(rst.getString(1));
        }
        return materialIds;
    }
}