package repository.diary;

import entity.diary.OnmomDiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryEntryRepository extends JpaRepository<OnmomDiaryEntry,Long> {
}
