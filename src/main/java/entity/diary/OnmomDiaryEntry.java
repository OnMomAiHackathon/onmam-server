package entity.diary;

import entity.group.OnmomGroup;
import entity.user.OnmomUser;
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

    // 여러 그림일기는 하나의 유저로부터 만들어질 수 있다
    @ManyToOne
    @JoinColumn(name = "userId")
    private OnmomUser user;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String transcribedContent; // 오디오 텍스트 내용
    @Column(columnDefinition = "TEXT")
    private String summaryText;//AI 요약된 내용
    private String imageURL; // 그림일기의 이미지 URL
    private String audioURL; // 음성 파일의 URL

    @Column(updatable = false)
    private LocalDate createdAt; // 그림일기 생성 날짜

    private boolean medicationStatus;



    // 하나의 다이어리 항목은 여러 일상 답변을 가질 수 있다.
    @OneToMany(mappedBy = "diaryEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OnmomDailyAnswer> dailyAnswers;

    @Builder
    public OnmomDiaryEntry(Long diaryEntryId, String title, OnmomGroup group, String transcribedContent, String summaryText, String imageURL, String audioURL, boolean medicationStatus) {
        this.diaryEntryId = diaryEntryId;
        this.title = title;
        this.group = group;
        this.transcribedContent = transcribedContent;
        this.summaryText = summaryText;
        this.imageURL = imageURL;
        this.audioURL = audioURL;
        this.medicationStatus = medicationStatus;
    }


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }

    public void updateAudioUrl(String audioUrl) {
        this.audioURL = audioUrl;
    }
}
