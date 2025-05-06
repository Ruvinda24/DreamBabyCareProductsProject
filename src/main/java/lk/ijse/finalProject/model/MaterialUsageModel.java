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

    public String saveMaterialUsage(MaterialUsageDto materialUsageDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "INSERT INTO material_usage VALUES (?,?,?,?)",
                materialUsageDto.getUsage_id(),
                materialUsageDto.getProduction_id(),
                materialUsageDto.getMaterial_id(),
                materialUsageDto.getQuantity_used()
        );
    }

    public String updateMaterialUsage(MaterialUsageDto materialUsageDto) throws ClassNotFoundException, SQLException{
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, materialUsageDto.getProduction_id());
        statement.setString(2, materialUsageDto.getMaterial_id());
        statement.setInt(3, materialUsageDto.getQuantity_used());
        statement.setString(4, materialUsageDto.getUsage_id());

        return CrudUtil.execute(
                "UPDATE material_usage SET production_id = ?, material_id = ?, quantity_used = ?WHERE usage_id= ?",
                materialUsageDto.getProduction_id(),
                materialUsageDto.getMaterial_id(),
                materialUsageDto.getQuantity_used(),
                materialUsageDto.getUsage_id()
        );
    }

    public String deleteMaterialUsage(String usage_id) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute("DELETE FROM material_usage WHERE usage_id = ?", usage_id);
    }

    public MaterialUsageDto searchMaterialUsage(String usage_id) throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM material_usage WHERE usage_id = ?", usage_id);

        if(rst.next()){
            MaterialUsageDto dto = new MaterialUsageDto(
                    rst.getString("usage_id"),
                    rst.getString("production_id"),
                    rst.getString("material_id"),
                    rst.getInt("quantity_used")
            );
            return dto;
        }
        return null;
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

        ResultSet resultSet = CrudUtil.execute("SELECT material_usage_id FROM material_usage ORDER BY material_usage_id DESC LIMIT 1");
        String tableCharacter = "MU"; // Use any character Ex:- customer table for C, item table for I
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
