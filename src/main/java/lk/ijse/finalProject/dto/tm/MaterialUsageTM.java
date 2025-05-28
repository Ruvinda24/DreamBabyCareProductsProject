package lk.ijse.finalProject.dto.tm;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

public class MaterialUsageTM {
    private String usage_id;
    private String production_id;
    private String material_id;
    private int quantity_used;
}
