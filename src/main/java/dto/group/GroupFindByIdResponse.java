package dto.group;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class GroupFindByIdResponse {
    private Long groupId;
    private String groupName;
    private List<GroupUserResponse> members;
    private Long groupOwnerUserId;//0831추가
    private LocalDate createdAt;//0831추가
    private String groupImageUrl;//0831추가
}
