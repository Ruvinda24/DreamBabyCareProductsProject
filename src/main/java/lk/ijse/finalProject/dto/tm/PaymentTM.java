package lk.ijse.finalProject.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class PaymentTM {
    private String payment_id;
    private String order_id;
    private double amount;
    private String payment_method;
}
