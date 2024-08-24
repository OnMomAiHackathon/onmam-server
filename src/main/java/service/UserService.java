package service;

import dto.auth.login.LoginRequest;
import dto.auth.login.LoginResponse;
import dto.user.get.UserResponseDto;
import dto.user.join.UserJoinRequest;
import entity.user.OnmomUser;
import exception.auth.login.InvalidCredentialsException;
import exception.user.get.UserNotFoundException;
import exception.user.join.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.user.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //회원 가입
    public OnmomUser joinUser(UserJoinRequest request) {

        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        OnmomUser user = OnmomUser.builder()
                .email(request.getEmail())
                .password(encodedPassword)  // 암호화된 비밀번호 저장
                .name(request.getName())
                .birthdate(request.getBirthdate())
                .phone(request.getPhone())
                .build();

        return userRepository.save(user);
    }

    // 회원 정보 조회
    public UserResponseDto getUserById(Long userId) {
        OnmomUser user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("유저를 찾을 수 없습니다."));
        return new UserResponseDto(user);
    }


    //로그인
    public LoginResponse loginUser(LoginRequest request) {
        // 이메일로 사용자 조회
        OnmomUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("이메일 또는 비밀번호가 잘못되었습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        // 로그인 성공 시 사용자 정보 반환
        return new LoginResponse(user.getUserId(), user.getKakaoId(), user.getEmail(), user.getName());
    }
}
