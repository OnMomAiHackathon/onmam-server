package dto.user.get;

import entity.user.OnmomUser;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserResponseDto {
    private Long userId;
    private String kakaoId;
    private String email;
    private String name;
    private LocalDate birthdate;
    private String phone;
    private String role;
    private String gender;

    public UserResponseDto(OnmomUser user) {
        this.userId = user.getUserId();
        this.kakaoId = user.getKakaoId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.birthdate = user.getBirthdate();
        this.phone = user.getPhone();
        this.role = user.getRole();
        this.gender = user.getGender();
    }
}
