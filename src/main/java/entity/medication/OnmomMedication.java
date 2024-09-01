package entity.medication;

import entity.group.OnmomGroup;
import entity.user.OnmomUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    @ManyToOne
    @JoinColumn(name = "userId")
    private OnmomUser user;

    @ManyToOne
    @JoinColumn(name = "groupId")
    private OnmomGroup group;

    private String medicineName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int frequency; // 하루 몇회?

    private int totalDosage; // 총복용량
    private int remainingDosage; // 남은 약 개수

    @OneToMany(mappedBy = "medication")
    private Set<OnmomMedicationLog> medicationLogs;

    @Builder
    public OnmomMedication(Long medicationId, OnmomUser user, String medicineName, LocalDate startDate, LocalDate endDate, int frequency, Set<OnmomMedicationLog> medicationLogs, OnmomGroup group, int totalDosage, int remainingDosage) {
        this.medicationId = medicationId;
        this.user = user;
        this.medicineName = medicineName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.frequency = frequency;
        this.medicationLogs = medicationLogs;
        this.group = group;
        this.totalDosage = totalDosage;
        this.remainingDosage = remainingDosage;
    }

    public void setRemainingDosage(int remainingDosage) {
        this.remainingDosage =  remainingDosage;
    }
}
