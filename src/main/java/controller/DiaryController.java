package controller;

import dto.ai.AIDiaryResponse;
import dto.diary.DiaryEntryRequest;
import dto.diary.DiaryEntryResponse;
import dto.diary.question.AnswerRequest;
import dto.diary.question.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.DiaryService;
import service.MedicationService;
import service.ai.AIService;
import service.ai.chatgpt.ChatGPTService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/diaries")
@RequiredArgsConstructor
@Tag(name = "그림일기 관련 API")
public class DiaryController {
    private final DiaryService diaryService;
    private final AIService aiService;
    private final ChatGPTService chatGPTService;
    private final MedicationService medicationService;


    //다이어리 생성
    @PostMapping("/create")
    @Operation(summary = "그림 일기 생성", description = "AI 그림일기를 생성하고 사용자의 음성을 텍스트로 변환하여 생성형 AI를 통해 일기로 요약합니다.")
    @Transactional
    public ResponseEntity<DiaryEntryResponse> createDiaryEntry(@ModelAttribute DiaryEntryRequest request) throws IOException {
        //오디오 MultipartFile을 파일로 변환하여 ai단에 넘김
        AIDiaryResponse aiDiaryResponse = aiService.processResponse(request.getAudioFile(), request.getUserId());

        //그림일기 생성
        DiaryEntryResponse response = null;
        try {
            response = diaryService.createDiaryEntry(request,aiDiaryResponse);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }


        // AI에서 복약 정보를 먹었다는 결과를 받았을 때만 복약 정보 업데이트
        boolean isEatedDrug = aiDiaryResponse.isMedicationStatus(); // 약을 먹었는지 여부는 AI의 응답으로 받아옴
        if (isEatedDrug) {
            medicationService.updateTodayMedication(request.getGroupId(), request.getUserId());
        }

        //응답에 셋팅
        response.setImageUrl(aiDiaryResponse.getImageURL());
        response.setSummaryContent(aiDiaryResponse.getSummary());
        response.setMedicationStatus(aiDiaryResponse.isMedicationStatus());

        return ResponseEntity.ok(response);
    }

    //특정 다이어리 조회
    @GetMapping("/{diaryEntryId}")
    @Operation(summary = "특정 그림 일기 조회", description = "그림일기 아이디를 통해 특정 그림 일기를 조회합니다.")
    public ResponseEntity<DiaryEntryResponse> getDiaryEntry(@PathVariable Long diaryEntryId) {
        DiaryEntryResponse response = diaryService.getDiaryEntry(diaryEntryId);
        return ResponseEntity.ok(response);
    }

    //다이어리 월별 조회
    @GetMapping("/monthly")
    @Operation(summary = "그림 일기 월별 조회", description = "월별 그림일기를 조회합니다.")
    public ResponseEntity<List<DiaryEntryResponse>> getMonthlyDiaryEntries(@RequestParam Long groupId,
                                                                           @RequestParam Long userId,
                                                                           @RequestParam int year,
                                                                           @RequestParam int month) {
        List<DiaryEntryResponse> response = diaryService.getMonthlyDiaryEntries(groupId,userId,year,month);
        return ResponseEntity.ok(response);
    }

    //모든 다이어리 조회
    @GetMapping
    @Operation(summary = "모든 그림일기 조회", description = "모든 그림일기를 조회합니다.")
    public ResponseEntity<List<DiaryEntryResponse>> getAllDiaryEntries() {
        List<DiaryEntryResponse> diaryEntries = diaryService.getAllDiaryEntries();
        return ResponseEntity.ok(diaryEntries);
    }

    // 그룹별 이미지 및 오디오 파일 저장
    // *********** 테스트 필요 ***********
//    @PostMapping("/upload")
//    public ResponseEntity<DiaryEntryResponse> uploadFiles(@ModelAttribute DiaryEntryRequest request) throws IOException {
//        // DiaryService를 통해 그림일기 저장
//        DiaryEntryResponse diaryEntryResponse = diaryService.saveDiaryEntry(request);
//        return ResponseEntity.ok(diaryEntryResponse);
//    }

    // 특정 그룹의 이미지 및 오디오 URL들을 가져오기
    // *********** 테스트 필요 ***********
    @GetMapping("/group/{groupId}/media")
    @Operation(summary = "이미지 및 오디오 불러오기", description = "특정 그룹의 이미지 및 오디오들을 가져옵니다.")
    public ResponseEntity<List<DiaryEntryResponse>> getGroupMedia(@PathVariable Long groupId) {
        List<DiaryEntryResponse> diaryEntryResponses = diaryService.getGroupMedia(groupId);
        return ResponseEntity.ok(diaryEntryResponses);
    }


    // 질문에 대한 답변 받기
    @PostMapping("/entries/{diaryEntryId}/answer")
    @Operation(summary = "그림일기 생성시 나오는 질문에 대한 답변 받기", description = "클라이언트 단의 그림일기 생성시 질문에 대한 답변을 서버에 저장합니다.")
    public ResponseEntity<?> receiveAnswer(@PathVariable Long diaryEntryId, @RequestBody AnswerRequest answerRequest) {
        diaryService.saveAnswer(diaryEntryId, answerRequest.getQuestion(), answerRequest.getAnswer());
        return ResponseEntity.ok(new ResponseMessage("다이어리 질문에 대한 답변이 성공적으로 전달되었습니다."));
    }
}
