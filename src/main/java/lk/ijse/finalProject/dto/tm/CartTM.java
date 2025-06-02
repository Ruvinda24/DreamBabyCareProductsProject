package lk.ijse.finalProject.dto.tm;

import lombok.*;

import javafx.scene.control.Button;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CartTM {
    private String customerId;
    private String itemId;
    private String itemName;
    private int cartQty;
    private double unitPrice;
    private double total;
    private Button btnRemove;

}
