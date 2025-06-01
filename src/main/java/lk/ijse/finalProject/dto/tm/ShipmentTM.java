package lk.ijse.finalProject.dto.tm;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ShipmentTM {
    private String shipment_id;
    private String tracking_number;
    private LocalDate shipment_date;
}
