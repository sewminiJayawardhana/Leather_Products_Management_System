package lk.ijse.Shop.dto.tm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SalaryTm {
    private String id;
    private String e_id;
    private String month;
    private double salary;
}
