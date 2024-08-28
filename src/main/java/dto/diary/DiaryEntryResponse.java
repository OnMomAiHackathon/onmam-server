package dto.diary;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class DiaryEntryResponse {
    private Long diaryEntryId;
    private String title;
    private String textContent;
    private String imageUrl;
    private String audioUrl;
    private List<DailyAnswerResponse> dailyAnswers;
    private LocalDate createdAt;
}
