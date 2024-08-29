package dto.ai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AIDiaryResponse {
    private String summary;
    private String translatedContent;
    private String imageURL;
    private boolean medicationStatus;
}