package dto.diary;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryAuthRequest {
    private Long groupId;
    private Long userId;
    private int year;
    private int month;

}
