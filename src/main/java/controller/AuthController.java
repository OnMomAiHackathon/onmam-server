package controller;

import dto.auth.login.KakaoLoginRequest;
import dto.auth.login.LoginRequest;
import dto.auth.login.LoginResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.KakaoAuthService;
import service.UserService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {
    private final UserService userService;
    private final KakaoAuthService kakaoAuthService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request, HttpSession session) {
        Optional<LoginResponse> response = userService.loginUser(request);
        // 로그인 성공 시 세션에 사용자 정보 저장
        session.setAttribute("user", response);
        session.setAttribute("groupId",response.get().getGroupId());//그룹아이디도 저장
        return ResponseEntity.ok(response.get());
    }

    //로그아웃
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();  // 세션 무효화
        return ResponseEntity.ok().build();
    }


    // 카카오톡 회원가입 및 로그인
    @PostMapping("/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestBody KakaoLoginRequest request, HttpSession session) {
        // 카카오 OAuth2.0을 통해 사용자 정보 가져오기
        Optional<LoginResponse> response = kakaoAuthService.kakaoLogin(request.getAccessToken());

        // 로그인 성공 시 세션에 사용자 정보 저장
        session.setAttribute("user", response);
        session.setAttribute("groupId",response.get().getGroupId());
        return ResponseEntity.ok(response.get());
    }
}
