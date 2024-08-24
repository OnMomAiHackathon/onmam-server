package controller;

import dto.auth.login.LoginRequest;
import dto.auth.login.LoginResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {
    private final UserService userService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request, HttpSession session) {
        LoginResponse response = userService.loginUser(request);
        // 로그인 성공 시 세션에 사용자 정보 저장
        session.setAttribute("user", response);
        return ResponseEntity.ok(response);
    }

    //로그아웃
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();  // 세션 무효화
        return ResponseEntity.ok().build();
    }
}
