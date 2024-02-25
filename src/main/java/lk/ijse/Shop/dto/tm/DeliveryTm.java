package lk.ijse.Shop.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class DeliveryTm {
    private String deliveryId;
    private String orderId;
    private String employeeId;
    private String location;
    private String deliveryStatus;
    private String tel;
}
