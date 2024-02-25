package lk.ijse.Shop.dto.tm;


import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeTm {
    private String id;
    private String name;

    private String intime;
    private String leavetime;

}
