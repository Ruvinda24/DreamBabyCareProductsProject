package lk.ijse.finalProject.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class CustomerTM {
    private String customerId;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private String orderPlatForm;
}
