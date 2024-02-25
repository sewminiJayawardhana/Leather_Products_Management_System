package lk.ijse.Shop.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class UserDTO {
    private String email;
    private String username;
    private String password;
}