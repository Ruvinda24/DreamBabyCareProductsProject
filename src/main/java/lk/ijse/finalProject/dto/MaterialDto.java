package lk.ijse.finalProject.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class MaterialDto {

    private  String material_id;
    private  String name;
    private  String color_type;
    private int quantity;

}
