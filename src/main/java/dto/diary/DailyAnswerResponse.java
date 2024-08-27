package dto.diary;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class DailyAnswerResponse {
    private Long id;
    private String questionText;
    private String answerText;
    private LocalDate createdAt;
}