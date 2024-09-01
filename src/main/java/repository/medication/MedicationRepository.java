package repository.medication;

import entity.group.OnmomGroup;
import entity.medication.OnmomMedication;
import entity.user.OnmomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MedicationRepository extends JpaRepository<OnmomMedication,Long> {
    List<OnmomMedication> findByGroup(OnmomGroup group);

    List<OnmomMedication> findByGroupAndUserAndStartDateLessThanEqualAndEndDateGreaterThanEqual(OnmomGroup group, OnmomUser user, LocalDate stratDate, LocalDate endDate);
}
