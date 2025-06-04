package lk.ijse.finalProject.model;

import javafx.scene.control.Alert;
import lk.ijse.finalProject.dto.MaterialDto;
import lk.ijse.finalProject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MaterialModel {

    public boolean saveMaterials(MaterialDto materialDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "INSERT INTO material VALUES (?,?,?,?)",
                materialDto.getMaterial_id(),
                materialDto.getName(),
                materialDto.getColor_type(),
                materialDto.getQuantity()
        );
    }
    public boolean updateMaterials(MaterialDto materialDto) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "UPDATE material SET name = ?, color_type = ?, quantity = ? WHERE material_id= ?",
                materialDto.getName(),
                materialDto.getColor_type(),
                materialDto.getQuantity(),
                materialDto.getMaterial_id()
        );
    }

    public boolean deleteMaterials(String material_id) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "DELETE FROM material WHERE material_id = ?",material_id
        );
    }

    public ArrayList<MaterialDto> searchMaterials(String searchText) throws ClassNotFoundException, SQLException{

        ArrayList<MaterialDto> materialDtoArrayList = new ArrayList<>();
        String sql = "SELECT * FROM material WHERE material_id LIKE ? OR name LIKE ? OR color_type LIKE ? OR quantity LIKE ?";
        String pattern = "%" + searchText + "%";
        ResultSet rst = CrudUtil.execute(sql, pattern, pattern, pattern, pattern);

        while (rst.next()){
            MaterialDto dto = new MaterialDto(
                    rst.getString("material_id"),
                    rst.getString("name"),
                    rst.getString("color_type"),
                    rst.getInt("quantity")
            );
            materialDtoArrayList.add(dto);
        }
        return materialDtoArrayList;
    }
    public ArrayList<MaterialDto> getAllMaterials() throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM material");
        ArrayList<MaterialDto> materialDto = new ArrayList<>();

        while (rst.next()) {
            MaterialDto dto = new MaterialDto(
                    rst.getString("material_id"),
                    rst.getString("name"),
                    rst.getString("color_type"),
                    rst.getInt("quantity")
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
            String lastIdNumberString = lastId.substring(tableCharacter.length()); // "001"
            int lastIdNumber = Integer.parseInt(lastIdNumberString); // 1
            int nextIdNUmber = lastIdNumber + 1; // 2
            // "C002"
            return String.format(tableCharacter + "%03d", nextIdNUmber);
        }
        // No data recode in table so return initial primary key
        return tableCharacter + "001";
    }

    public ArrayList<String> getAllMaterialIds() {
        ArrayList<String> materialIds = new ArrayList<>();
        try {
            ResultSet rst = CrudUtil.execute("SELECT material_id FROM material");
            while (rst.next()) {
                materialIds.add(rst.getString("material_id"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load material IDs..!").show();
        }
        return materialIds;
    }

    public String getMaterialNameById(String newVal) {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT name FROM material WHERE material_id = ?", newVal);
            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load material name..!").show();
        }
        return null;
    }

    public String getMaterialColorTypeById(String newVal) {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT color_type FROM material WHERE material_id = ?", newVal);
            if (resultSet.next()) {
                return resultSet.getString("color_type");
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load material color type..!").show();
        }
        return null;
    }

    public String getMaterialQtyById(String newVal) {
    try {
            ResultSet resultSet = CrudUtil.execute("SELECT quantity FROM material WHERE material_id = ?", newVal);
            if (resultSet.next()) {
                return String.valueOf(resultSet.getInt("quantity"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load material quantity..!").show();
        }
        return null;
    }

    public boolean reduceMaterialQty(String materialId, int materialUsageQty) {
        try {
            ResultSet resultSet = CrudUtil.execute(
                    "SELECT quantity FROM material WHERE material_id = ?",
                    materialId
            );
            if (resultSet.next()) {
                int currentQty = resultSet.getInt("quantity");
                if (currentQty >= materialUsageQty) {
                    int newQty = currentQty - materialUsageQty;
                    return CrudUtil.execute(
                            "UPDATE material SET quantity = ? WHERE material_id = ?",
                            newQty,
                            materialId);
                } else {
                    new Alert(Alert.AlertType.ERROR, "Insufficient stock for Material ID: " + materialId).show();
                    return false;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error reducing material quantity...").show();
        }
        return false;
    }
}
