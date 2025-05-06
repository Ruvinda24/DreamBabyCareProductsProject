package lk.ijse.finalProject.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

public class ShipmentDto {

    private String shipment_id;
    private String tracking_number;
    private String shipment_date;

}
