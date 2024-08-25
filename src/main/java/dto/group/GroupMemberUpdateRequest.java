package dto.group;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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
        private String nickname;
        private MultipartFile profileImageFile;
    }
}
