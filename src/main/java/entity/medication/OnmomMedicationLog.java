package entity.medication;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// 복약 기록을 관리하는 Entity
@Entity
@Table(name = "onmomMedicationLog")
@Getter
@Setter
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
}
