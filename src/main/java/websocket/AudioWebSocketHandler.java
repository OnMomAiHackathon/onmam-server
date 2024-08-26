package websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import service.DiaryService;
import service.S3Service;

@Component
@RequiredArgsConstructor
public class AudioWebSocketHandler extends BinaryWebSocketHandler {

    private final S3Service s3Service;
    private final DiaryService diaryService;

    // *********** 테스트 필요 ***********
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        byte[] audioData = message.getPayload().array();

        // 세션에서 groupId 가져오기
        Long groupId = (Long) session.getAttributes().get("groupId");
        if (groupId == null) {
            session.close();  // groupId가 없으면 세션을 닫음
            return;
        }

        // S3에 오디오 파일 업로드
        String audioUrl = s3Service.uploadAudioFile(audioData, groupId.toString());

        // 이 오디오 URL을 OnmomDiaryEntry 엔티티에 저장
        Long diaryEntryId = 1L; // 예시로 diaryEntryId를 설정함
        diaryService.updateDiaryEntryWithAudioUrl(diaryEntryId, audioUrl);

        // 클라이언트에 오디오 URL 전송
        session.sendMessage(new TextMessage("Audio URL: " + audioUrl));
    }
}
