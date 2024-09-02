package repository.group;

import entity.group.GroupImage;
import entity.group.OnmomGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupImageRepository extends JpaRepository<GroupImage,Long> {
    List<GroupImage> findByGroup(OnmomGroup group);
}
