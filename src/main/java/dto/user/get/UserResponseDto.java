package dto.user.get;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String kakaoId;
    private String email;
    private String name;
    private LocalDate birthdate;
    private String phone;
    private String role;
    private String gender;
    private String accessToken;  // 액세스 토큰 추가
}
