package lk.ijse.finalProject.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class TaskTM {
    private String task_id;
    private String employee_id;
    private String description;
    private String status;
}
