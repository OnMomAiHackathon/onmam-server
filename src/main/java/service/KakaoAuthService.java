package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.auth.login.LoginResponse;
import entity.user.OnmomUser;
import exception.auth.login.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import repository.user.UserRepository;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {
    private final UserRepository userRepository;

    public Optional<LoginResponse> kakaoLogin(String accessToken) {
        // 카카오 API를 통해 사용자 정보 가져오기
        OnmomUser user = getKakaoUser(accessToken);

        // 사용자 정보로 로그인 처리
        if (!userRepository.existsByEmail(user.getEmail())) {
            // 존재하지 않으면 회원가입 처리
            userRepository.save(user);
        }

        // 로그인 성공 시 사용자 정보 반환
        return Optional.of(new LoginResponse(user.getUserId(), user.getKakaoId(), user.getEmail(), user.getName(), user.getGroup().getGroupId()));
    }

    private OnmomUser getKakaoUser(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode body = mapper.readTree(response.getBody());

                // 카카오톡에서 받은 사용자 정보 파싱
                String kakaoId = body.get("id").asText();
                String email = body.path("kakao_account").path("email").asText();
                String name = body.path("properties").path("nickname").asText();

                // OnmomUser 객체로 변환
                return OnmomUser.builder()
                        .kakaoId(kakaoId)
                        .email(email)
                        .name(name)
                        .build();
            } catch (IOException e) {
                throw new InvalidCredentialsException("카카오 사용자 정보 파싱에 실패했습니다.");
            }
        } else {
            throw new InvalidCredentialsException("카카오 로그인에 실패했습니다.");
        }
    }

}
