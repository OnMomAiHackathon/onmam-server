package dto.auth.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private Long userId;
    private String kakaoId;
    private String email;
    private String name;
    private Long groupId;

}
