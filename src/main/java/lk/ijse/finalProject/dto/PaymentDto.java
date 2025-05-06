package lk.ijse.finalProject.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class PaymentDto {

    private String payment_id;
    private String order_id;
    private double amount;
    private String payment_method;

}
