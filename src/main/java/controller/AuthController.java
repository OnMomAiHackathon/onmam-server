package controller;

import dto.auth.login.KakaoLoginRequest;
import dto.auth.login.LoginRequest;
import dto.auth.login.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.KakaoAuthService;
import service.UserService;



import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
@Tag(name = "인증 관련 API")
public class AuthController {
    private final UserService userService;
    private final KakaoAuthService kakaoAuthService;

    // 로그인
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "유저 로그인 기능")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request, HttpSession session) {
        Optional<LoginResponse> response = userService.loginUser(request);
        // 로그인 성공 시 세션에 사용자 정보 저장
        session.setAttribute("user", response);
        session.setAttribute("groupId",response.get().getGroupId());//그룹아이디도 저장
        return ResponseEntity.ok(response.get());
    }

    //로그아웃
    @GetMapping("/logout")
    @Operation(summary = "로그아웃", description = "유저 로그아웃 수행")
    public ResponseEntity<Map<String,String>> logout(HttpSession session) {
        session.invalidate();  // 세션 무효화
        Map<String,String> response = new HashMap<>();
        response.put("message", "로그아웃되었습니다.");
        return ResponseEntity.ok(response);
    }


    // 카카오톡 회원가입 및 로그인
    @PostMapping("/kakao")
    @Operation(summary = "카카오톡 회원가입 및 로그인", description = "카카오톡을 통해 회원가입 및 로그인을 수행합니다.")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestBody KakaoLoginRequest request, HttpSession session) {
        // 카카오 OAuth2.0을 통해 사용자 정보 가져오기
        Optional<LoginResponse> response = kakaoAuthService.kakaoLogin(request.getAccessToken());

        // 로그인 성공 시 세션에 사용자 정보 저장
        session.setAttribute("user", response);
        session.setAttribute("groupId",response.get().getGroupId());
        return ResponseEntity.ok(response.get());
    }
}
