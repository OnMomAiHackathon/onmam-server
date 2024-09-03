package controller;

import dto.main.groupImage.GroupImageResponse;
import dto.main.groupImage.MarkImageAsViewedRequest;
import dto.main.groupImage.SendImageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.MainService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("main")
@RequiredArgsConstructor
@Tag(name = "자식이 부모에게 이미지를 전송하는 API")
public class MainController {
    private final MainService mainService;


    // 그룹내 이미지 전송(공유)
    @PostMapping("sendImages")
    @Operation(summary = "그룹 내 이미지 공유", description = "(주로)자식이 부모에게 이미지를 보낼 때 사용됩니다.")
    public ResponseEntity<Map<String, String>> sendImage(@ModelAttribute SendImageRequest sendImageRequest) {
        return mainService.sendImage(sendImageRequest);
    }

    //모든 그룹 이미지 조회
    @GetMapping("/group/{groupId}/images")
    @Operation(summary = "모든 그룹 이미지 리스트 ", description = "그룹에 공유된 모든 이미지들을 불러옵니다.")
    public ResponseEntity<List<GroupImageResponse>> getAllImagesByGroupId(@PathVariable Long groupId) {
        List<GroupImageResponse> images = mainService.getAllImagesByGroupId(groupId);
        return ResponseEntity.ok(images);
    }

    //이미지를 볼 때 기록
    @PostMapping("/user/{userId}/image/{imageId}/view")
    @Operation(summary = "그룹 내 공유된 이미지 확인시 읽음 처리", description = "이미지를 읽음 처리합니다.")
    public ResponseEntity<Map<String,String>> markImageAsViewed(@PathVariable Long userId, @PathVariable Long imageId) {
        Map<String,String> response = mainService.markImageAsViewed(userId,imageId);
        return ResponseEntity.ok(response);
    }

    //이미지를 봤는지 확인
    @GetMapping("/user/{userId}/image/{imageId}/viewed")
    @Operation(summary = "그룹원의 이미지 확인 여부 체크", description = "그룹원의 이미지 확인 여부를 체크합니다.")
    public ResponseEntity<Map<String, String>> hasUserViewedImage(@PathVariable Long userId, @PathVariable Long imageId) {
        Map<String, String> response = mainService.hasUserViewedImage(imageId, userId);
        return ResponseEntity.ok(response);
    }
}
