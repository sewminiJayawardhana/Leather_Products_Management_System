package lk.ijse.Shop.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SalaryDto {
    private String id;
    private String e_id;
    private String month;
    private double salary;
}
