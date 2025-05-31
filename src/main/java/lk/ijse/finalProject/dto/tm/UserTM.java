package lk.ijse.finalProject.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class UserTM {
    private String user_id;
    private  String user_name;
    private  String password;
    private  String email;
    private  String name;
    private  String phone_number;

}
