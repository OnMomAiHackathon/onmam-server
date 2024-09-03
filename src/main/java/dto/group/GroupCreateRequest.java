package dto.group;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class GroupCreateRequest {
    private String groupName;
    private Long userId;
    private MultipartFile groupImage;
    private String role;
}
