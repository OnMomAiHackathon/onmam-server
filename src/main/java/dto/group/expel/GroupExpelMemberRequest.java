package dto.group.expel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupExpelMemberRequest {
    private Long userId;
    private Long targetUserId;
}
