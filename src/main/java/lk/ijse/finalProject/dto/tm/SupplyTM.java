package lk.ijse.finalProject.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

public class SupplyTM {
    private String supply_id;
    private String supplier_id;
    private String material_id;
    private double amount;
    private int quantity;
}
