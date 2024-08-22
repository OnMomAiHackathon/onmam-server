package ai.chatgpt.service;

import org.springframework.stereotype.Service;

@Service
public class ChatGPTService {

    public String example(String prompt){
        System.out.println("ai service: example()");
        String message = "응답";
        return "요청메시지 "+prompt+"를 받았고, 그 결과로"+message+"를 전달합니다.";

    }

}
