package controller;

import dto.user.get.UserResponseDto;
import dto.user.join.UserJoinRequest;
import dto.user.join.UserJoinResponse;
import entity.user.OnmomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import service.UserService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    // 회원 가입
    @PostMapping
    public ResponseEntity<UserJoinResponse> joinUser(@RequestBody UserJoinRequest request) {
        OnmomUser user = userService.joinUser(request);
        UserJoinResponse response = new UserJoinResponse(user.getUserId(), "회원 가입 성공");
        return ResponseEntity.ok(response);
    }

    // 회원 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        UserResponseDto userResponse = userService.getUserById(userId);
        return ResponseEntity.ok(userResponse);
    }

    // 카카오 OAuth2 로그인 성공 후 사용자 정보를 처리하는 엔드포인트
    @GetMapping("/success")
    public ResponseEntity<UserResponseDto> oauth2LoginSuccess(OAuth2AuthenticationToken authentication) {
        OAuth2User oauth2User = authentication.getPrincipal();

        String kakaoEmail = ((Map<String, Object>) oauth2User.getAttribute("kakao_account")).get("email").toString();
        String name = ((Map<String, Object>) oauth2User.getAttribute("properties")).get("nickname").toString();

        // 유저 생성 또는 조회
        OnmomUser user = userService.findOrCreateUser(kakaoEmail, name);

        // 액세스 토큰 생성 (여기서는 예시로 OAuth2User의 ID를 사용)
        String accessToken = authentication.getPrincipal().getAttribute("id").toString();

        // UserResponseDto 빌드
        UserResponseDto response = userService.buildUserResponseDto(authentication, user, accessToken);

        return ResponseEntity.ok(response);
    }

    // 로그인 실패 시 처리할 엔드포인트
    @GetMapping("/failure")
    public ResponseEntity<String> oauth2LoginFailure() {
        return ResponseEntity.badRequest().body("카카오 로그인 실패");
    }

    // user아이디에 따른 groupId를 반환하는 api
    @GetMapping("/groupId")
    public ResponseEntity<Map<String,String>> getGroupIdByUserId(@RequestParam Long userId){
        String groupId = userService.getGroupIdByUserId(userId);
        Map<String,String> response = new HashMap<>();
        response.put("groupId",groupId.toString());
        return ResponseEntity.ok(response);
    }
}