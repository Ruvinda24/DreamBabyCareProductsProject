package lk.ijse.finalProject.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class MaterialTM {
    private  String material_id;
    private  String name;
    private  String color_type;
    private int quantity;
}
