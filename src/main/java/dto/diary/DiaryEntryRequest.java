package dto.diary;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class DiaryEntryRequest {
    private Long groupId;
    private String textContent;
    private String question1;  // 첫 번째 질문
    private String answer1;    // 첫 번째 답변
    private String question2;  // 두 번째 질문
    private String answer2;    // 두 번째 답변
    private MultipartFile audioFile;
}