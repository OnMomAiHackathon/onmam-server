package dto.group;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupMemberUpdateRequest {
    private String groupName;  // 그룹 이름 수정
    private List<Member> members;

    @Getter
    @Setter
    public static class Member {
        private Long userId;
        private Long targetUserId;// 닉네임을 설정하는 대상 유저의 ID (타겟 유저)
        private String nickname;
    }
}
