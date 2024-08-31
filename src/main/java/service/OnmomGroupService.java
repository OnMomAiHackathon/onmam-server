package service;

import dto.group.GroupCreateRequest;
import dto.group.GroupCreateResponse;
import dto.group.GroupMemberUpdateRequest;
import dto.group.GroupMemberUpdateResponse;
import dto.group.invite.InviteAcceptRequest;
import dto.group.invite.InviteAcceptResponse;
import entity.group.OnmomGroup;
import entity.group.UserNickname;
import entity.user.OnmomUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import repository.group.GroupRepository;
import repository.group.UserNicknameRepository;
import repository.user.UserRepository;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OnmomGroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserNicknameRepository userNicknameRepository;
    private final S3Service s3Service;


    // 그룹 생성!
    @Transactional
    public GroupCreateResponse createGroup(GroupCreateRequest groupRequest) throws IOException {
        // 1. 그룹 생성
        OnmomGroup group = OnmomGroup.builder()
                .groupName(groupRequest.getGroupName())
                .createdAt(LocalDate.now())
                .build();

        group = groupRepository.save(group);//그룹 저장 (그룹id생성을위함)


        // 그룹 이미지를 S3에 업로드하고, 이미지 URL을 그룹 엔티티에 설정
        if (groupRequest.getGroupImage() != null) {
            String imageUrl = s3Service.uploadGroupImage(groupRequest.getGroupImage(), group.getGroupId());
            group.setGroupImageUrl(imageUrl);
            groupRepository.save(group);//이미지설정된 그룹을 한 번 더 저장
        }


        // 2. 사용자 업데이트
        OnmomUser user = userRepository.findById(groupRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 유저아이디입니다."));
        user.setGroup(group);//유저 그룹 세팅
        user.setRole(groupRequest.getRole());//유저 역할 부여
        userRepository.save(user);

        // 3. 응답 생성
        return GroupCreateResponse.builder()
                .groupId(group.getGroupId())
                .message("그룹 생성 성공")
                .build();
    }


    // 그룹 멤버 수정 로직
    public GroupMemberUpdateResponse updateGroupMembers(Long groupId, GroupMemberUpdateRequest updateRequest) throws IOException {
        OnmomGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 group ID"));

        // 그룹 이름 업데이트
        group.setGroupName(updateRequest.getGroupName());
        groupRepository.save(group);

        // 멤버들에 대한 닉네임과 프로필 이미지 업데이트
        for (GroupMemberUpdateRequest.Member memberRequest : updateRequest.getMembers()) {
            OnmomUser user = userRepository.findById(memberRequest.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 user ID"));

            // S3에 프로필 이미지 업로드
            String profileImageUrl = null;
            MultipartFile profileImageFile = memberRequest.getProfileImageFile();
            if (profileImageFile != null && !profileImageFile.isEmpty()) {
                profileImageUrl = s3Service.uploadProfileImage(profileImageFile, groupId, user.getUserId(), user.getUserId());
            }

            // 닉네임 및 프로필 이미지 업데이트 로직
            UserNickname userNickname = userNicknameRepository.findByUserAndGroup(user, group)
                    .orElse(UserNickname.builder()
                            .user(user)
                            .group(group)
                            .targetUser(user)  // 본인을 대상으로 하는 경우
                            .nickname(memberRequest.getNickname())
                            .profileImageUrl(profileImageUrl)
                            .build());

            if (profileImageUrl != null) {
                userNickname.updateProfileImageUrl(profileImageUrl);
            }
            userNickname.updateNickname(memberRequest.getNickname());
            userNicknameRepository.save(userNickname);
        }

        return GroupMemberUpdateResponse.builder()
                .message("그룹 멤버 수정 성공")
                .build();
    }


    // 초대 코드 생성 및 전송
    public String sendInvite(Long groupId) {
        OnmomGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 그룹아이디입니다."));

        // 초대 코드 생성
        String inviteCode = generateInviteCode(5); // 5글자의 초대 코드 생성
        group.setInvitationCode(inviteCode);  // 초대 코드 설정
        groupRepository.save(group);  // 그룹 저장

        return inviteCode;
    }

    private String generateInviteCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new SecureRandom();
        StringBuilder inviteCode = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            inviteCode.append(characters.charAt(random.nextInt(characters.length())));
        }

        return inviteCode.toString();
    }

    // 초대 수락 로직
    public InviteAcceptResponse acceptInvite(Long groupId, InviteAcceptRequest request) {
        // 1. 그룹과 초대 코드 확인
        OnmomGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 그룹 아이디입니다."));

        if (!group.getInvitationCode().equals(request.getInviteCode())) {
            throw new IllegalArgumentException("유효하지 않은 초대 코드입니다.");
        }

        // 2. 사용자가 이미 그룹에 속해 있는지 확인
        OnmomUser user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 아이디입니다."));

        if (user.getGroup() != null) {
            throw new IllegalArgumentException("사용자가 이미 다른 그룹에 속해있습니다.");
        }

        // 3. 그룹에 사용자 추가
        user.setGroup(group);
        userRepository.save(user);

        // 4. 응답 생성
        return InviteAcceptResponse.builder()
                .message("초대가 성공적으로 수락되었습니다.")
                .build();
    }

    @Transactional
    public void deleteGroup(Long groupId) {
        OnmomGroup group = groupRepository.findById(groupId).orElseThrow(()
                -> new IllegalArgumentException("유효하지 않은 그룹아이디입니다."));

        //해당 groupId를 가지고 있는 유저들의 그룹을 null로 설정
        userRepository.findByGroup(group)
                .forEach(onmomUser -> {
                    onmomUser.setGroup(null);
                    userRepository.save(onmomUser);
                });

        //그룹 삭제
        groupRepository.deleteById(groupId);
    }

    public OnmomGroup findGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 그룹아이디입니다."));
    }
}
