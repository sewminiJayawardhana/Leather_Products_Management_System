package lk.ijse.Shop.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductsDto {
    private String id;
    private String description;
    private double unitprice;
    private int qtyonhand;

}
