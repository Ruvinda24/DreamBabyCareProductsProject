package lk.ijse.finalProject.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ShipmentTM {
    private String shipment_id;
    private String tracking_number;
    private String shipment_date;
}
