package controller;

import dto.group.GroupCreateRequest;
import dto.group.GroupCreateResponse;
import dto.group.GroupMemberUpdateRequest;
import dto.group.GroupMemberUpdateResponse;
import dto.group.invite.InviteAcceptRequest;
import dto.group.invite.InviteAcceptResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.OnmomGroupService;

import java.io.IOException;
import java.util.HashMap;
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
    public ResponseEntity<Map<String,String>> sendGroupInvite(@PathVariable Long groupId, @RequestBody String email) {
        String inviteCode = onmomGroupService.sendInvite(groupId, email);
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
}
