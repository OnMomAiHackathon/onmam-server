package controller;

import dto.main.groupImage.GroupImageResponse;
import dto.main.groupImage.SendImageRequest;
import entity.group.GroupImage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.MainService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("main")
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;


    // 그룹내 이미지 전송(공유)
    @PostMapping("sendImages")
    public ResponseEntity<Map<String, String>> sendImage(@ModelAttribute SendImageRequest sendImageRequest) {
        return mainService.sendImage(sendImageRequest);
    }

    //모든 그룹 이미지 조회
    @GetMapping("group/{groupId}/images")
    public ResponseEntity<List<GroupImageResponse>> getAllImagesByGroupId(@PathVariable Long groupId) {
        List<GroupImageResponse> images = mainService.getAllImagesByGroupId(groupId);
        return ResponseEntity.ok(images);
    }
}
