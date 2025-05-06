package lk.ijse.finalProject.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CustomerDto {
    private String customerId;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private String orderPlatForm;
}
