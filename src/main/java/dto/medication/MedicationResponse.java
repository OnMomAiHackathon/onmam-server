package dto.medication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MedicationResponse {
    private Long medicationId;
    private String medicineName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int frequency;
    private int totalDosage; // 날짜와 복용빈도로 계산된 총 복용 횟수
    private int remainingDosage; // 남은 복용 횟수
    private List<MedicationLogResponse> logs; // 복약 로그 리스트
}
