package lk.ijse.finalProject.model;

import lk.ijse.finalProject.db.DBConnection;
import lk.ijse.finalProject.dto.UserDto;
import lk.ijse.finalProject.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserModel {

    public static boolean saveUsers(UserDto userDto) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute(
                "INSERT INTO user VALUES (?,?,?,?,?,?)",
                userDto.getUser_id(),
                userDto.getUser_name(),
                userDto.getPassword(),
                userDto.getEmail(),
                userDto.getName(),
                userDto.getPhone_number()
        );
    }
    public boolean updateUsers(UserDto userDto) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute(
                "UPDATE user SET user_name = ?, password = ?, email = ?, name = ?, phone_number = ?WHERE user_id= ?",

                userDto.getUser_name(),
                userDto.getPassword(),
                userDto.getEmail(),
                userDto.getName(),
                userDto.getPhone_number(),
                userDto.getUser_id()
        );
    }

    public boolean deleteUsers(String user_id) throws ClassNotFoundException, SQLException{

        return CrudUtil.execute("DELETE FROM user WHERE user_id = ?", user_id);
    }

    public UserDto searchUsers(String user_id) throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM user WHERE user_id = ?", user_id);

        if(rst.next()){
            UserDto dto = new UserDto(
                    rst.getString("user_id"),
                    rst.getString("user_name"),
                    rst.getString("password"),
                    rst.getString("email"),
                    rst.getString("name"),
                    rst.getString("phone_number")
            );
            return dto;
        }
        return null;
    }
    public ArrayList<UserDto> getAllUsers() throws ClassNotFoundException, SQLException{

        ResultSet rst = CrudUtil.execute("SELECT * FROM user");
        ArrayList<UserDto> userDto = new ArrayList<>();

        while (rst.next()) {
            UserDto dto = new UserDto(
                    rst.getString("user_id"),
                    rst.getString("user_name"),
                    rst.getString("password"),
                    rst.getString("email"),
                    rst.getString("name"),
                    rst.getString("phone_number")
            );
            userDto.add(dto);
        }
        return userDto;
    }

    public static String getNextUserId() throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT user_id FROM user ORDER BY user_id DESC LIMIT 1");
        String tableCharacter = "U"; // Use any character Ex:- customer table for C, item table for I
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
