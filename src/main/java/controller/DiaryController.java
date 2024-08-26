package controller;

import dto.diary.DiaryEntryRequest;
import dto.diary.DiaryEntryResponse;
import dto.diary.question.AnswerRequest;
import dto.diary.question.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.DiaryService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;


    @PostMapping("/create")
    public ResponseEntity<DiaryEntryResponse> createDiaryEntry(@ModelAttribute DiaryEntryRequest request) throws IOException {
        DiaryEntryResponse response = diaryService.createDiaryEntry(request);
        return ResponseEntity.ok(response);
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
    public ResponseEntity<List<DiaryEntryResponse>> getGroupMedia(@PathVariable Long groupId) {
        List<DiaryEntryResponse> diaryEntryResponses = diaryService.getGroupMedia(groupId);
        return ResponseEntity.ok(diaryEntryResponses);
    }

    // 특정 그림일기의 이미지 및 오디오 URL 가져오기
    // *********** 테스트 필요 ***********
    @GetMapping("/{diaryEntryId}/media")
    public ResponseEntity<DiaryEntryResponse> getDiaryEntryMedia(@PathVariable Long diaryEntryId) {
        DiaryEntryResponse diaryEntryResponse = diaryService.getDiaryEntry(diaryEntryId);
        return ResponseEntity.ok(diaryEntryResponse);
    }

    // 질문에 대한 답변 받기
    @PostMapping("/entries/{diaryEntryId}/answer")
    public ResponseEntity<?> receiveAnswer(@PathVariable Long diaryEntryId, @RequestBody AnswerRequest answerRequest) {
        diaryService.saveAnswer(diaryEntryId, answerRequest.getQuestion(), answerRequest.getAnswer());
        return ResponseEntity.ok(new ResponseMessage("다이어리 질문에 대한 답변이 성공적으로 전달되었습니다."));
    }
}
