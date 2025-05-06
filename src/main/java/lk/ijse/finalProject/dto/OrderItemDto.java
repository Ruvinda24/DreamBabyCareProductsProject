package lk.ijse.finalProject.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

public class OrderItemDto {

    private String order_item_id;
    private String order_id;
    private String inventory_id;
    private int quantity;
    private double amount;

}
