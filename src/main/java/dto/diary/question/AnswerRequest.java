package dto.diary.question;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerRequest {
    private String question;
    private String answer;
}
