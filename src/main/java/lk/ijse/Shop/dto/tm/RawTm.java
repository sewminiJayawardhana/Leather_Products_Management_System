package lk.ijse.Shop.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RawTm {
    private String rawMaterialId;
    private double qtyOnStock;
    private double unitPrice;
}
