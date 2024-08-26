package entity.group;

import entity.user.OnmomUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "userNickname")
@Getter
@NoArgsConstructor
public class UserNickname {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private OnmomUser user;

    @ManyToOne
    @JoinColumn(name = "targetUserId")
    private OnmomUser targetUser;

    @ManyToOne
    @JoinColumn(name = "groupId")
    private OnmomGroup group;

    private String nickname;

    private String profileImageUrl; //프로필 이미지

    @Builder
    public UserNickname(OnmomUser user, OnmomUser targetUser, OnmomGroup group, String nickname, String profileImageUrl) {
        this.user = user;
        this.targetUser = targetUser;
        this.group = group;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    // 프로필 이미지 URL을 업데이트할 메서드
    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    // 닉네임을 업데이트할 메서드
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

}
