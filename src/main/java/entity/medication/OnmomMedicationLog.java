package entity.medication;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 복약 기록을 관리하는 Entity
@Entity
@Table(name = "onmom_medication_log")
@Getter
@NoArgsConstructor
public class OnmomMedicationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne
    @JoinColumn(name = "medication_id")
    private OnmomMedication medication;

    private LocalDateTime eatTime; // 먹은 시간

    @Builder
    public OnmomMedicationLog(Long logId, OnmomMedication medication, LocalDateTime eatTime) {
        this.logId = logId;
        this.medication = medication;
        this.eatTime = eatTime;
    }
}