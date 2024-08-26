package dto.group.invite;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InviteAcceptRequest {
    private Long userId;
    private String inviteCode;
}
