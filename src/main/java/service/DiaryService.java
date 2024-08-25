package service;

import dto.diary.DiaryEntryRequest;
import dto.diary.DiaryEntryResponse;
import entity.diary.OnmomDiaryEntry;
import entity.group.OnmomGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.diary.DiaryEntryRepository;
import repository.group.GroupRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final S3Service s3Service;
    private final DiaryEntryRepository diaryEntryRepository;
    private final GroupRepository groupRepository;

    public DiaryEntryResponse saveDiaryEntry(DiaryEntryRequest request) throws IOException {
        OnmomGroup group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid group ID"));

        // 이미지 및 오디오 파일을 S3에 업로드하고 URL을 가져오기
        String imageUrl = s3Service.uploadFile(request.getImageFile(), request.getGroupId().toString());
        String audioUrl = s3Service.uploadFile(request.getAudioFile(), request.getGroupId().toString());

        // OnmomDiaryEntry 엔티티 생성 및 저장
        OnmomDiaryEntry diaryEntry = OnmomDiaryEntry.builder()
                .group(group)
                .textContent(request.getTextContent())
                .imageURL(imageUrl)
                .audioURL(audioUrl)
                .createdAt(LocalDate.now())
                .build();

        diaryEntryRepository.save(diaryEntry);

        // DiaryEntryResponse DTO 생성 및 반환
        return DiaryEntryResponse.builder()
                .diaryEntryId(diaryEntry.getDiaryEntryId())
                .textContent(diaryEntry.getTextContent())
                .imageUrl(diaryEntry.getImageURL())
                .audioUrl(diaryEntry.getAudioURL())
                .build();
    }

    public DiaryEntryResponse getDiaryEntry(Long diaryEntryId) {
        OnmomDiaryEntry diaryEntry = diaryEntryRepository.findById(diaryEntryId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 다이어리 아이디입니다."));

        return DiaryEntryResponse.builder()
                .diaryEntryId(diaryEntry.getDiaryEntryId())
                .textContent(diaryEntry.getTextContent())
                .imageUrl(diaryEntry.getImageURL())
                .audioUrl(diaryEntry.getAudioURL())
                .build();
    }

    // 특정 그룹의 모든 그림일기의 이미지 및 오디오 URL 가져오기
    public List<DiaryEntryResponse> getGroupMedia(Long groupId) {
        OnmomGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 그룹 아이디입니다."));

        List<OnmomDiaryEntry> diaryEntries = diaryEntryRepository.findByGroup(group);

        return diaryEntries.stream()
                .map(entry -> DiaryEntryResponse.builder()
                        .diaryEntryId(entry.getDiaryEntryId())
                        .textContent(entry.getTextContent())
                        .imageUrl(entry.getImageURL())
                        .audioUrl(entry.getAudioURL())
                        .build())
                .collect(Collectors.toList());
    }


    // 오디오 URL 업데이트 로직 (웹소켓 이후)
    public void updateDiaryEntryWithAudioUrl(Long diaryEntryId, String audioUrl) {
        OnmomDiaryEntry diaryEntry = diaryEntryRepository.findById(diaryEntryId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 다이어리 아이디입니다."));

        diaryEntry.updateAudioUrl(audioUrl);
        diaryEntryRepository.save(diaryEntry);
    }
}
