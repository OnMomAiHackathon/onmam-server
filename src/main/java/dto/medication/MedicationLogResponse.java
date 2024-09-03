package dto.medication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class MedicationLogResponse {
    private Long logId;
    private LocalDateTime eatTime;
}