package dto.group;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GroupUserResponse {
    private Long userId;
    private String name;
    private String role;
}
