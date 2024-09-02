package dto.main.groupImage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarkImageAsViewedRequest {
    private Long imageId;
    private Long userId;

}
