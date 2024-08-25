package service;

import dto.group.GroupCreateRequest;
import dto.group.GroupCreateResponse;
import dto.group.GroupMemberUpdateRequest;
import dto.group.GroupMemberUpdateResponse;
import entity.group.OnmomGroup;
import entity.group.UserNickname;
import entity.user.OnmomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import repository.group.GroupRepository;
import repository.group.UserNicknameRepository;
import repository.user.UserRepository;

import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OnmomGroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserNicknameRepository userNicknameRepository;
    private final S3Service s3Service;

    // 그룹 생성!
    public GroupCreateResponse createGroup(GroupCreateRequest groupRequest) {
        // 1. 그룹 생성
        OnmomGroup group = OnmomGroup.builder()
                .groupName(groupRequest.getGroupName())
                .createdAt(LocalDate.now())
                .build();
        groupRepository.save(group);

        // 2. 사용자 업데이트 (그룹에 연결)
        OnmomUser user = userRepository.findById(groupRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        user.setGroup(group);  // 사용자에게 그룹 정보 설정
        userRepository.save(user);  // 사용자 정보 업데이트

        // 3. 응답 생성
        return GroupCreateResponse.builder()
                .groupId(group.getGroupId())
                .message("그룹 생성 성공")
                .build();
    }



    // 그룹 멤버 수정 로직
    public GroupMemberUpdateResponse updateGroupMembers(Long groupId, GroupMemberUpdateRequest updateRequest) throws IOException {
        OnmomGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid group ID"));

        // 그룹 이름 업데이트
        group.setGroupName(updateRequest.getGroupName());
        groupRepository.save(group);

        // 멤버들에 대한 닉네임과 프로필 이미지 업데이트
        for (GroupMemberUpdateRequest.Member memberRequest : updateRequest.getMembers()) {
            OnmomUser user = userRepository.findById(memberRequest.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

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

}
