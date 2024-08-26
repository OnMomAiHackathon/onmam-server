package repository.diary;

import entity.diary.OnmomDiaryEntry;
import entity.group.OnmomGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryEntryRepository extends JpaRepository<OnmomDiaryEntry,Long> {
    List<OnmomDiaryEntry> findByGroup(OnmomGroup group);
}
