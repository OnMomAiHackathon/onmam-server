package repository.medication;

import entity.medication.OnmomMedication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<OnmomMedication,Long> {
}
