package repository.medication;

import entity.medication.OnmomMedicationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationLogRepository extends JpaRepository<OnmomMedicationLog,Long> {

}
