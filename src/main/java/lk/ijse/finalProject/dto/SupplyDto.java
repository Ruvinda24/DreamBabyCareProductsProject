package lk.ijse.finalProject.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

public class SupplyDto {

    private String supply_id;
    private String supplier_id;
    private String material_id;
    private double amount;
    private int quantity;

}
