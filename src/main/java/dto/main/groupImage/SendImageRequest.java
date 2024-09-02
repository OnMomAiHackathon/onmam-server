package dto.main.groupImage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class SendImageRequest {
    private List<MultipartFile> imageFiles;
    private Long userId;
    private Long groupId;

}
