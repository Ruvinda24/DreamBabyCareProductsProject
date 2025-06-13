package lk.ijse.finalProject.dto.tm;

import lombok.*;

import javafx.scene.control.Button;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderCartTM {
    private String customerId;
    private String itemId;
    private String itemName;
    private int cartQty;
    private double unitPrice;
    private double total;
    private String paymentMethod;
    private Button btnRemove;

}
