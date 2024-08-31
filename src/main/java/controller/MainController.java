package controller;

import dto.main.SendImageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.MainService;

import java.util.Map;

@RestController
@RequestMapping("main")
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;


    @PostMapping("sendImages")
    public ResponseEntity<Map<String, String>> sendImage(@ModelAttribute SendImageRequest sendImageRequest) {
        return mainService.sendImage(sendImageRequest);
    }
}
