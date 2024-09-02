package dto.main.groupImage;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class GroupImageResponse {
    private Long id;
    private String imageUrl;
    private LocalDateTime uploadedAt;
    private Long userId;
    private String userName;
}
