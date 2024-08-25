package service;

import dto.group.GroupCreateRequest;
import dto.group.GroupCreateResponse;
import entity.group.OnmomGroup;
import entity.user.OnmomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.group.GroupRepository;
import repository.user.UserRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OnmomGroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

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

}
