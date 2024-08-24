package service;

import dto.user.get.UserResponseDto;
import dto.user.join.UserJoinRequest;
import entity.user.OnmomUser;
import exception.user.get.UserNotFoundException;
import exception.user.join.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.user.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    //회원 가입
    public OnmomUser joinUser(UserJoinRequest request) {

        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) { //같은 Email이 있다면
            throw new UserAlreadyExistsException("이미 사용 중인 이메일입니다.");
        }

        OnmomUser user = OnmomUser.builder()
                .email(request.getEmail())// 1. 이메일
                .password(request.getPassword())// 2. 패스워드
                .name(request.getName())// 3. 이름
                .birthdate(request.getBirthdate())// 4. 생년월일
                .phone(request.getPhone())// 5. 휴대폰번호
                .build();
                //카카오 id, 그룹, role은 회원가입시 등록하지 않는다.

        return userRepository.save(user);
    }

    // 회원 정보 조회
    public UserResponseDto getUserById(Long userId) {
        OnmomUser user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("유저를 찾을 수 없습니다."));
        return new UserResponseDto(user);
    }
}
