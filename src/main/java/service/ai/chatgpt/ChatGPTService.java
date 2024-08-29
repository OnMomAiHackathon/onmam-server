package service.ai.chatgpt;

import config.ai.AIConfig;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ChatGPTService {
    private final AIConfig aiConfig;

    @Value("${openai.api.api-key}")
    private String OPENAI_API_KEY;

    @Value("${openai.api.transcriptions.end-point}")
    private String TRANSCRIPTIONS_API_ENDPOINT;

    @Value("${openai.api.gpt.end-point}")
    private String GPT_API_ENDPOINT;

    // OkHttpClient에 타임아웃 설정 추가
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    public String transcribe(File audioFile) throws IOException {
        if (OPENAI_API_KEY == null || OPENAI_API_KEY.isEmpty()) {
            throw new IllegalStateException("API key is not set.");
        }

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", audioFile.getName(),
                        RequestBody.create(MediaType.parse("audio/mp3"), audioFile))
                .addFormDataPart("model", "whisper-1")
                .addFormDataPart("language", "ko")
                .addFormDataPart("response_format", "text")
                .build();

        Request request = new Request.Builder()
                .url(TRANSCRIPTIONS_API_ENDPOINT)
                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Transcription API request failed with response: " + response);
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    public String createSummary(String transcript) throws IOException {
        if (OPENAI_API_KEY == null || OPENAI_API_KEY.isEmpty()) {
            throw new IllegalStateException("API key is not set.");
        }

        String prompt = transcript;

        // JSON 객체를 생성하여 안전하게 JSON 문자열을 구성합니다.
        JSONObject json = new JSONObject();
        json.put("model", "gpt-3.5-turbo");  // 또는 사용하려는 다른 모델 이름
        json.put("messages", new JSONArray()
                .put(new JSONObject()
                        .put("role", "system")
                        .put("content", aiConfig.getSummaryPrompt()))
                .put(new JSONObject()
                        .put("role", "user")
                        .put("content", prompt))
        );
        json.put("max_tokens", 300);
        json.put("temperature", 0.7);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                json.toString()
        );

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")  // 올바른 엔드포인트 사용
                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            if (!response.isSuccessful()) {
                System.err.println("Summary API request failed with response: " + response);
                System.err.println("Response body: " + responseBody);  // 추가된 디버깅 정보 출력
                throw new IOException("Unexpected code " + response);
            }

            return extractMessageFromChatResponse(responseBody);
        }
    }

    private String extractMessageFromChatResponse(String responseBody) {
        JSONObject jsonResponse = new JSONObject(responseBody);
        JSONArray choices = jsonResponse.optJSONArray("choices");

        if (choices != null && choices.length() > 0) {
            JSONObject messageObject = choices.getJSONObject(0).optJSONObject("message");
            if (messageObject != null) {
                return messageObject.optString("content", "No content");
            }
        }

        return "No content available";
    }





    public boolean getMedicationStatus(String summary) {
        return summary.contains("true");
    }
}
