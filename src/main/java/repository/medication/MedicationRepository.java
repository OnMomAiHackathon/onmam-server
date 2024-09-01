package repository.medication;

import entity.group.OnmomGroup;
import entity.medication.OnmomMedication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationRepository extends JpaRepository<OnmomMedication,Long> {
    List<OnmomMedication> findByGroup(OnmomGroup group);
}
