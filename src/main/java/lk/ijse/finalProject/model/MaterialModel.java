package lk.ijse.finalProject.model;

import lk.ijse.finalProject.dto.MaterialDto;
import lk.ijse.finalProject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MaterialModel {

    public String saveMaterials(MaterialDto materialDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "INSERT INTO material VALUES (?,?,?,?)",
                materialDto.getMaterial_id(),
                materialDto.getName(),
                materialDto.getColor_type(),
                materialDto.getQuantity()
        );
    }
    public String updateMaterials(MaterialDto materialDto) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "UPDATE material SET name = ?, color_type = ?, quantity = ? WHERE material_id= ?",
                materialDto.getName(),
                materialDto.getColor_type(),
                materialDto.getQuantity(),
                materialDto.getMaterial_id()
        );
    }

    public String deleteMaterials(String material_id) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "DELETE FROM material WHERE material_id = ?",material_id
        );
    }

    public MaterialDto searchMaterials(String material_id) throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM material WHERE material_id = ?", material_id);

        if(rst.next()){
            MaterialDto dto = new MaterialDto(
                    rst.getString("material_id"),
                    rst.getString("name"),
                    rst.getString("color_type"),
                    rst.getInt("address")
            );
            return dto;
        }
        return null;
    }
    public ArrayList<MaterialDto> getAllMaterials() throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM material");
        ArrayList<MaterialDto> materialDto = new ArrayList<>();

        while (rst.next()) {
            MaterialDto dto = new MaterialDto(
                    rst.getString("material_id"),
                    rst.getString("name"),
                    rst.getString("color_type"),
                    rst.getInt("address")
            );
            materialDto.add(dto);
        }
        return materialDto;
    }

    public String getNextMaterialId() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT material_id FROM material ORDER BY material_id DESC LIMIT 1");
        String tableCharacter = "MAT"; // Use any character Ex:- customer table for C, item table for I
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
