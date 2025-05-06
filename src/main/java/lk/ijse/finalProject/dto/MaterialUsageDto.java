package lk.ijse.finalProject.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

public class MaterialUsageDto {

    private String usage_id;
    private String production_id;
    private String material_id;
    private int quantity_used;

}
