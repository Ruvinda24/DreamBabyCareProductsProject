package lk.ijse.finalProject.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

public class TaskDto {

    private String task_id;
    private String employee_id;
    private String description;
    private String status;

}
