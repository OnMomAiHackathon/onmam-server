package ai.chatgpt.controller;

import ai.chatgpt.service.ChatGPTService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class ChatGPTController {
    private final ChatGPTService chatGPTService;


    @GetMapping("test")
    public String aiTest(@RequestParam("prompt") String prompt){
        System.out.println("ai controller");

        return chatGPTService.example(prompt);
    }
}
