package lk.ijse.finalProject.model;

import lk.ijse.finalProject.db.DBConnection;
import lk.ijse.finalProject.dto.ShipmentDto;
import lk.ijse.finalProject.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShipmentModel {

    public String saveShipments(ShipmentDto shipmentDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "INSERT INTO shipment VALUES (?,?,?)",
                shipmentDto.getShipment_id(),
                shipmentDto.getTracking_number(),
                shipmentDto.getShipment_date()
        );
    }

    public String updateShipments(ShipmentDto shipmentDto) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "UPDATE shipment SET tracking_number = ?, shipment_date = ? WHERE shipment_id= ?",
                shipmentDto.getTracking_number(),
                shipmentDto.getShipment_date(),
                shipmentDto.getShipment_id()
        );
    }

    public String deleteShipments(String shipment_id) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute("DELETE FROM shipment WHERE shipment_id = ?", shipment_id);
    }

    public ShipmentDto searchShipments(String shipment_id) throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM shipment WHERE shipment_id = ?", shipment_id);

        if(rst.next()){
            ShipmentDto dto = new ShipmentDto(
                    rst.getString("shipment_id"),
                    rst.getString("tracking_number"),
                    rst.getString("shipment_date")
            );
            return dto;
        }
        return null;
    }
    public ArrayList<ShipmentDto> getAllShipments() throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM shipment");
        ArrayList<ShipmentDto> shipmentDto = new ArrayList<>();

        while (rst.next()) {
            ShipmentDto dto = new ShipmentDto(
                    rst.getString("shipment_id"),
                    rst.getString("tracking_number"),
                    rst.getString("shipment_date")
            );
            shipmentDto.add(dto);
        }
        return shipmentDto;
    }

    public String getNextShipmentId() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT shipment_id FROM shipment ORDER BY shipment_id DESC LIMIT 1");
        String tableCharacter = "SHP"; // Use any character Ex:- customer table for C, item table for I
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
