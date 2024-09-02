package repository.group;

import entity.group.GroupImage;
import entity.group.OnmomGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<OnmomGroup,Long> {
    Optional<OnmomGroup> findByInvitationCode(String inviteCode);
}
