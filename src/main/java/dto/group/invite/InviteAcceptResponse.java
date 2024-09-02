package dto.group.invite;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InviteAcceptResponse {
    private String message;
    private String groupId;

}
