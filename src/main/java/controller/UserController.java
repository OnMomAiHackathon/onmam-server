package controller;

import dto.user.join.UserJoinRequest;
import dto.user.join.UserJoinResponse;
import entity.user.OnmomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    // 회원 가입
    // 관련 dto.user : UserJoinRequest,  UserJoinResponse
    // 관련 exception.user : UserAlreadyExistsException (이메일 중복 체크)
    @PostMapping
    public ResponseEntity<UserJoinResponse> joinUser(@RequestBody UserJoinRequest request) {
        OnmomUser user = userService.joinUser(request);
        UserJoinResponse response = new UserJoinResponse(user.getUserId(), "회원 가입 성공");
        return ResponseEntity.ok(response);
    }


}
