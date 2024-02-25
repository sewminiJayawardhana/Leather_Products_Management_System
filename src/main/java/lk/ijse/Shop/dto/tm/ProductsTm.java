package lk.ijse.Shop.dto.tm;
import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductsTm {
    private String id;
    private String description;
    private double unitprice;
    private int qtyonhand;
    private Button btn;


}
