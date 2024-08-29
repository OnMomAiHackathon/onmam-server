package service.ai.dalle;

import entity.user.OnmomUser;
import exception.user.get.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.apache.catalina.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import config.ai.AIConfig;
import repository.user.UserRepository;

import java.io.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DallEService {
    private final UserRepository userRepository;

    @Value("${openai.api.api-key}")
    private String OPENAI_API_KEY;

    @Value("${openai.api.dalle.end-point}")
    private String DALLE_API_ENDPOINT;

    static AIConfig gptConfig = new AIConfig();

    static final String prompt = gptConfig.getImagePrompt(); // config에 저장한 프롬프트 내용




    // 이미지를 생성하는 메서드
    public String generateImageURL(String summary, Long userId) {
        OnmomUser user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("유저 아이디를 찾을 수 없습니다. 생성된 유저아이디인지 확인하세요."));
        String gender = user.getGender(); // 성별
        int age; //나이
        if(user.getBirthdate()!=null){
            age = Period.between(LocalDate.now(), user.getBirthdate()).getYears();
        }else{
            throw new IllegalArgumentException("유저에 생년월일이 입력되어있지 않습니다.");
        }
        System.out.println("gender:"+gender);
        System.out.println("age:"+age);

        String finalPrompt = prompt + summary;

        // 사용자 정보 추가로 입력 시(성별, 나이 등)
        // finalPrompt += "<사용자 정보> " + 사용자_정보

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("model", "dall-e-3");
            jsonObject.put("prompt", finalPrompt);
            jsonObject.put("n", 1);
            jsonObject.put("size", "1024x1024");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // json 문자열로 변환
        String json = jsonObject.toString();

        // OkHttpClient 생성 (타임아웃 설정 포함) -> 일정 시간 이후에 응답 없으면 끊어냄
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        // RequestBody 생성
        RequestBody requestBody = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );

        // Request 생성
        Request request = new Request.Builder()
                .url(DALLE_API_ENDPOINT)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + OPENAI_API_KEY)
                .post(requestBody)
                .build();
        String imageUrl = "";

        // 요청 실행 및 응답 처리
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // 성공적인 응답 처리
                String responseBody = response.body().string();
                System.out.println("Response: " + responseBody);

                // JSON 응답에서 이미지 URL 추출
                JSONObject jsonResponse = new JSONObject(responseBody);
                imageUrl = jsonResponse.getJSONArray("data").getJSONObject(0).getString("url");

                // 이미지 제대로 받아왔는지 확인용 코드
                // 이미지 URL에서 파일(test.png)로 저장
                saveImageFromUrl(imageUrl, "test.png");

            } else {
                // 실패한 응답 처리
                System.out.println("Request failed with status code: " + response.code());
                System.out.println("Response body: " + response.body().string());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return imageUrl;
    }

    // 이미지 확인용 루트폴더에 저장하는 함수
    private static void saveImageFromUrl(String imageUrl, String fileName){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(imageUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                File file = new File(fileName);
                File parentDir = file.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();  // 디렉토리가 없으면 생성
                }

                InputStream inputStream = response.body().byteStream();
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                }
                System.out.println("Image saved to " + file.getAbsolutePath());
            } else {
                System.err.println("Failed to download image with status code: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}