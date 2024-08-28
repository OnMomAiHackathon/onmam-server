package dto.group;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GroupFindByIdResponse {
    private Long groupId;
    private String groupName;
    private List<GroupUserResponse> members;
}
