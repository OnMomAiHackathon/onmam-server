package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ai.AIDiaryResponse;
import dto.diary.DailyAnswerResponse;
import dto.diary.DiaryEntryRequest;
import dto.diary.DiaryEntryResponse;
import entity.diary.OnmomDailyAnswer;
import entity.diary.OnmomDiaryEntry;
import entity.group.OnmomGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.diary.DiaryEntryRepository;
import repository.diary.OnmomDailyAnswerRepository;
import repository.group.GroupRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.YearMonth;
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
    private final MedicationService medicationService;
    private final ObjectMapper objectMapper;

    // 다이어리 엔트리 생성
    public DiaryEntryResponse createDiaryEntry(DiaryEntryRequest request, AIDiaryResponse aiDiaryResponse) throws IOException, URISyntaxException {
        // 그룹 조회
        OnmomGroup group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 그룹 ID입니다."));

        // 오디오 파일을 byte[]로 변환
        byte[] audioData = request.getAudioFile().getBytes();

        // S3에 오디오 파일 업로드
        String audioUrl = s3Service.uploadAudioFile(audioData, request.getGroupId().toString());

        // OnmomDiaryEntry 생성
        OnmomDiaryEntry diaryEntry = OnmomDiaryEntry.builder()
                .group(group)
                .title(aiDiaryResponse.getTitle()) // aiDiaryResponse에서 직접 title 가져오기
                .transcribedContent(aiDiaryResponse.getTranslatedContent())
                .summaryText(aiDiaryResponse.getSummary())
                .imageURL(aiDiaryResponse.getImageURL())
                .audioURL(audioUrl)
                .medicationStatus(aiDiaryResponse.isMedicationStatus()) // aiDiaryResponse에서 직접 medicationStatus 가져오기
                .build();

        // 다이어리 엔트리 저장
        diaryEntry = diaryEntryRepository.save(diaryEntry);

        // 복약 상태가 true일 경우 복약 로그 추가
        if (aiDiaryResponse.isMedicationStatus()) {
            medicationService.updateTodayMedication(request.getGroupId(), request.getUserId());
        }

        // 질문과 답변 저장 (생략 가능)
        List<OnmomDailyAnswer> dailyAnswers = new ArrayList<>();
        // 질문과 답변을 저장할 로직이 있으면 추가

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
                .title(diaryEntry.getTitle())
                .translatedContent(diaryEntry.getTranscribedContent())
                .summaryContent(diaryEntry.getSummaryText())
                .imageUrl(diaryEntry.getImageURL())
                .audioUrl(diaryEntry.getAudioURL())
                .medicationStatus(diaryEntry.isMedicationStatus())
                .createdAt(diaryEntry.getCreatedAt())
                .dailyAnswers(dailyAnswerResponses)
                .build();
    }


    // 특정 다이어리 엔트리 가져오기
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
                .title(diaryEntry.getTitle())
                .translatedContent(diaryEntry.getTranscribedContent())
                .summaryContent(diaryEntry.getSummaryText())
                .imageUrl(diaryEntry.getImageURL())
                .audioUrl(diaryEntry.getAudioURL())
                .medicationStatus(diaryEntry.isMedicationStatus())
                .createdAt(diaryEntry.getCreatedAt())
                .dailyAnswers(dailyAnswerResponses)
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
                        .title(entry.getTitle())
                        .translatedContent(entry.getTranscribedContent())
                        .summaryContent(entry.getSummaryText())
                        .imageUrl(entry.getImageURL())
                        .audioUrl(entry.getAudioURL())
                        .medicationStatus(entry.isMedicationStatus())
                        .createdAt(entry.getCreatedAt())
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

    // 특정 연월의 다이어리 엔트리 가져오기
    public List<DiaryEntryResponse> getMonthlyDiaryEntries(Long groupId, Long userId, int year,int month) {

        // YearMonth를 이용해 해당 연도와 월을 표현
        YearMonth yearMonth = YearMonth.of(year, month);

        // 해당 월의 첫 번째 날과 마지막 날을 계산
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        // 주어진 그룹 ID로 그룹 조회
        OnmomGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 그룹 ID입니다."));

        boolean isContainUserIdByGroup = group.getUsers().stream().anyMatch(onmomUser ->
                onmomUser.getUserId().equals(userId));
        if(!isContainUserIdByGroup){
            throw new IllegalArgumentException("유저아이디가 해당 그룹에 속해있지 않습니다.");
        }

        // 해당 연월에 속하는 다이어리 엔트리들 조회
        List<OnmomDiaryEntry> diaryEntries = diaryEntryRepository.findByGroupAndCreatedAtBetween(group, startDate, endDate);

        // 다이어리 엔트리들을 응답 객체로 변환
        return diaryEntries.stream()
                .map(entry -> {
                    // 질문과 답변을 DailyAnswerResponse로 변환
                    List<DailyAnswerResponse> dailyAnswerResponses = entry.getDailyAnswers().stream()
                            .map(answer -> DailyAnswerResponse.builder()
                                    .id(answer.getId())
                                    .questionText(answer.getQuestionText())
                                    .answerText(answer.getAnswerText())
                                    .createdAt(answer.getCreatedAt())
                                    .build())
                            .collect(Collectors.toList());

                    // DiaryEntryResponse 객체 생성
                    return DiaryEntryResponse.builder()
                            .diaryEntryId(entry.getDiaryEntryId())
                            .title(entry.getTitle())
                            .translatedContent(entry.getTranscribedContent())
                            .summaryContent(entry.getSummaryText())
                            .imageUrl(entry.getImageURL())
                            .audioUrl(entry.getAudioURL())
                            .medicationStatus(entry.isMedicationStatus())
                            .createdAt(entry.getCreatedAt())
                            .dailyAnswers(dailyAnswerResponses)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 모든 다이어리 엔트리 가져오기
    public List<DiaryEntryResponse> getAllDiaryEntries() {
        // 모든 다이어리 엔트리를 조회
        List<OnmomDiaryEntry> diaryEntries = diaryEntryRepository.findAll();

        // 조회된 엔트리들을 DiaryEntryResponse로 변환
        return diaryEntries.stream()
                .map(entry -> {
                    // 질문과 답변을 DailyAnswerResponse로 변환
                    List<DailyAnswerResponse> dailyAnswerResponses = entry.getDailyAnswers().stream()
                            .map(answer -> DailyAnswerResponse.builder()
                                    .id(answer.getId())
                                    .questionText(answer.getQuestionText())
                                    .answerText(answer.getAnswerText())
                                    .createdAt(answer.getCreatedAt())
                                    .build())
                            .collect(Collectors.toList());

                    // DiaryEntryResponse 객체 생성
                    return DiaryEntryResponse.builder()
                            .diaryEntryId(entry.getDiaryEntryId())
                            .title(entry.getTitle())
                            .translatedContent(entry.getTranscribedContent())
                            .summaryContent(entry.getSummaryText())
                            .imageUrl(entry.getImageURL())
                            .audioUrl(entry.getAudioURL())
                            .medicationStatus(entry.isMedicationStatus())
                            .createdAt(entry.getCreatedAt())
                            .dailyAnswers(dailyAnswerResponses)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
