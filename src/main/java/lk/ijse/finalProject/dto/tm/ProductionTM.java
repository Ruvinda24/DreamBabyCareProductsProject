package lk.ijse.finalProject.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ProductionTM {
    private String production_id;
    private String inventory_id;
    private String description;
    private String status;
}
