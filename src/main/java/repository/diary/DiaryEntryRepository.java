package repository.diary;

import entity.diary.OnmomDiaryEntry;
import entity.group.OnmomGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DiaryEntryRepository extends JpaRepository<OnmomDiaryEntry,Long> {
    List<OnmomDiaryEntry> findByGroup(OnmomGroup group);

    List<OnmomDiaryEntry> findByGroupAndCreatedAtBetween(OnmomGroup group, LocalDate startDate, LocalDate endDate);
}
