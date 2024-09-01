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
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import repository.user.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    //회원 가입
    public OnmomUser joinUser(UserJoinRequest request) {

        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("이미 사용 중인 이메일입니다.");
        }

        OnmomUser user = OnmomUser.builder()
                .email(request.getEmail())
                .gender(request.getGender())
                .password(request.getPassword())
                .name(request.getName())
                .birthdate(request.getBirthdate())
                .phone(request.getPhone())
                .build();

        return userRepository.save(user);
    }

    // 회원 정보 조회
    public UserResponseDto getUserById(Long userId) {
        OnmomUser user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("유저를 찾을 수 없습니다."));
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .kakaoId(user.getKakaoId())
                .email(user.getEmail())
                .name(user.getName())
                .birthdate(user.getBirthdate())
                .phone(user.getPhone())
                .role(user.getRole())
                .gender(user.getGender())
                .build();
    }


    //로그인
    public Optional<LoginResponse> loginUser(LoginRequest request) {
        // 이메일로 사용자 조회
        OnmomUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("이메일 또는 비밀번호가 잘못되었습니다."));

        // 비밀번호 검증
        if (!user.getPassword().equals(request.getPassword())) {
            throw new InvalidCredentialsException("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        // 그룹이 null일 수 있으므로 안전하게 처리
        Long groupId = (user.getGroup() != null) ? user.getGroup().getGroupId() : null;

        // 로그인 성공 시 사용자 정보 반환
        return Optional.of(new LoginResponse(user.getUserId(), user.getKakaoId(), user.getEmail(), user.getName(), groupId));
    }

    //카카오로그인시 이메일을 찾고, 없으면 회원가입한다.
    public OnmomUser findOrCreateUser(String kakaoEmail, String name) {
        return userRepository.findByKakaoId(kakaoEmail)
                .orElseGet(() -> userRepository.save(
                        OnmomUser.builder()
                                .kakaoId(kakaoEmail)
                                .email(kakaoEmail)
                                .name(name)
                                .build()
                ));
    }

    public UserResponseDto buildUserResponseDto(OAuth2AuthenticationToken authentication, OnmomUser user, String accessToken) {
        OAuth2User oauth2User = authentication.getPrincipal();

        // 필요한 경우 추가 정보를 여기서 처리

        return UserResponseDto.builder()
                .userId(user.getUserId())
                .kakaoId(user.getKakaoId())
                .email(user.getEmail())
                .name(user.getName())
                .birthdate(user.getBirthdate())
                .phone(user.getPhone())
                .role(user.getRole())
                .gender(user.getGender())
                .accessToken(accessToken)  // 액세스 토큰 설정
                .build();
    }
}
