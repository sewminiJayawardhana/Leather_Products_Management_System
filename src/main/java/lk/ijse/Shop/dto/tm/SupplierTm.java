package lk.ijse.Shop.dto.tm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@AllArgsConstructor
@Data
public class SupplierTm {
    private String supplierId;
    private String supplierName;
    private String address;
    private String tel;

}
