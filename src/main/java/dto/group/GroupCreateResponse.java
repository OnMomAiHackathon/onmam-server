package dto.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GroupCreateResponse {
    private Long groupId;
    private String message;
}
