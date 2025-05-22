package lk.ijse.finalProject.dto.tm;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrdersTM {
    private String order_id;
    private LocalDate order_date;
    private String customer_id;
    private String shipment_id;
    private String status;

}
