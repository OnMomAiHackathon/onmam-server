package entity.medication;

import entity.user.OnmomUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

// 복약 정보를 저장하는 Entity
@Entity
@Table(name = "onmomMedication")
@Getter
@NoArgsConstructor
public class OnmomMedication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicationId;

    //여러 복약 정보가 하나의 user와 관계를 가진다.
    @ManyToOne
    @JoinColumn(name = "userId")
    private OnmomUser user;

    private String medicineName; // 약 이름
    private LocalDate startDate; // 뵥약 시작 날짜
    private LocalDate endDate; // 복약 종료 날짜
    private String frequency; // 하루 몇회?

    // 하나의 복약정보는 여러 복약 로그데이터를 가질 수 있다.
    @OneToMany(mappedBy = "medication")
    private Set<OnmomMedicationLog> medicationLogs;

    @Builder
    public OnmomMedication(Long medicationId, OnmomUser user, String medicineName, LocalDate startDate, LocalDate endDate, String frequency, Set<OnmomMedicationLog> medicationLogs) {
        this.medicationId = medicationId;
        this.user = user;
        this.medicineName = medicineName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.frequency = frequency;
        this.medicationLogs = medicationLogs;
    }
}
