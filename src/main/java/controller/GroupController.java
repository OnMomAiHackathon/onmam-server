package controller;

import dto.group.*;
import dto.group.invite.InviteAcceptRequest;
import dto.group.invite.InviteAcceptResponse;
import entity.group.OnmomGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.OnmomGroupService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("groups")
public class GroupController {
    private final OnmomGroupService onmomGroupService;

    // 그룹 생성
    @PostMapping
    public ResponseEntity<GroupCreateResponse> createGroup(@RequestBody GroupCreateRequest groupCreateRequest) {
        GroupCreateResponse response = onmomGroupService.createGroup(groupCreateRequest);
        return ResponseEntity.ok(response);
    }


    // 그룹 멤버 수정
    @PostMapping("/{groupId}/members")
    public ResponseEntity<GroupMemberUpdateResponse> updateGroupMembers(
            @PathVariable Long groupId,
            @ModelAttribute GroupMemberUpdateRequest updateRequest) throws IOException {

        GroupMemberUpdateResponse response = onmomGroupService.updateGroupMembers(groupId, updateRequest);
        return ResponseEntity.ok(response);
    }

    // 초대코드 보내기
    @PostMapping("/{groupId}/invite")
    public ResponseEntity<Map<String,String>> sendGroupInvite(@PathVariable Long groupId) {
        String inviteCode = onmomGroupService.sendInvite(groupId);
        // 응답 데이터 구성
        Map<String, String> response = new HashMap<>();
        response.put("message", inviteCode);

        return ResponseEntity.ok(response);
    }

    // 가족 초대 수락
    @PostMapping("/{groupId}/invite/accept")
    public ResponseEntity<InviteAcceptResponse> acceptInvite(
            @PathVariable Long groupId,
            @RequestBody InviteAcceptRequest request) {
        InviteAcceptResponse response = onmomGroupService.acceptInvite(groupId, request);
        return ResponseEntity.ok(response);
    }

    // 그룹 삭제
    @PostMapping("/{groupId}/delete")
    public ResponseEntity<Map<String,String>> deleteGroup(@PathVariable Long groupId){
        onmomGroupService.deleteGroup(groupId);
        Map<String,String> response = new HashMap<>();
        response.put("message","그룹이 성공적으로 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 특정 그룹 조회
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupFindByIdResponse> findGroupById(@PathVariable Long groupId){
        OnmomGroup group = onmomGroupService.findGroupById(groupId);

        List<GroupUserResponse> members = group.getUsers().stream()
                .map(user -> GroupUserResponse.builder()
                        .userId(user.getUserId())
                        .name(user.getName())
                        .role(user.getRole())
                        .build()
                ).toList();

        GroupFindByIdResponse response = GroupFindByIdResponse.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .members(members)
                .build();
        return ResponseEntity.ok(response);

    }

}
