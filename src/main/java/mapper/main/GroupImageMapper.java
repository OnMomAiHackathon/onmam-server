package mapper.main;

import dto.main.groupImage.GroupImageResponse;
import entity.group.GroupImage;

public class GroupImageMapper {
    public static GroupImageResponse toResponse(GroupImage groupImage) {
        GroupImageResponse response = GroupImageResponse.builder()
                .id(groupImage.getId())
                .userId(groupImage.getUser().getUserId())
                .userName(groupImage.getUser().getName())
                .uploadedAt(groupImage.getUploadedAt())
                .imageUrl(groupImage.getImageUrl())
                .build();

        return response;
    }
}
