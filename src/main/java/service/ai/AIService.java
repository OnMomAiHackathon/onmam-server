package service.ai;

import dto.ai.AIDiaryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import service.ai.chatgpt.ChatGPTService;
import service.ai.dalle.DallEService;

import java.io.File;
import java.io.IOException;

@Service
public class AIService {
    private static final Logger log = LoggerFactory.getLogger(AIService.class);
    @Autowired
    private ChatGPTService chatGPTService;

    @Autowired
    private DallEService dallEService;

    public AIDiaryResponse processResponse(MultipartFile audioMultipartFile, Long userId) throws IOException {
        // MultipartFile을 File로 변환
        File audioFile = File.createTempFile("audio", ".mp3");
        audioMultipartFile.transferTo(audioFile);

        // aiDiaryResponse DTO 생성 및 반환
        String transcript = chatGPTService.transcribe(audioFile);
        System.out.println("transcript:"+transcript);

        if (transcript=="") {
            transcript = "발화 내용이 비었습니다.";
        }

        String summary = chatGPTService.createSummary(transcript);
        System.out.println(summary);
//        boolean medicationStatus = chatGPTService.getMedicationStatus(summary);
        boolean medicationStatus = false;
         String imageURL = dallEService.generateImageURL(summary,userId);


        AIDiaryResponse aiDiaryResponse = new AIDiaryResponse();
        aiDiaryResponse.setSummary(summary);
        aiDiaryResponse.setTranslatedContent(transcript);
        aiDiaryResponse.setImageURL(imageURL);
        aiDiaryResponse.setMedicationStatus(medicationStatus);

        return aiDiaryResponse;
    }
}