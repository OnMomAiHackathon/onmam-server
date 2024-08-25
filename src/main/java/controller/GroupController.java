package controller;

import dto.group.GroupCreateRequest;
import dto.group.GroupCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.OnmomGroupService;

@RestController
@RequiredArgsConstructor
@RequestMapping("groups")
public class GroupController {
    private final OnmomGroupService onmomGroupService;

    @PostMapping
    public ResponseEntity<GroupCreateResponse> createGroup(@RequestBody GroupCreateRequest groupCreateRequest) {
        GroupCreateResponse response = onmomGroupService.createGroup(groupCreateRequest);
        return ResponseEntity.ok(response);
    }
}
