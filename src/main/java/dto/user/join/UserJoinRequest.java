package dto.user.join;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserJoinRequest {
    private String kakaoId;  // Kakao ID는 선택적
    private String email;
    private String gender;
    private String password;
    private String name;
    private LocalDate birthdate;
    private String phone;
}
