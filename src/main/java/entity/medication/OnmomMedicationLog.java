package entity.medication;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 복약 기록을 관리하는 Entity
@Entity
@Table(name = "onmomMedicationLog")
@Getter
@NoArgsConstructor
public class OnmomMedicationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    // 여러 복약 기록은 하나의 복약정보를 갖는다.
    @ManyToOne
    @JoinColumn(name = "medicationId")
    private OnmomMedication medication;

    private LocalDateTime eatTime; // 먹은 시간
    private boolean taken; // 복용 여부

    @Builder
    public OnmomMedicationLog(Long logId, OnmomMedication medication, LocalDateTime eatTime, boolean taken) {
        this.logId = logId;
        this.medication = medication;
        this.eatTime = eatTime;
        this.taken = taken;
    }
}
