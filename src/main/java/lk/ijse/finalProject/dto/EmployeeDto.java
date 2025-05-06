package lk.ijse.finalProject.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class EmployeeDto {
    private  String employee_id;
    private  String name;
    private String nic;
    private String number;
    private String role;

}
