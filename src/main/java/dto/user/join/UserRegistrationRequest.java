package dto.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserRegistrationRequest {
    private String email;
    private String password;
    private String name;
    private LocalDate birthdate;
    private String phone;
}
