package entity.user;

import entity.group.OnmomGroup;
import entity.group.UserNickname;
import entity.medication.OnmomMedication;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "onmomUser")
@Getter
@NoArgsConstructor
public class OnmomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String kakaoId; // 카카오아이디

    private String email; // 이메일
    private String password; // 비번
    private String name; // 이름
    private String gender; // 성별
    private LocalDate birthdate; // 생일
    private String phone; // 휴대폰

    // !!! 정규화에 위반한 필드 !!!
    // but 프로젝트 단순성을 위해 role을 유저에 포함했다.
    private String role; // 독거노인 or 자식

    // N명의 유저는 하나의 그룹에 속할 수 있다.
    @ManyToOne
    @JoinColumn(name = "groupId")
    private OnmomGroup group;

    // 하나의 유저는 여러 복약 정보를 가질 수 있다.
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<OnmomMedication> medications;


    // 사용자가 설정한 닉네임들
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<UserNickname> nicknamesSetByUser;

    // 사용자가 타인에게 설정된 닉네임들
    @OneToMany(mappedBy = "targetUser", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<UserNickname> nicknamesSetOnUser;


    @Builder
    public OnmomUser(String email, String password, String name, LocalDate birthdate, String gender, String phone, String kakaoId, String role, OnmomGroup group) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.birthdate = birthdate;
        this.gender = gender;
        this.phone = phone;
        this.kakaoId = kakaoId;
        this.role = role;
        this.group = group;
    }

    // 그룹생성시 set Group!
    public void setGroup(OnmomGroup group) {
        this.group = group;
    }
}
