package lk.ijse.finalProject.model;

import lk.ijse.finalProject.db.DBConnection;
import lk.ijse.finalProject.dto.EmployeeDto;
import lk.ijse.finalProject.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeModel {

    public boolean saveEmployees(EmployeeDto employeeDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "INSERT INTO employee VALUES (?,?,?,?,?)",
                employeeDto.getEmployee_id(),
                employeeDto.getName(),
                employeeDto.getNic(),
                employeeDto.getNumber(),
                employeeDto.getRole()
        );


    }
    public boolean updateEmployees(EmployeeDto employeeDto) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "UPDATE employee SET name = ?, nic = ?, number = ?, role = ? WHERE employee_id= ?",
                employeeDto.getName(),
                employeeDto.getNic(),
                employeeDto.getNumber(),
                employeeDto.getRole(),
                employeeDto.getEmployee_id()
        );
    }

    public boolean deleteEmployees(String employee_id) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "DELETE FROM employee WHERE employee_id = ?",
                employee_id
        );
    }

    public ArrayList<EmployeeDto> searchEmployees(String searchText) throws ClassNotFoundException, SQLException{

        ArrayList<EmployeeDto> employeeDtoArrayList = new ArrayList<>();
        String sql = "SELECT * FROM employee WHERE employee_id LIKE ? OR name LIKE ? OR nic LIKE ? OR number LIKE ? OR role LIKE ?";
        String pattern = "%" + searchText + "%";
        ResultSet resultSet = CrudUtil.execute(sql, pattern, pattern, pattern, pattern, pattern);

        while (resultSet.next()){
            EmployeeDto employeeDto = new EmployeeDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
            employeeDtoArrayList.add(employeeDto);
        }
        return employeeDtoArrayList;
    }
    public ArrayList<EmployeeDto> getAllEmployees() throws ClassNotFoundException, SQLException{
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM employee";
        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet resultSet = CrudUtil.execute("SELECT * FROM employee");
        ArrayList<EmployeeDto> employeeDto = new ArrayList<>();

        while (resultSet.next()) {
            EmployeeDto dto = new EmployeeDto(
                    resultSet.getString("employee_id"),
                    resultSet.getString("name"),
                    resultSet.getString("nic"),
                    resultSet.getString("number"),
                    resultSet.getString("role")
            );
            employeeDto.add(dto);
        }
        return employeeDto;
    }
    public static String getNextEmployeeId() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT employee_id FROM employee ORDER BY employee_id DESC LIMIT 1");
        String tableCharacter = "EMP"; // Use any character Ex:- customer table for C, item table for I
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
