package controller;

import dto.user.get.UserResponseDto;
import dto.user.join.UserJoinRequest;
import dto.user.join.UserJoinResponse;
import entity.user.OnmomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    // 회원 가입
    // 관련 exception.user : UserAlreadyExistsException (이메일 중복 체크)
    @PostMapping
    public ResponseEntity<UserJoinResponse> joinUser(@RequestBody UserJoinRequest request) {
        OnmomUser user = userService.joinUser(request);
        UserJoinResponse response = new UserJoinResponse(user.getUserId(), "회원 가입 성공");
        return ResponseEntity.ok(response);
    }

    // 회원 정보 조회
    // 관련 exception.user : UserNotFoundException (유저를 찾을 수 없습니다)
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        UserResponseDto userResponse = userService.getUserById(userId);
        return ResponseEntity.ok(userResponse);
    }
}
