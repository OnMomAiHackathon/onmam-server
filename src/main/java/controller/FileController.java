package controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import service.S3Service;

import java.io.IOException;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = "그룹 이미지 저장 API")
public class FileController {
    private final S3Service s3Service;

    // 그룹별 이미지 저장
    // aws의 S3버킷/image/groupId폴더에 이미지파일을 저장한다.
    // ================= Dalle API와 합쳐야할 필요가 있다 ===============
    @PostMapping("/upload")
    @Operation(summary = "그룹별 이미지 저장", description = "그룹 이미지를 저장합니다.")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("groupId") String groupId) throws IOException {
        String fileUrl = s3Service.uploadFile(file, groupId);
        return ResponseEntity.ok(fileUrl);
    }



}
