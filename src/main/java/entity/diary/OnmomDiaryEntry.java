package entity.diary;

import entity.group.OnmomGroup;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

// 그림일기의 기본 정보를 저장하는 엔티티
@Entity
@Table(name = "onmomDiaryEntry")
@Getter
@NoArgsConstructor
public class OnmomDiaryEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diaryEntryId;

    // 여러 그림일기는 하나의 그룹에 속한다.
    @ManyToOne
    @JoinColumn(name = "groupId")
    private OnmomGroup group;

    @Column(columnDefinition = "TEXT")
    private String textContent; // 그림일기의 텍스트 내용
    private String imageURL; // 그림일기의 이미지 URL
    private String audioURL; // 음성 파일의 URL
    private LocalDate createdAt; // 그림일기 생성 날짜

    // 하나의 다이어리 항목은 여러 일상 답변을 가질 수 있다.
    @OneToMany(mappedBy = "diaryEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OnmomDailyAnswer> dailyAnswers;

    @Builder
    public OnmomDiaryEntry(Long diaryEntryId, OnmomGroup group, String textContent, String imageURL, String audioURL, LocalDate createdAt) {
        this.diaryEntryId = diaryEntryId;
        this.group = group;
        this.textContent = textContent;
        this.imageURL = imageURL;
        this.audioURL = audioURL;
        this.createdAt = createdAt;
    }

    public void updateAudioUrl(String audioUrl) {
        this.audioURL = audioUrl;
    }
}
