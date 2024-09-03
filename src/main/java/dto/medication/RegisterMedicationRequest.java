package dto.medication;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterMedicationRequest {
    private Long userId;
    private Long groupId;
    private String medicineName; // 약 이름
    private LocalDate startDate; // 뵥약 시작 날짜
    private LocalDate endDate; // 복약 종료 날짜
    private int frequency; // 하루 몇회?
}
