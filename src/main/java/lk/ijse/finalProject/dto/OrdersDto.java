package lk.ijse.finalProject.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class OrdersDto {

    private String order_id;
    private String order_date;
    private String customer_id;
    private String shipment_id;
    private String status;

}
