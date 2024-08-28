package entity.diary;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "onmomDailyAnswer")
@Getter
@NoArgsConstructor
public class OnmomDailyAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 여러 답변은 하나의 다이어리 엔트리와 연결된다.
    @ManyToOne
    @JoinColumn(name = "diaryEntryId", nullable = false)
    private OnmomDiaryEntry diaryEntry;

    private String questionText; // 질문 내용
    private String answerText; // 답변 내용

    @Column(updatable = false)
    private LocalDate createdAt; // 답변 생성 날짜

    @Builder
    public OnmomDailyAnswer(OnmomDiaryEntry diaryEntry, String questionText, String answerText, LocalDate createdAt) {
        this.diaryEntry = diaryEntry;
        this.questionText = questionText;
        this.answerText = answerText;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }
}