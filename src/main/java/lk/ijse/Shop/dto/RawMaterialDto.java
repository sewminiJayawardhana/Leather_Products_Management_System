package lk.ijse.Shop.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class RawMaterialDto {
    private String rawMaterialId;
    private double qtyOnStock;
    private double unitPrice;
}
