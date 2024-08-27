package service;

import dto.diary.DailyAnswerResponse;
import dto.diary.DiaryEntryRequest;
import dto.diary.DiaryEntryResponse;
import entity.diary.OnmomDailyAnswer;
import entity.diary.OnmomDiaryEntry;
import entity.group.OnmomGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import repository.diary.DiaryEntryRepository;
import repository.diary.OnmomDailyAnswerRepository;
import repository.group.GroupRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final S3Service s3Service;
    private final DiaryEntryRepository diaryEntryRepository;
    private final GroupRepository groupRepository;
    private final OnmomDailyAnswerRepository dailyAnswerRepository;

//    public DiaryEntryResponse saveDiaryEntry(DiaryEntryRequest request) throws IOException {
//        OnmomGroup group = groupRepository.findById(request.getGroupId())
//                .orElseThrow(() -> new IllegalArgumentException("Invalid group ID"));
//
//        // 이미지 및 오디오 파일을 S3에 업로드하고 URL을 가져오기
////        String imageUrl = s3Service.uploadFile(request.getImageFile(), request.getGroupId().toString());
//        String audioUrl = s3Service.uploadFile(request.getAudioFile(), request.getGroupId().toString());
//
//        // OnmomDiaryEntry 엔티티 생성 및 저장
//        OnmomDiaryEntry diaryEntry = OnmomDiaryEntry.builder()
//                .group(group)
//                .textContent(request.getTextContent())
////                .imageURL(imageUrl)
//                .audioURL(audioUrl)
//                .createdAt(LocalDate.now())
//                .build();
//
//        diaryEntryRepository.save(diaryEntry);
//
//        // DiaryEntryResponse DTO 생성 및 반환
//        return DiaryEntryResponse.builder()
//                .diaryEntryId(diaryEntry.getDiaryEntryId())
//                .textContent(diaryEntry.getTextContent())
//                .imageUrl(diaryEntry.getImageURL())
//                .audioUrl(diaryEntry.getAudioURL())
//                .build();
//    }

    public DiaryEntryResponse getDiaryEntry(Long diaryEntryId) {
        OnmomDiaryEntry diaryEntry = diaryEntryRepository.findById(diaryEntryId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 다이어리 ID입니다."));

        // 질문과 답변을 DailyAnswerResponse로 변환
        List<DailyAnswerResponse> dailyAnswerResponses = diaryEntry.getDailyAnswers().stream()
                .map(answer -> DailyAnswerResponse.builder()
                        .id(answer.getId())
                        .questionText(answer.getQuestionText())
                        .answerText(answer.getAnswerText())
                        .createdAt(answer.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        // 응답 생성
        return DiaryEntryResponse.builder()
                .diaryEntryId(diaryEntry.getDiaryEntryId())
                .textContent(diaryEntry.getTextContent())
                .imageUrl(diaryEntry.getImageURL())
                .audioUrl(diaryEntry.getAudioURL())
                .dailyAnswers(dailyAnswerResponses)
                .build();
    }

//    public DiaryEntryResponse getDiaryEntry(Long diaryEntryId) {
//        OnmomDiaryEntry diaryEntry = diaryEntryRepository.findById(diaryEntryId)
//                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 다이어리 아이디입니다."));
//
//        return DiaryEntryResponse.builder()
//                .diaryEntryId(diaryEntry.getDiaryEntryId())
//                .textContent(diaryEntry.getTextContent())
//                .imageUrl(diaryEntry.getImageURL())
//                .audioUrl(diaryEntry.getAudioURL())
//                .build();
//    }




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

    // 질문과 답변 저장
    public void saveAnswer(Long diaryEntryId, String questionText, String answer) {
        // diaryEntryId로 다이어리 항목을 찾음
        OnmomDiaryEntry diaryEntry = diaryEntryRepository.findById(diaryEntryId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 다이어리 ID입니다."));

        // 새로운 일상 답변 엔트리 생성
        OnmomDailyAnswer dailyAnswer = OnmomDailyAnswer.builder()
                .diaryEntry(diaryEntry)
                .questionText(questionText)
                .answerText(answer)
                .createdAt(LocalDate.now())
                .build();

        dailyAnswerRepository.save(dailyAnswer);
    }

    public DiaryEntryResponse  createDiaryEntry(DiaryEntryRequest request) throws IOException {
        // 그룹 조회
        OnmomGroup group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 그룹 ID입니다."));

        // 오디오 파일을 byte[]로 변환
        byte[] audioData = request.getAudioFile().getBytes();

        // S3에 오디오 파일 업로드
        String audioUrl = s3Service.uploadAudioFile(audioData, request.getGroupId().toString());

        // OnmomDiaryEntry 생성 (이미지 URL은 나중에 DALL·E로 생성)
        OnmomDiaryEntry diaryEntry = OnmomDiaryEntry.builder()
                .group(group)
                .textContent(request.getTextContent())
                .imageURL("") // 이미지 URL은 나중에 DALL·E로 생성
                .audioURL(audioUrl)
                .createdAt(LocalDate.now())
                .build();

        // 다이어리 엔트리 저장
        diaryEntry = diaryEntryRepository.save(diaryEntry);

        // 질문과 답변 저장
        List<OnmomDailyAnswer> dailyAnswers = new ArrayList<>();
        if (request.getQuestion1() != null && request.getAnswer1() != null) {
            OnmomDailyAnswer dailyAnswer1 = OnmomDailyAnswer.builder()
                    .diaryEntry(diaryEntry)
                    .questionText(request.getQuestion1())
                    .answerText(request.getAnswer1())
                    .createdAt(LocalDate.now())
                    .build();
            dailyAnswers.add(dailyAnswer1);
        }

        if (request.getQuestion2() != null && request.getAnswer2() != null) {
            OnmomDailyAnswer dailyAnswer2 = OnmomDailyAnswer.builder()
                    .diaryEntry(diaryEntry)
                    .questionText(request.getQuestion2())
                    .answerText(request.getAnswer2())
                    .createdAt(LocalDate.now())
                    .build();
            dailyAnswers.add(dailyAnswer2);
        }

        dailyAnswerRepository.saveAll(dailyAnswers);

        // 응답 생성 (질문과 답변을 포함)
        List<DailyAnswerResponse> dailyAnswerResponses = dailyAnswers.stream()
                .map(answer -> DailyAnswerResponse.builder()
                        .id(answer.getId())
                        .questionText(answer.getQuestionText())
                        .answerText(answer.getAnswerText())
                        .createdAt(answer.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return DiaryEntryResponse.builder()
                .diaryEntryId(diaryEntry.getDiaryEntryId())
                .textContent(diaryEntry.getTextContent())
                .imageUrl(diaryEntry.getImageURL())
                .audioUrl(diaryEntry.getAudioURL())
                .dailyAnswers(dailyAnswerResponses)  // 추가된 부분
                .build();
    }



}
