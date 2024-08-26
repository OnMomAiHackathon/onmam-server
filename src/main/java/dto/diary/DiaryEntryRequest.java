package dto.diary;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class DiaryEntryRequest {
    private Long groupId;
    private String textContent;
    private MultipartFile audioFile; // ************ 웹소켓으로 구현
}
