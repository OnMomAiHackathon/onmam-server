package repository.group;

import entity.group.OnmomGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<OnmomGroup,Long> {
}
